package db;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class HibernateUtils {
    private static final SessionFactory m_sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            m_sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return m_sessionFactory;
    }

    public static DBUsersEntity getUserByIID(long iid, int sourceType) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("from DBUsersEntity where iid = :iid and sourceType = :sourceType")
                .setLong("iid", iid)
                .setInteger("sourceType", sourceType);
        DBUsersEntity usersEntity = (DBUsersEntity)query.uniqueResult();
        if (usersEntity == null) {
            usersEntity = new DBUsersEntity();
            usersEntity.setIid(iid);
            usersEntity.setSourceType(sourceType);
            session.save(usersEntity);
        }

        session.getTransaction().commit();
        session.close();

        return usersEntity;
    }
}
