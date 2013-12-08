package lsa;

import db.DBDialogsEntity;
import db.DBMessagesEntity;
import db.HibernateUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class LSANormilizer {
    private Thread m_workThread;

    public void startNormalizing() {

        if (m_workThread == null || !m_workThread.isAlive()) {
            m_workThread = new Thread(new LSANormalizerWorker());
            m_workThread.start();
        }
    }

    private class LSANormalizerWorker implements Runnable {
        private LSAStemmer m_stemmer = null;

        @Override
        public void run() {
            while (true) {
                stuff();
                try {
                    Thread.sleep(300000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        void stuff() {
            m_stemmer = new LSAStemmer();

            Session session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();

            int updateCount = 0;

            Query query = session.createQuery("from DBDialogsEntity");
            List<DBDialogsEntity> dialogs = query.list();
            for (DBDialogsEntity dialog : dialogs) {
                query = session.createQuery("from DBMessagesEntity where dialog.id = :dialog_id order by date asc").setInteger("dialog_id", dialog.getId());
                List<DBMessagesEntity> messages = query.list();
                for (DBMessagesEntity message : messages) {
                    String clearBody = message.getBody().replaceAll("[\\s]|[^a-zA-Zа-яА-Я ]", " ").trim();
                    List<LSAStem> stems = m_stemmer.stem(clearBody);
                    StringBuilder builder = new StringBuilder();
                    for (LSAStem stem : stems) {
                        if ("S".equals(stem.getPartOfSpeech()) || "SPRO".equals(stem.getPartOfSpeech())) {
                            builder.append(stem.getName()).append(" ");
                        }
                    }
                    message.setFixedBody(builder.toString().trim());
                    session.update(message);
                    if (++updateCount > 100) {
                        updateCount = 0;
                        session.flush();
                        session.clear();
                    }
                }
            }

            session.getTransaction().commit();
            session.close();
        }
    }
}
