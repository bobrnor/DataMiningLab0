import crawlers.CrawlerAuthProps;
import crawlers.TwiCrawlerAuthProps;
import crawlers.auths.TwiAuth;
import crawlers.auths.TwiAuthListener;
import crawlers.auths.VkAuth;
import crawlers.auths.VkAuthListener;
import crawlers.twi.TwiCrawler;
import crawlers.vk.VkCrawler;
import db.HibernateUtils;
import javafx.application.Application;
import javafx.stage.Stage;
import lsa.LSANormilizer;
import lsa.LSAStemmer;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.Map;

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

    //CrawlerAuthProps authProps = twiAuth.auth("jFxAP2VXOEokO2eI9k3azQ", "SOhBDSc3DU3OJvGnH8JGOQ83jMZxvkpWy6In7EY48", "");

    @Override
    public void start(Stage stage) throws Exception {
        m_vkAuthProps = null;

//        VkAuth vkAuth = new VkAuth(stage, "4016574", "2QWebF0E6PSEtg7GyZD8", "friends,messages", this);
//        vkAuth.auth();

//        LSANormilizer normilizer = new LSANormilizer();
//        normilizer.startNormalizing();

        TwiAuth twiAuth = new TwiAuth(new Stage(), "jFxAP2VXOEokO2eI9k3azQ", "SOhBDSc3DU3OJvGnH8JGOQ83jMZxvkpWy6In7EY48", "", this);
        twiAuth.auth();
    }

    @Override
    public void authSucceded(CrawlerAuthProps authProps) {
        m_vkAuthProps = authProps;

//        TwiAuth twiAuth = new TwiAuth(new Stage(), "jFxAP2VXOEokO2eI9k3azQ", "SOhBDSc3DU3OJvGnH8JGOQ83jMZxvkpWy6In7EY48", "", this);
//        twiAuth.auth();
    }

    @Override
    public void authFailed() {

    }

    @Override
    public void twiAuthSucceeded(TwiCrawlerAuthProps twiAuthProps) {
//        VkCrawler vkCrawler = new VkCrawler();
//        vkCrawler.startCrawling(m_vkAuthProps, null);

        TwiCrawler twiCrawler = new TwiCrawler();
        twiCrawler.startCrawling(twiAuthProps, null);
    }

    @Override
    public void twiAuthFailed() {

    }
}
