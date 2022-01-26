using System.Threading.Tasks;

using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using Microsoft.Extensions.DependencyInjection;

using MudBlazor.Services;

namespace ui
{
    public class Program
    {
        public static async Task Main(string[] args)
        {
            var builder = WebAssemblyHostBuilder.CreateDefault(args);
            
            builder.Services.AddHttpClient();
            builder.RootComponents.Add<App>("#app");
            
            builder.Services.AddMudServices();

            await builder.Build().RunAsync();
        }
    }
}
