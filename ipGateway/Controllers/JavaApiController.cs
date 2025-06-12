using Microsoft.AspNetCore.Mvc;
using IpGatewayProject.Services;
using System.Threading.Tasks;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Text;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;

namespace IpGatewayProject.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class JavaApiController : ControllerBase
    {
        private readonly JavaApiService _javaApiService;
        private readonly string _jwtSecret;

        public JavaApiController(JavaApiService javaApiService, IConfiguration configuration)
        {
            _javaApiService = javaApiService;
            _jwtSecret = configuration["JwtSecret"]; // Get from appsettings.json or environment
        }

        [HttpGet("test")]
        public async Task<IActionResult> TestJavaApi()
        {
            if (!Request.Headers.ContainsKey("Authorization"))
                return Unauthorized("Missing Authorization header.");

            var token = Request.Headers["Authorization"].ToString().Replace("Bearer ", "");

            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(_jwtSecret);
            try
            {
                tokenHandler.ValidateToken(token, new TokenValidationParameters
                {
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(key),
                    ValidateIssuer = false,
                    ValidateAudience = false,
                }, out SecurityToken validatedToken);
            }
            catch
            {
                return Unauthorized("Invalid or expired JWT token.");
            }

            try
            {
                string javaApiUrl = "http://localhost:8761/eureka";
                var result = await _javaApiService.GetJavaApiDataAsync(javaApiUrl);
                return Ok(result);
            }
            catch (System.Exception ex)
            {
                return StatusCode(StatusCodes.Status502BadGateway, $"Error calling Java API: {ex.Message}");
            }
        }
    }
}