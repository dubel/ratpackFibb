import ratpack.server.RatpackServer;

/**
 * Created by mike on 11.02.17.
 */
public class Fibb {


    public static void main(String[] args) throws Exception{
        System.out.println("test");

        RatpackServer.start(ratpackServerSpec ->
                        ratpackServerSpec.handlers( chain -> chain.prefix("fibo", fibb ->
                                            fibb.all(ctx -> ctx.render("jest ok")))));
        
    }
}
