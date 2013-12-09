import crawlers.CrawlerAuthProps;
import crawlers.TwiCrawlerAuthProps;
import crawlers.auths.TwiAuth;
import crawlers.auths.TwiAuthListener;
import crawlers.auths.VkAuth;
import crawlers.auths.VkAuthListener;
import crawlers.twi.TwiCrawler;
import crawlers.vk.VkCrawler;
import javafx.application.Application;
import javafx.stage.Stage;
import lsa.LSANormilizer;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 10/12/13
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class Main extends Application implements VkAuthListener, TwiAuthListener {
    CrawlerAuthProps m_vkAuthProps;

    public static void main(final String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        m_vkAuthProps = null;

        VkAuth vkAuth = new VkAuth(stage, "4016574", "2QWebF0E6PSEtg7GyZD8", "friends,messages", this);
        vkAuth.auth();

        LSANormilizer normilizer = new LSANormilizer();
        normilizer.startNormalizing();
    }

    @Override
    public void authSucceded(CrawlerAuthProps authProps) {
        m_vkAuthProps = authProps;

        VkCrawler vkCrawler = new VkCrawler();
        vkCrawler.startCrawling(m_vkAuthProps, null);

        TwiAuth twiAuth = new TwiAuth(new Stage(), "jFxAP2VXOEokO2eI9k3azQ", "SOhBDSc3DU3OJvGnH8JGOQ83jMZxvkpWy6In7EY48", "", this);
        twiAuth.auth();
    }

    @Override
    public void authFailed() {

    }

    @Override
    public void twiAuthSucceeded(TwiCrawlerAuthProps twiAuthProps) {
        TwiCrawler twiCrawler = new TwiCrawler();
        twiCrawler.startCrawling(twiAuthProps, null);
    }

    @Override
    public void twiAuthFailed() {

    }
}
