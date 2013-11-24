package crawlers.vk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 11/24/13
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
public class VkCrawlerGetDialogsEntity {
    public static class VkCrawlerGetDialogsResponse {
        public Integer count;
        public ArrayList<VkCrawlerGetDialogsMessage> items;
    }

    public VkCrawlerGetDialogsResponse response;
}
