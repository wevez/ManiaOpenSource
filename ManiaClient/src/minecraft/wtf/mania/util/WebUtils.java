package wtf.mania.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import wtf.mania.util.WebUtils.HtmlParserCallback;

public class WebUtils {
	
	public static String agent1 = "User-Agent";
    public static String agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
	
    public static List<String> visitSiteThreaded(final String urly){
    	ArrayList<String> lines = new ArrayList<String>();
		URL url;
        try {
            String line;
            url = new URL(urly);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(agent1, agent2);
            final InputStreamReader reader = new InputStreamReader(connection.getInputStream(), "Shift_JIS");
            BufferedReader in = new BufferedReader(reader);
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
        }
		return lines;
	}
    
    public static InputStreamReader getReader(final String urly){
    	ArrayList<String> lines = new ArrayList<String>();
		URL url;
        try {
            url = new URL(urly);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(agent1, agent2);
            return new InputStreamReader(connection.getInputStream(), "Shift_JIS");
        } catch (Exception e) {
        }
		return null;
	}
    
    public static List<String> getFormatted(InputStreamReader reader) {
    	ParserDelegator parserDelegator = new ParserDelegator();
        HtmlParserCallback callback = new HtmlParserCallback();
        String formatted = "";
        final LinkedList result = new LinkedList<>();
        try {
            parserDelegator.parse(reader, callback, true);
            formatted = callback.getFormattedText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // add to list
        return Arrays.asList(formatted.split("\n"));
    }
    
    public static String getHTML(InputStreamReader reader) {
    	String line = "", result = "";
    	BufferedReader in = new BufferedReader(reader);
        try {
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return result;
    }
    
    public static class HtmlParserCallback extends ParserCallback {
        private StringBuffer buffer;

        public HtmlParserCallback() {
            buffer = new StringBuffer();
        }

        public String getFormattedText() {
            return buffer.toString();
        }

        @Override
        public void handleText(char[] data, int pos) {
            buffer.append(data);
        }

        @Override
        public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
        }

        @Override
        public void handleEndTag(Tag t, int pos) {
        }

        @Override
        public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
            if (t.equals(Tag.BR)) {
                buffer.append("\n");
            }
        }
    }
    
    public static String clip(String target, String first, String last) {
    	return target.substring(target.indexOf(first) + first.length(), target.indexOf(last));
    }

}
