package singh.saurabh.iscfresnostate.NewsFeedParser;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.text.Html;
import android.text.Spanned;
import android.util.Xml;

import java.util.ArrayList;
import java.util.List;

public class AndroidSaxFeedParser extends BaseFeedParser {

	static final String RSS = "rss";
	public AndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<Message> parse() {
		final Message currentMessage = new Message();
		RootElement root = new RootElement(RSS);
		final List<Message> messages = new ArrayList<Message>();
		Element channel = root.getChild(CHANNEL);
		Element item = channel.getChild(ITEM);

		item.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});

		item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});

		item.getChild(LINK).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setLink(body);
			}
		});

		item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				Spanned result;
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					result = Html.fromHtml(body,Html.FROM_HTML_MODE_LEGACY);
				} else {
					result = Html.fromHtml(body);
				}
				currentMessage.setDescription(result.toString());
			}
		});

		item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDate(body);
			}
		});

		item.getChild(AUTHOR).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setAuthor(body);
			}
		});

		item.getChild(CONTENT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setContent(body);
			}
		});

		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
}
