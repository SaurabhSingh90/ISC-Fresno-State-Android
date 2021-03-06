package singh.saurabh.iscfresnostate.NewsFeedParser;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MessageList {
	
	public List<Message> messages;

    public ArrayList<HashMap<String, String>> startFunction() {
        return loadFeed(ParserType.ANDROID_SAX);
    }


	private ArrayList<HashMap<String, String>> loadFeed(ParserType type){
    	try{
	    	FeedParser parser = FeedParserFactory.getParser(type);

			assert parser != null;
			messages = parser.parse();

	    	ArrayList<HashMap<String, String>> titles =
                    new ArrayList<HashMap<String, String>>(messages.size());

            String posted_on;

	    	for (Message msg : messages){

				HashMap<String, String> newsPost = new HashMap<String, String>();
	    		newsPost.put("title", msg.getTitle());
                posted_on = msg.getDate();
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzzz", Locale.US);
                sdf1.getTimeZone();
                Date d1 = null;
                try {
                    d1 = sdf1.parse(posted_on);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.getDefault());
				posted_on = sdf.format(d1);

                newsPost.put("createdAt", posted_on);
				newsPost.put("description", msg.getDescription());
                titles.add(newsPost);
	    	}
            return titles;

    	} catch (Throwable t){
    		Log.e("AndroidNews", t.getMessage(), t);
    	}
        return null;
    }
    
//	private String writeXml(){
//		XmlSerializer serializer = Xml.newSerializer();
//		StringWriter writer = new StringWriter();
//		try {
//			serializer.setOutput(writer);
//			serializer.startDocument("UTF-8", true);
//			serializer.startTag("", "messages");
//			serializer.attribute("", "number", String.valueOf(messages.size()));
//			for (Message msg: messages){
//				serializer.startTag("", "message");
//				serializer.attribute("", "date", msg.getDate());
//				serializer.startTag("", "title");
//				serializer.text(msg.getTitle());
//				serializer.endTag("", "title");
//				serializer.startTag("", "url");
//				serializer.text(msg.getLink().toExternalForm());
//				serializer.endTag("", "url");
//
////				serializer.startTag("", "author");
////				serializer.text(msg.getAuthor());
////				serializer.endTag("", "author");
//				serializer.startTag("", "body");
//				serializer.text(msg.getDescription());
//				serializer.endTag("", "body");
//				serializer.endTag("", "message");
//			}
//			serializer.endTag("", "messages");
//			serializer.endDocument();
//			return writer.toString();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
}