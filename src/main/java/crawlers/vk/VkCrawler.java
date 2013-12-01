package crawlers.vk;

import crawlers.CrawlerAuthProps;
import crawlers.CrawlerInterface;
import crawlers.CrawlerStorageProps;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 11/24/13
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public class VkCrawler implements CrawlerInterface {
    static String kApiPath = "https://api.vk.com/method";
    static String kGetDialogsMethodFormat = "%s/messages.getDialogs?access_token=%s&v=%s&offset=%d&count=%d&preview_length=%d";
    static String kGetHistoryMethodFormat = "%s/messages.getHistory?access_token=%s&v=%s&offset=%d&count=%d&user_id=%d";

    CrawlerAuthProps m_authProps;
    CrawlerStorageProps m_storageProps;
    long m_lastRequestTime;
    Thread m_workThread;

    public VkCrawler() {
        m_authProps = null;
        m_storageProps = null;
        m_lastRequestTime = 0;
        m_workThread = null;
    }

    @Override
    public void startCrawling(CrawlerAuthProps autoProps, CrawlerStorageProps storageProps) {
        assert(m_workThread == null);

        m_authProps = autoProps;
        m_storageProps = storageProps;
        m_workThread = new Thread(new VkCrawlerWorkThread());
        m_workThread.start();
    }

    private class VkCrawlerWorkThread implements Runnable {
        public void run() {
            try {
                List<Integer> dialogs = obtainDialogs();
                for (Integer userId : dialogs) {
                    obtainHistory(userId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Integer> obtainDialogs() throws HttpException, IOException, URISyntaxException {
            List<Integer> items = new ArrayList<Integer>();

            VkCrawlerGetDialogsEntity vkEntity = doGetDialogs(0, 200, 1);
            for (VkCrawlerGetDialogsMessage message : vkEntity.response.items) {
                items.add(message.user_id);
            }

            int overall = vkEntity.response.count;
            for (int i = 200; i < overall; i += 200) {
                vkEntity = doGetDialogs(i, 200, 1);
                for (VkCrawlerGetDialogsMessage message : vkEntity.response.items) {
                    items.add(message.user_id);
                }
            }

            return items;
        }

        void obtainHistory(Integer userId) throws HttpException, IOException, URISyntaxException, InterruptedException {

            List<VkCrawlerGetDialogsMessage> messages = new ArrayList<VkCrawlerGetDialogsMessage>();

            VkCrawlerGetHistoryEntity vkEntity = doGetHistory(0, 200, userId);
            messages.addAll(vkEntity.response.items);

            int overall = vkEntity.response.count;
            for (int i = 200; i < overall; i += 200) {
                vkEntity = doGetHistory(i, 200, userId);
                messages.addAll(vkEntity.response.items);
            }
        }

        VkCrawlerGetDialogsEntity doGetDialogs(int offset, int count, int bodyLength) throws URISyntaxException, IOException, HttpException {
            String postString = String.format(
                    kGetDialogsMethodFormat,
                    kApiPath,
                    m_authProps.getAccessToken(),
                    "5.4",
                    offset,
                    count,
                    bodyLength);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postString);
            HttpResponse response = client.execute(post);

            HttpEntity entity = response.getEntity();
            InputStream entityStream = entity.getContent();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(entityStream, VkCrawlerGetDialogsEntity.class);
        }

        VkCrawlerGetHistoryEntity doGetHistory(int offset, int count, int userId) throws URISyntaxException, IOException, HttpException, InterruptedException {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - m_lastRequestTime < 400) {
                Thread.sleep(400 - (currentTime - m_lastRequestTime));
            }
            m_lastRequestTime = Calendar.getInstance().getTimeInMillis();

            String postString = String.format(
                    kGetHistoryMethodFormat,
                    kApiPath,
                    m_authProps.getAccessToken(),
                    "5.4",
                    offset,
                    count,
                    userId);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postString);
            HttpResponse response = client.execute(post);

            HttpEntity entity = response.getEntity();
            InputStream entityStream = entity.getContent();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entityStream, writer);
            System.out.println(writer.toString());

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(writer.toString(), VkCrawlerGetHistoryEntity.class);
        }
    }
}
