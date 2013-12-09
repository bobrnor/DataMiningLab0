package crawlers.twi;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwiCrawlerGetTimelineTweet {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contributor {
        public Long id;
        public String id_str;
        public String screen_name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coordinates {
        public ArrayList<Double> coordinates;

        @JsonProperty("type")
        public String type_name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TweetID {
        public Long id;
        public String id_str;
    }

    public ArrayList<Contributor> contributors;
    public Coordinates coordinates;
    public String created_at;
    public TweetID current_user_retweet;
    public Integer favorite_count;
    public Boolean favorited;
    public String filter_level;
    public Long id;
    public String id_str;
    public String in_reply_to_screen_name;
    public Long in_reply_to_status_id;
    public String in_reply_to_status_id_str;
    public Long in_reply_to_user_id;
    public String in_reply_to_user_id_str;
    public String lang;
    public Boolean possibly_sensitive;
    public Integer retweet_count;
    public Boolean retweeted;
    public TwiCrawlerGetTimelineTweet retweeted_status;
    public String source;
    public String text;
    public Boolean truncated;
    public TwiCrawlerUserEntity user;
    public Boolean withheld_copyright;
    public ArrayList<String> withheld_in_countries;
    public String withheld_scope;
}
