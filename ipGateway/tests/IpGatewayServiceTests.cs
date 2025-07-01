using System;
using System.Threading.Tasks;
using Xunit;
using IpGatewayProject.Services;

namespace IpGatewayProject.Tests
{
    public class IpGatewayServiceTests
    {
        private readonly IpGatewayService _service;

        public IpGatewayServiceTests()
        {
            _service = new IpGatewayService();
        }

        [Fact]
        public async Task GetGatewayInfo_ShouldReturnValidInfo()
        {
            // Arrange

            // Act
            var result = await _service.GetGatewayInfo();

            // Assert
            Assert.NotNull(result);
            // Additional assertions can be added based on expected result
        }

        [Fact]
        public async Task SendRequest_ShouldReturnSuccess()
        {
            // Arrange
            var request = new IpGatewayRequest
            {
                IpAddress = "192.168.1.1",
                RequestData = "Test data"
            };

            // Act
            var result = await _service.SendRequest(request);

            // Assert
            Assert.True(result.IsSuccess);
            // Additional assertions can be added based on expected result
        }
    }
}