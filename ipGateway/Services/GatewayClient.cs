using System.Net.Http;
using System.Threading.Tasks;

namespace IpGatewayProject.Services
{
    public class GatewayClient
    {
        private readonly HttpClient _httpClient;

        public GatewayClient(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<string> GetPublicKeyAsync(string gatewayUrl)
        {
            // Example: GET http://java-gateway-url/api/public-key
            var response = await _httpClient.GetAsync(gatewayUrl);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadAsStringAsync();
        }
    }
}