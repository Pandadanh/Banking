using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using IpGatewayProject.Services;
using IpGatewayProject.Models;

namespace IpGatewayProject.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class IpGatewayController : ControllerBase
    {
        private readonly IpGatewayService _ipGatewayService;

        public IpGatewayController(IpGatewayService ipGatewayService)
        {
            _ipGatewayService = ipGatewayService;
        }

        [HttpGet("info")]
        public async Task<IActionResult> GetGatewayInfo()
        {
            var gatewayInfo = await _ipGatewayService.GetGatewayInfo();
            return Ok(gatewayInfo);
        }

        [HttpPost("request")]
        public async Task<IActionResult> PostRequest([FromBody] IpGatewayRequest request)
        {
            if (request == null)
            {
                return BadRequest("Invalid request");
            }

            var response = await _ipGatewayService.SendRequest(request);
            return Ok(response);
        }
    }
}