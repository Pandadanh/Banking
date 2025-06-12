using System.Net.Http;
using System.Threading.Tasks;

namespace IpGatewayProject.Services
{
    public class JavaApiService
    {
        private readonly HttpClient _httpClient;

        public JavaApiService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<string> GetJavaApiDataAsync(string javaApiUrl)
        {
            var response = await _httpClient.GetAsync(javaApiUrl);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadAsStringAsync();
        }
    }
}