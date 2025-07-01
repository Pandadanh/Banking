using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

public class Program
{
    public static void Main(string[] args)
    {
        CreateHostBuilder(args).Build().Run();
    }

    public static IHostBuilder CreateHostBuilder(string[] args) =>
        Host.CreateDefaultBuilder(args)
            .ConfigureWebHostDefaults(webBuilder =>
            {
                webBuilder.UseStartup<Startup>();
            });
}

// In your project, ensure you have a Startup class like this:
public class Startup
{
    // Dummy implementation of JavaApiService to resolve the missing type error
    public class JavaApiService
    {
        // Add your service logic here
    }

    public void ConfigureServices(IServiceCollection services)
    {
        services.AddHttpClient<JavaApiService>();
        // other service registrations
    }

    public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
    {
        // middleware configuration
    }
}