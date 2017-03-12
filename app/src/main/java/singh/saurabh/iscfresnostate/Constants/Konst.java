package singh.saurabh.iscfresnostate.Constants;

/**
 * Created by saurabhsingh on 3/11/17.
 */

public class Konst {
    private static final String FACEBOOK_GROUP_FEED_ENDPOINT = "/490680074276717/feed";
    public static String getFacebookGroupFeedEndpoint() {
        return FACEBOOK_GROUP_FEED_ENDPOINT;
    }

    private static  final String FACEBOOK_ENDPOINT_FIELDS_KEY = "fields";
    public static String getFacebookEndpointFieldsKey() {
        return FACEBOOK_ENDPOINT_FIELDS_KEY;
    }

    private static  final String FACEBOOK_ENDPOINT_FIELDS_VALUE = "from,message,story,link,picture,name,description,type,object_id,created_time,updated_time,is_hidden,subscribed,is_expired";
    public static String getFacebookEndpointFieldsValue() {
        return FACEBOOK_ENDPOINT_FIELDS_VALUE;
    }
}
