package crawlers;

import crawlers.CrawlerAuthProps;
import org.scribe.oauth.OAuthService;
import org.scribe.model.Token;

/**
 * Created by pavel on 08/12/13.
 */
public class TwiCrawlerAuthProps extends CrawlerAuthProps {
    OAuthService m_authService;
    Token m_token;

    public OAuthService getAuthService() {

        return m_authService;
    }

    public void setAuthService(OAuthService authService) {
        m_authService = authService;
    }

    public Token getToken() {

        return m_token;
    }

    public void setToken(Token accessToken) {

        m_token = accessToken;
    }
}
