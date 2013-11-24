package crawlers.vk;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 11/24/13
 * Time: 20:47
 * To change this template use File | Settings | File Templates.
 */
public class VkCrawlerGetHistoryEntity {
    public static class VkCrawlerGetHistoryResponse {
        public Integer count;
        public ArrayList<VkCrawlerGetDialogsMessage> items;
    }

    public VkCrawlerGetHistoryResponse response;
}
