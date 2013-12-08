package crawlers.twi;

import crawlers.CrawlerAuthProps;
import crawlers.CrawlerInterface;
import crawlers.CrawlerStorageProps;
import crawlers.TwiCrawlerAuthProps;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

public class TwiCrawler implements CrawlerInterface {
    static String kAccountSettingsPath = "https://api.twitter.com/1.1/account/settings.json";
    static String kUserShowPath = "https://api.twitter.com/1.1/users/show.json?screen_name=%s&include_entities=false";
    static String kStatusTimelinePath = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=%s&count=%s&trim_user=true&include_rts=true";
    static String kStatusMentionsTimelinePath = "https://api.twitter.com/1.1/statuses/mentions_timeline.json?count=%s&trim_user=true&contributor_details=false&include_entities=false";
    static String kStatusShowPath = "https://api.twitter.com/1.1/statuses/show.json?id=%s&trim_user=true&include_my_retweet=false&include_entities=false";

    TwiCrawlerAuthProps m_authProps;
    CrawlerStorageProps m_storageProps;
    long m_lastRequestTime;
    Thread m_workThread;

    public TwiCrawler() {
        m_authProps = null;
        m_storageProps = null;
        m_lastRequestTime = 0;
        m_workThread = null;
    }

    @Override
    public void startCrawling(CrawlerAuthProps authProps, CrawlerStorageProps storageProps) {
        assert(m_workThread == null);

        m_authProps = (TwiCrawlerAuthProps)authProps;
        m_storageProps = storageProps;
        m_workThread = new Thread(new TwiCrawlerWorkThread());
        m_workThread.start();
    }

