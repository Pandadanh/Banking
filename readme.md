# Banking System Architecture

## Database
- PostgreSQL
- Redis
- Elasticsearch

## Programming Language & Platform
- Java 17

## Frameworks & Libraries
- Spring Boot
- Spring Security 6
- Spring Cloud
- Feign Client

## Microservices & Service Discovery
- Eureka
- Gateway
- Load Balance

## Security
- Keycloak

## Messaging & Queue
- RabbitMQ
- Kafka

## Containerization & Orchestration
- Docker
- Kubernetes

## CI/CD & DevOps
- Jenkins

## Monitoring & Tracing
- Grafana
- Prometheus
- Zipkin

## Mapping & Geolocation
- MapStruct

---

## Flow Security

```
request -> gateway  gắn thêm publickey ở header -> authen -> gateway -> mdm -> gateway thu hồi publickey ở header nếu có
```

### Flow Diagram
```
[Client] 
   |
   | HTTP request with JWT
   ↓
[Gateway Service]
   |
   |→ Gắn thêm "X-Public-Key: ..." (hoặc một header khác)
   |→ Gửi token sang Authen để verify
   ↓
[Authen Service]
   |→ Check JWT, xác thực, trả thông tin user hoặc lỗi
   ↓
[Gateway Service]
   |→ Nếu token hợp lệ:
   |     → Forward request đến MDM
   ↓
[MDM Service]
   |→ Nhận request (kèm các headers và context user nếu cần ) 
   ↓
[Gateway Service]
   |→ Xoá X-Public-Key khỏi header (hoặc làm sạch sensitive data)
   ↓
[Client]
   |← Trả response
```

### Security Notes
- Tất cả các service ngoại trừ gateway đều phải kiểm tra jwt của gateway gửi xem có đúng secrectKey không 
- Gateway phải xác thực bằng RSA khi từ client gửi vô
- Cần thêm:
  - Audit Logging
  - Token Expiry + Revocation
  - Rate Limiting tại Gateway
  - Vault quản lý Secret/Key

### Security Components
1. **SecurityConfig**: Cấu hình các rule bảo mật (path nào được phép, filter nào chạy trước)
2. **JwtFilter**: Đọc JWT từ request, kiểm tra tính hợp lệ, load thông tin người dùng
3. **JwtUtils**: Xử lý tạo, giải mã, xác minh chữ ký JWT (RSA hoặc HS256)
4. **AuthenticationEntryPoint** (tuỳ chọn): Trả lỗi nếu người dùng không xác thực
5. **UserDetails + Authentication**: Dùng để lưu username, roles vào context

### Authentication Flow
```
Client sẽ gửi token -> gateway call authen -> authen verify check trong catch redis có không 
-> gateway call mdm -> mdm verify -> mdm response -> gateway response -> client
```

### Security Requirements
- HTTPS + TLS mTLS

### Detailed Architecture
```
                        ┌────────────────────────────┐
                        │         [ Client ]         │
                        │    (Web / Mobile App)      │
                        └────────────┬───────────────┘
                                     │ ① Gửi JWT (Bearer)
                                     ▼
                        ┌────────────────────────────┐
                        │     [ API Gateway ]        │
                        │ - Verify JWT bằng PublicKey│
                        │ - Gắn header nội bộ        │
                        └────────────┬───────────────┘
                                     │ ② Gửi Request nội bộ
                                     │    → Gắn thêm:
                                     │       - X-Internal-Auth: HMAC(ts + serviceId)
                                     ▼
        ┌──────────────────────────────────────────────────────────────┐
        │                      [ Authen Service ]                      │
        │ - Có PrivateKey (RSA)                                        │
        │ - Sinh JWT (gửi cho Client)                                  │
        │ - Trả PublicKey cho Gateway (nếu cần)                        │
        └──────────────────────────────────────────────────────────────┘

                                     │
                                     │ ③ Forward đến các service nội bộ
                                     ▼
        ┌─────────────────────────────────────┐
        │             [ MDM Service ]         │
        │ - Kiểm tra X-Internal-Auth          │
        │   (bằng SecretKey + timestamp)      │
        │ - Kiểm tra Role từ JWT (nếu cần)    │
        │ - Chỉ nhận request từ Gateway       │
        └─────────────────────────────────────┘

                                     ▼
                        ┌────────────────────────────┐
                        │     [ Response trả về ]     │
                        │ → Gateway → Client          │
                        └────────────────────────────┘
```

---

# 🧾 .NET Import Employee Service

## 🔍 Mục đích

`.NET Import Employee Service` là một microservice được thiết kế để **tự động import danh sách nhân viên** từ hệ thống Master Data Management (MDM) sang hệ thống nội bộ. Việc xử lý import được thực hiện theo lịch trình định sẵn bằng **Hangfire**, đồng thời sử dụng **Service Bus (SB)** để chuyển tiếp dữ liệu đến một **service lưu trữ riêng biệt** (Import Result Service).

## ⚙️ Công nghệ sử dụng

- **ASP.NET Core**
- **Hangfire** – Thực thi tác vụ theo lịch (Schedule Jobs)
- **HttpClient / Refit** – Gọi API từ MDM Service
- **Azure Service Bus / RabbitMQ** – Gửi dữ liệu import sang service khác
- **Docker** – Đóng gói và triển khai
- **Serilog** – Logging

## 🧩 Chức năng chính

| Chức năng | Mô tả |
|-----------|-------|
| 🕒 **Schedule Import** | Dùng **Hangfire** để lên lịch chạy job import nhân viên định kỳ |
| 🔄 **Call API MDM** | Gửi HTTP request đến API của MDM service để lấy danh sách nhân viên |
| 📨 **Gửi kết quả qua SB** | Sau khi xử lý dữ liệu, gửi kết quả import (danh sách thành công/thất bại) sang một **Import Result Service** thông qua Service Bus |
| 📊 **Hangfire Dashboard** | Giao diện web để theo dõi lịch trình, trạng thái và log của các job đã chạy |

## 🔄 Luồng xử lý

1. **Hangfire** khởi động 1 job theo thời gian định sẵn (VD: mỗi ngày 2h sáng)
2. Job gọi đến **MDM Service** qua API `GET /api/employees`
3. Dữ liệu nhân viên được xử lý & kiểm tra hợp lệ
4. Kết quả import được đóng gói (success, fail, lý do thất bại,...)
5. Gửi payload qua **Service Bus** (VD: topic `employee.import.result`)
6. **Import Result Service** sẽ nhận và lưu lại kết quả vào database hoặc hiển thị frontend

