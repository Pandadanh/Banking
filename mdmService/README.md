 # MDM Service - Metadata Ngân Hàng

## Tổng quan
Dự án này cung cấp hệ thống metadata cho ngân hàng, quản lý các thực thể như: Nhân viên, Khách hàng, Chi nhánh, Tuyến đường, Quản lý cửa hàng, Tài khoản, Giao dịch, Vai trò, Địa chỉ, Sản phẩm/Dịch vụ, Hợp đồng, Thẻ, Lịch sử đăng nhập, Thông báo, Hồ sơ tín dụng, Loại khách hàng, Loại nhân viên, Phòng ban, Lương thưởng.

## Cấu trúc thư mục
- `model/`         : Entity (bảng dữ liệu)
- `repository/`    : Repository (ReactiveCrudRepository)
- `dto/`           : Data Transfer Object (DTO, CreateRequest, UpdateRequest)
- `util/`          : Mapper chuyển đổi giữa entity và DTO
- `service/`       : Service xử lý nghiệp vụ CRUD
- `controller/`    : REST API controller

## Danh sách bảng chính
- Employee (Nhân viên)
- Customer (Khách hàng)
- Branch (Chi nhánh)
- StoreRoute (Tuyến đường)
- StoreManager (Quản lý cửa hàng)
- Account (Tài khoản)
- Transaction (Giao dịch)
- Role (Vai trò)
- Address (Địa chỉ)
- Product (Sản phẩm/Dịch vụ)
- Contract (Hợp đồng)
- Card (Thẻ ngân hàng)
- LoginHistory (Lịch sử đăng nhập)
- Notification (Thông báo)
- CreditProfile (Hồ sơ tín dụng)
- CustomerType (Loại khách hàng)
- EmployeeType (Loại nhân viên)
- Department (Phòng ban)
- Payroll (Lương thưởng)

## Hướng dẫn chạy service
1. Cài đặt Java 17+ và Maven 3.8+
2. Chạy lệnh:
   ```bash
   cd mdmService
   ./mvnw spring-boot:run
   ```
3. Service mặc định chạy ở port 8080 (cấu hình trong `src/main/resources/application.yml`)

## Hướng dẫn test API
- Sử dụng Postman hoặc curl để gọi các endpoint REST:
  - GET    `/api/employees`           : Lấy danh sách nhân viên
  - POST   `/api/customers`           : Tạo mới khách hàng
  - PUT    `/api/accounts/{id}`       : Cập nhật tài khoản
  - DELETE `/api/products/{id}`       : Xóa sản phẩm
  - ...
- Các API đều tuân theo chuẩn RESTful, trả về dữ liệu dạng JSON.
- Một số API cần truyền header `X-User-Id` để xác định người thao tác.

## Ghi chú
- Dự án sử dụng Spring WebFlux (Reactive), các repository/service/controller đều reactive (Mono/Flux).
- Có thể mở rộng thêm các bảng hoặc trường tùy ý theo nghiệp vụ ngân hàng.
- Nếu cần migration DB, hãy bổ sung file SQL vào thư mục `init-scripts/`.

---
Mọi thắc mắc hoặc cần hỗ trợ, liên hệ team phát triển!
