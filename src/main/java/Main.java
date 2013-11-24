import crawlers.CrawlerAuthProps;
import crawlers.auths.VkAuth;
import crawlers.vk.VkCrawler;
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
public class Main {
//    private static final SessionFactory ourSessionFactory;
//    private static final ServiceRegistry serviceRegistry;
//
//    static {
//        try {
//            Configuration configuration = new Configuration();
//            configuration.configure();
//
//            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
//        } catch (Throwable ex) {
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
//
//    public static Session getSession() throws HibernateException {
//        return ourSessionFactory.openSession();
//    }

    public static void main(final String[] args) throws Exception {

        VkAuth vkAuth = new VkAuth();
        CrawlerAuthProps authProps = vkAuth.auth("4016574", "2QWebF0E6PSEtg7GyZD8", "friends,messages");

        VkCrawler crawler = new VkCrawler();
        crawler.startCrawling(authProps, null);

        while (true) {
            Thread.sleep(1000);
        }

//        final Session session = getSession();
//        try {
//            System.out.println("querying all the managed entities...");
//            final Map metadataMap = session.getSessionFactory().getAllClassMetadata();
//            for (Object key : metadataMap.keySet()) {
//                final ClassMetadata classMetadata = (ClassMetadata) metadataMap.get(key);
//                final String entityName = classMetadata.getEntityName();
//                final Query query = session.createQuery("from " + entityName);
//                System.out.println("executing: " + query.getQueryString());
//                for (Object o : query.list()) {
//                    System.out.println("  " + o);
//                }
//            }
//        } finally {
//            session.close();
//        }
    }
}
