package crawlers.auths;

import crawlers.CrawlerAuthProps;
import javafx.stage.Stage;
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
public class VkAuth implements OAuthWebHelperListener {
    private Stage m_stage;
    private OAuthService m_service;
    private VkAuthListener m_listener;

    public VkAuth(Stage stage, String appId, String apiSecret, String permissions, VkAuthListener listener) {
        m_stage = stage;
        m_service = new ServiceBuilder()
                .provider(VkontakteApi.class)
                .apiKey(appId)
                .apiSecret(apiSecret)
                .scope(permissions)
                .callback("https://oauth.vk.com/blank.html")
                .build();
        m_listener = listener;
    }

    public void auth() {
        String authorizationUrl = m_service.getAuthorizationUrl(null);

        OAuthWebHelper oauthHelper = new OAuthWebHelper(m_stage, this);
        oauthHelper.auth("Vk OAuth", authorizationUrl);
    }

    @Override
    public void authCodeReceived(String code) {
        Verifier verifier = new Verifier(code);
        Token accessToken = m_service.getAccessToken(null, verifier);

        CrawlerAuthProps authProps = new CrawlerAuthProps();
        authProps.setAccessToken(accessToken.getToken());
        m_listener.authSucceded(authProps);
        m_stage.close();
    }
}
