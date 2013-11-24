package crawlers.vk;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 11/24/13
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
public class VkCrawlerGetDialogsMessage {
    public Integer id;
    public Integer date;
    public Integer out;
    public Integer user_id;
    public Integer from_id;
    public Integer read_state;
    public String title;
    public String body;
    public Object attachments;
    public Object fwd_messages;
    public Integer emoji;
    public Integer important;
    public Integer deleted;

    public Integer chat_id;
    public ArrayList<Integer> chat_active;
    public Integer users_count;
    public Integer admin_id;
    public String photo_50;
    public String photo_100;
    public String photo_200;
}
