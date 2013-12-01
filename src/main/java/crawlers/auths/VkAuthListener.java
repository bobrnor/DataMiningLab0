package crawlers.auths;

import crawlers.CrawlerAuthProps;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/2/13
 * Time: 03:16
 * To change this template use File | Settings | File Templates.
 */
public interface VkAuthListener {
    public void authSucceded(CrawlerAuthProps authProps);
    public void authFiled();
}
