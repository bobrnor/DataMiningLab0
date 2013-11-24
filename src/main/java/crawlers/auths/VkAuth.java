package crawlers.auths;

import crawlers.CrawlerAuthProps;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.VkontakteApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 11/24/13
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public class VkAuth {

    public CrawlerAuthProps auth(String appId, String apiSecret, String permissions) {

        OAuthService service = new ServiceBuilder()
                .provider(VkontakteApi.class)
                .apiKey(appId)
                .apiSecret(apiSecret)
                .scope(permissions)
                .callback("https://oauth.vk.com/blank.html")
                .build();

        Scanner in = new Scanner(System.in);

        System.out.println("=== Vk's OAuth Workflow ===");
        System.out.println();

        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(null);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize Scribe here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        Token accessToken = service.getAccessToken(null, verifier);

        CrawlerAuthProps authProps = new CrawlerAuthProps();
        authProps.setAccessToken(accessToken.getToken());

        return authProps;
    }
}
