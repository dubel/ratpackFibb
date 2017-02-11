import ratpack.exec.Promise;
import ratpack.http.client.HttpClient;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.RatpackServer;

import java.net.InetAddress;
import java.net.URI;
import java.time.Duration;

/**
 * Created by mike on 11.02.17.
 */
public class Fibb {


    public static void main(String[] args) throws Exception{
        System.out.println("test");

        HttpClient httpClient = HttpClient.of(httpClientSpec -> httpClientSpec.readTimeout(Duration.ofMinutes(2)));


        final String uri = "http://localhost:8080/fibo/";

        RatpackServer.start(ratpackServerSpec ->
                        ratpackServerSpec
                                .serverConfig(serverConfigBuilder ->
                                                serverConfigBuilder
                                                    .port(8080)
                                                    .address(InetAddress.getByName("0.0.0.0"))
                                                    .threads(4))
                                .handlers(
                                chain -> chain
                                        .prefix("fibo", fibb ->
                                            fibb.get(":value", ctx -> {
                                                final Long n = Long.parseLong(ctx.getPathTokens().get("value"));
//                                                String val = ctx.getAllPathTokens().get("value");
//                                                ctx.render(fibonacci(n).toString());
                                                if (n < 2){
                                                    ctx.render("1");
                                                } else {
                                                    final Promise<ReceivedResponse> val1 = httpClient.get(URI.create(uri + (n - 1)));
                                                    final Promise<ReceivedResponse> val2 = httpClient.get(URI.create(uri + (n - 2)));

                                                    final Promise<Long> promisedFibb1 = val1.map(bodyResponse ->
                                                            Long.parseLong(bodyResponse.getBody().getText())
                                                    );
                                                    final Promise<Long> promisedFibb2 = val2.map(bodyResponse ->
                                                            Long.parseLong(bodyResponse.getBody().getText())
                                                    );

                                                    promisedFibb1.then(fib1 -> promisedFibb2.then(fib2 -> ctx.render(String.valueOf(fib1 + fib2))));

//                                                    ctx.render("Wyszlo");
                                                }
                                            }))
                                        ));

    }

    public static Long fibonacci(Long number) {
        if (number == 1 || number == 2) {
            return 1L;
        }
        return fibonacci(number - 1) + fibonacci(number - 2);
    }

}
