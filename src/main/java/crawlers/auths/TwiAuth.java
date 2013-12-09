package crawlers.auths;

import crawlers.TwiCrawlerAuthProps;
import javafx.stage.Stage;
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
public class TwiAuth implements OAuthWebHelperListener {
    private Stage m_stage;
    private OAuthService m_service;
    private TwiAuthListener m_listener;
    private Token m_requestToken;

    public TwiAuth(Stage stage, String appId, String apiSecret, String permissions, TwiAuthListener listener) {
        m_stage = stage;
        m_service = new ServiceBuilder()
                .provider(TwitterApi.SSL.class)
                .apiKey(appId)
                .apiSecret(apiSecret)
                .build();
        m_listener = listener;
        m_requestToken = null;
    }

    public void auth() {
        m_requestToken = m_service.getRequestToken();
        String authorizationUrl = m_service.getAuthorizationUrl(m_requestToken);

        TwiOAuthWebHelper oauthHelper = new TwiOAuthWebHelper(m_stage, this);
        oauthHelper.auth("Twi Pin OAuth", authorizationUrl);
    }

    @Override
    public void authCodeReceived(String code) {
        Verifier verifier = new Verifier(code);
        Token accessToken = m_service.getAccessToken(m_requestToken, verifier);

        TwiCrawlerAuthProps authProps = new TwiCrawlerAuthProps();
        authProps.setAuthService(m_service);
        authProps.setToken(accessToken);
        m_listener.twiAuthSucceeded(authProps);
        m_stage.close();
    }
}
