package singh.saurabh.iscfresnostate.Constants;

/**
 * Created by saurabhsingh on 3/11/17.
 */

public class Konst {
    private static final int MAX_STATUS_LENGTH = 100;
    public static int getMaxStatusLength() {
        return MAX_STATUS_LENGTH;
    }
    private static final String FACEBOOK_GROUP_FEED_ENDPOINT = "/490680074276717/feed";
    public static String getFacebookGroupFeedEndpoint() {
        return FACEBOOK_GROUP_FEED_ENDPOINT;
    }

    private static  final String FACEBOOK_ENDPOINT_DATE_FORMAT_KEY = "date_format";
    public static String getFacebookEndpointDateFormatKey() {
        return FACEBOOK_ENDPOINT_DATE_FORMAT_KEY;
    }

    private static  final String FACEBOOK_ENDPOINT_DATE_FORMAT_VALUE = "U";
    public static String getFacebookEndpointDateFormatValue() {
        return FACEBOOK_ENDPOINT_DATE_FORMAT_VALUE;
    }

    private static  final String FACEBOOK_ENDPOINT_FIELDS_KEY = "fields";
    public static String getFacebookEndpointFieldsKey() {
        return FACEBOOK_ENDPOINT_FIELDS_KEY;
    }

    private static  final String FACEBOOK_ENDPOINT_FIELDS_VALUE = "from{picture,name},message,story,link,full_picture,name,description,type,object_id,created_time,updated_time,is_hidden,subscribed,is_expired";
    public static String getFacebookEndpointFieldsValue() {
        return FACEBOOK_ENDPOINT_FIELDS_VALUE;
    }
}
