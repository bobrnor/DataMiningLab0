package crawlers.auths;

import crawlers.TwiCrawlerAuthProps;

public interface TwiAuthListener {
    public void twiAuthSucceeded(TwiCrawlerAuthProps authProps);
    public void twiAuthFailed();
}
