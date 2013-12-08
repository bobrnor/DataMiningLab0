package crawlers.auths;

import crawlers.TwiCrawlerAuthProps;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: pavel
 * Date: 03/12/13
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public class TwiAuth {

    public TwiCrawlerAuthProps auth(String appId, String apiSecret, String permissions) {

        OAuthService service = new ServiceBuilder()
                .provider(TwitterApi.class)
                .apiKey(appId)
                .apiSecret(apiSecret)
                .build();

        Scanner in = new Scanner(System.in);

        System.out.println("=== Twi's OAuth Workflow ===");
        System.out.println();
        System.out.println("Getting the request token...");
        Token requestToken = service.getRequestToken();
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize Scribe here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        Token accessToken = service.getAccessToken(requestToken, verifier);

        TwiCrawlerAuthProps authProps = new TwiCrawlerAuthProps();
        authProps.setAuthService(service);
        authProps.setToken(accessToken);

        return authProps;
    }
}
