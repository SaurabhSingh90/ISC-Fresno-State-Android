package singh.saurabh.iscfresnostate.model;

/**
 * Class to get and set single post details
 */
public class Post {
    private String mFirstName;
    private String mLastName;
    protected String mPostId;
    private String mTitle;
    private String mPostContent;
    private String mDate;
    private String mNumberOfReplies;
    private String mPostChannel;
    private String mPostTags;

    public String getPostTags() {
        return mPostTags;
    }

    public void setPostTags(String postTags) {
        mPostTags = postTags;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPostContent() {
        return mPostContent;
    }

    public void setPostContent(String postContent) {
        mPostContent = postContent;
    }

    public String getNumberOfReplies() {
        return mNumberOfReplies;
    }

    public void setNumberOfReplies(String numberOfReplies) {
        mNumberOfReplies = numberOfReplies;
    }

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
    }

    public String getPostChannel() {
        return mPostChannel;
    }

    public void setPostChannel(String postChannel) {
        mPostChannel = postChannel;
    }
}