    private class TwiCrawlerWorkThread implements Runnable {
        public void run() {
            try {
                TwiCrawlerAccountSettingsEntity settings = doGetAccountSettings();
                String username = settings.screen_name;
                if (username.isEmpty()) {
                    System.out.println("there's something wrong, pal.");
                } else {
                    TwiCrawlerUserEntity user = doGetUserInfo(username);

                    TreeSet<Long> tweet_ids = new TreeSet<Long>();
                    TreeSet<Long> parent_ids = new TreeSet<Long>();

                    System.out.print("Fetching home timeline tweets... ");
                    ArrayList<TwiCrawlerGetTimelineTweet> homeTweets = getHomeTimelineForUser(user);
                    for (TwiCrawlerGetTimelineTweet tweet : homeTweets) {
                        tweet_ids.add(tweet.id);
                        if (tweet.in_reply_to_status_id != null) {
                            parent_ids.add(tweet.in_reply_to_status_id);
                        }
                    }
                    System.out.println("Done.");

                    System.out.print("Fetching mentions... ");
                    ArrayList<TwiCrawlerGetTimelineTweet> mentions = getMentionsTimelineForAuthenticatedUser();
                    for (TwiCrawlerGetTimelineTweet tweet : mentions) {
                        tweet_ids.add(tweet.id);
                        if (tweet.in_reply_to_status_id != null) {
                            parent_ids.add(tweet.in_reply_to_status_id);
                        }
                    }
                    System.out.println("Done.");

                    ArrayList<TwiCrawlerGetTimelineTweet> relatedTweets = new ArrayList<TwiCrawlerGetTimelineTweet>();
                    while (parent_ids.size() > 0) {
                        parent_ids.removeAll(tweet_ids);
                        System.out.println("We need more tweets: " + Integer.toString(parent_ids.size()) + "... ");

                        ArrayList<TwiCrawlerGetTimelineTweet> tweets = getTweets(parent_ids);

                        parent_ids.clear();
                        for (TwiCrawlerGetTimelineTweet tweet : tweets) {
                            tweet_ids.add(tweet.id);
                            if (tweet.in_reply_to_status_id != null) {
                                parent_ids.add(tweet.in_reply_to_status_id);
                            }
                        }
                        relatedTweets.addAll(tweets);
                        System.out.println("Done.");
                    }

                    System.out.println("Home tweets count: " + Integer.toString(homeTweets.size()));
                    System.out.println("Mentions count: " + Integer.toString(mentions.size()));
                    System.out.println("Related tweets count: " + Integer.toString(relatedTweets.size()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayList<TwiCrawlerGetTimelineTweet> getHomeTimelineForUser(TwiCrawlerUserEntity user) throws OAuthConnectionException, IOException, InterruptedException {
            ArrayList<TwiCrawlerGetTimelineTweet> homeTweets = new ArrayList<TwiCrawlerGetTimelineTweet>();

            String username = user.screen_name;
            int tweetsCount = user.statuses_count;
            if (tweetsCount > 0) {
                int tweetsToProcess = (tweetsCount < 200 ? tweetsCount : (tweetsCount > 3200 ? 3200 : tweetsCount));
                long max_id = -1;
                do {
                    int count = (tweetsToProcess > 200 ? 200 : tweetsToProcess);
                    ArrayList<TwiCrawlerGetTimelineTweet> recentTweets = doGetTimeline(username, count, max_id);

                    homeTweets.addAll(recentTweets);

                    tweetsToProcess -= count;
                    max_id = recentTweets.get(recentTweets.size() - 1).id - 1;
                } while (tweetsToProcess > 0);
            }

            return homeTweets;
        }

        ArrayList<TwiCrawlerGetTimelineTweet> getMentionsTimelineForAuthenticatedUser() throws OAuthConnectionException, IOException, InterruptedException {
            ArrayList<TwiCrawlerGetTimelineTweet> mentions = new ArrayList<TwiCrawlerGetTimelineTweet>();

            int maxTweetsRemaining = 800;
            int lastReturnedMentionsCount = 200;

            long max_id = -1;
            do {
                int count = 200;
                ArrayList<TwiCrawlerGetTimelineTweet> recentTweets = doGetMentionsTimeline(count, max_id);

                mentions.addAll(recentTweets);

                lastReturnedMentionsCount = recentTweets.size();
                maxTweetsRemaining -= lastReturnedMentionsCount;
                max_id = recentTweets.get(recentTweets.size() - 1).id - 1;
            } while (maxTweetsRemaining > 0 && lastReturnedMentionsCount == 200);

            return mentions;
        }

        ArrayList<TwiCrawlerGetTimelineTweet> getTweets(TreeSet<Long> ids) throws OAuthConnectionException, IOException, InterruptedException {
            ArrayList<TwiCrawlerGetTimelineTweet> tweets = new ArrayList<TwiCrawlerGetTimelineTweet>();

            for (Long id : ids) {
                String getPath = String.format(
                        kStatusShowPath,
                        Long.toString(id)
                );

                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - m_lastRequestTime < 400) {
                    Thread.sleep(400 - (currentTime - m_lastRequestTime));
                }
                m_lastRequestTime = Calendar.getInstance().getTimeInMillis();

                OAuthRequest request = new OAuthRequest(Verb.GET, getPath);
                m_authProps.getAuthService().signRequest(m_authProps.getToken(), request);
                Response response = request.send();

                InputStream entityStream = response.getStream();

                ObjectMapper mapper = new ObjectMapper();
                TwiCrawlerGetTimelineTweet tweet = mapper.readValue(entityStream, TwiCrawlerGetTimelineTweet.class);

                int requestsRemaining = Integer.parseInt(response.getHeader("x-rate-limit-remaining"));
                if (requestsRemaining == 0) {
                    long resetTime = Long.parseLong(response.getHeader("x-rate-limit-reset"));
                    System.out.println("Frame rate reset time is " + Long.toString(resetTime));
                    currentTime = (long)(System.currentTimeMillis() / 1000L);
                    System.out.println("Current time is " + Long.toString(currentTime));
                    System.out.println("Gonna sleep " + Long.toString(resetTime - currentTime + 2) + " seconds.");
                    Thread.sleep((resetTime - currentTime + 2) * 1000L);
                    System.out.println("Woke up!");
                }
            }

            return tweets;
        }

        TwiCrawlerAccountSettingsEntity doGetAccountSettings() throws OAuthConnectionException, IOException {
            OAuthRequest request = new OAuthRequest(Verb.GET, kAccountSettingsPath);
            m_authProps.getAuthService().signRequest(m_authProps.getToken(), request);
            Response response = request.send();
//            for (String key : response.getHeaders().keySet()) {
//                String val = response.getHeaders().get(key);
//                System.out.println(key + ": " + val);
//            }

            InputStream entityStream = response.getStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(entityStream, TwiCrawlerAccountSettingsEntity.class);
        }

        TwiCrawlerUserEntity doGetUserInfo(String username) throws OAuthConnectionException, IOException {
            String getPath = String.format(
                    kUserShowPath,
                    username
            );

            OAuthRequest request = new OAuthRequest(Verb.GET, getPath);
            m_authProps.getAuthService().signRequest(m_authProps.getToken(), request);
            Response response = request.send();

            InputStream entityStream = response.getStream();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(entityStream, TwiCrawlerUserEntity.class);
        }

        ArrayList<TwiCrawlerGetTimelineTweet> doGetTimeline(String username, int tweetsCount, long max_id) throws OAuthConnectionException, IOException, InterruptedException {


            String getPath = String.format(
                    kStatusTimelinePath,
                    username,
                    Integer.toString(tweetsCount)
            );
            if (max_id > -1) {
                getPath += "&max_id=" + Long.toString(max_id);
            }

            return doGetTweets(getPath);
        }

        ArrayList<TwiCrawlerGetTimelineTweet> doGetMentionsTimeline(int tweetsCount, long max_id) throws OAuthConnectionException, IOException, InterruptedException {
            String getPath = String.format(
                    kStatusMentionsTimelinePath,
                    Integer.toString(tweetsCount)
            );
            if (max_id > -1) {
                getPath += "&max_id=" + Long.toString(max_id);
            }

            return doGetTweets(getPath);
        }

        ArrayList<TwiCrawlerGetTimelineTweet> doGetTweets(String getPath) throws OAuthConnectionException, IOException, InterruptedException {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - m_lastRequestTime < 400) {
                Thread.sleep(400 - (currentTime - m_lastRequestTime));
            }
            m_lastRequestTime = Calendar.getInstance().getTimeInMillis();

            OAuthRequest request = new OAuthRequest(Verb.GET, getPath);
            m_authProps.getAuthService().signRequest(m_authProps.getToken(), request);
            Response response = request.send();

            InputStream entityStream = response.getStream();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entityStream, writer);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(writer.toString(), new TypeReference<ArrayList<TwiCrawlerGetTimelineTweet>>(){});
        }

        TwiCrawlerGetTimelineTweet doGetTweet(Long id) throws OAuthConnectionException, IOException, InterruptedException {
            String getPath = String.format(
                    kStatusShowPath,
                    Long.toString(id)
            );

            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - m_lastRequestTime < 400) {
                Thread.sleep(400 - (currentTime - m_lastRequestTime));
            }
            m_lastRequestTime = Calendar.getInstance().getTimeInMillis();

            OAuthRequest request = new OAuthRequest(Verb.GET, getPath);
            m_authProps.getAuthService().signRequest(m_authProps.getToken(), request);
            Response response = request.send();

            InputStream entityStream = response.getStream();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entityStream, writer);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(writer.toString(), TwiCrawlerGetTimelineTweet.class);
        }
    }
}
