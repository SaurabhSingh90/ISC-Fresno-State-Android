package singh.saurabh.iscfresnostate;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by saurabhsingh on 3/11/17.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class FeedModel {

    @JsonField(name = "data")
    private List<FeedItem> feedItems;

    public List<FeedItem> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(List<FeedItem> items) {
        this.feedItems = items;
    }

    @JsonObject
    public static class FeedItem {

        @JsonField(name = "from")
        private FromUser fromUser;
        public FromUser getFromUser() {
            return fromUser;
        }
        public void setFromUser(FromUser fromUser) {
            this.fromUser = fromUser;
        }

        @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
        public static class FromUser {
            @JsonField
            private String name;
            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }
        }

        @JsonField
        private String message;
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @JsonField
        private String story;
        public String getStory() {
            return story;
        }

        public void setStory(String story) {
            this.story = story;
        }

        @JsonField
        private String link;
        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        @JsonField(name = "picture")
        private String pictureUrl;
        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        @JsonField(name = "name")
        private String postName;
        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        @JsonField
        private String type;
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @JsonField
        private String object_id;
        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        @JsonField(name = "created_time")
        private String createdTime;
        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        @JsonField(name = "updated_time")
        private String updatedTime;
        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        @JsonField(name = "is_hidden")
        private boolean isHidden;
        public boolean isHidden() {
            return isHidden;
        }

        public void setIsHidden(boolean isHidden) {
            this.isHidden = isHidden;
        }

        @JsonField
        private boolean subscribed;
        public boolean isSubscribed() {
            return subscribed;
        }

        public void setSubscribed(boolean subscribed) {
            this.subscribed = subscribed;
        }

        @JsonField(name = "is_expired")
        private boolean isExpired;
        public boolean getIsExpired() {
            return isExpired;
        }

        public void setIsExpired(boolean isExpired) {
            this.isExpired = isExpired;
        }

        @JsonField
        private String id;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
