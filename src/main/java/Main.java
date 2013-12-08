import crawlers.CrawlerAuthProps;
import crawlers.auths.TwiAuth;
import crawlers.auths.VkAuth;
import crawlers.auths.VkAuthListener;
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
public class Main extends Application implements VkAuthListener {
    public static void main(final String[] args) throws Exception {
        launch(args);
    }

    //CrawlerAuthProps authProps = twiAuth.auth("jFxAP2VXOEokO2eI9k3azQ", "SOhBDSc3DU3OJvGnH8JGOQ83jMZxvkpWy6In7EY48", "");

    @Override
    public void start(Stage stage) throws Exception {
        VkAuth vkAuth = new VkAuth(stage, "4016574", "2QWebF0E6PSEtg7GyZD8", "friends,messages", this);
        vkAuth.auth();

        LSANormilizer normilizer = new LSANormilizer();
        normilizer.startNormalizing();
    }

    @Override
    public void authSucceded(CrawlerAuthProps authProps) {
        VkCrawler crawler = new VkCrawler();
        crawler.startCrawling(authProps, null);
    }

    @Override
    public void authFiled() {

    }
}
