package wtf.mania.util.site;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.html.parser.ParserDelegator;

import wtf.mania.util.WebUtils;
import wtf.mania.util.WebUtils.HtmlParserCallback;

public class NichanUtils {
	
	private static Sure currentSure;
	
	public static Sure loadNiChan(String url) {
		currentSure = new Sure(url);
		return currentSure;
	}
	
	public static Sure getCurrentSure() {
		return currentSure;
	}
	
	public static class Sure {
		
		private final String url;
		
		private final String title;
		
		private final List<Resu> resuList;
		
		private Sure(String url) {
			this.url = url;
			resuList = new LinkedList<>();
			// load sure
			final InputStreamReader reader = WebUtils.getReader(url);
			final String html = WebUtils.getHTML(reader).replaceAll("　", "");
			// formattedは困ったときの最終兵器
			/*final List<String> formatted = WebUtils.getFormatted(reader);
			strings = formatted;*/
			// タイトル回収
			{
				this.title = WebUtils.clip(html, "<h1 class=\"title\">", "</h1>");
			}
			// レス回収
			{
				final String[] posts = html.split("<div class=\"post\"");
				int successCount = 0;
				for (int i = 0; i < posts.length; i++) {
					final String s  = posts[i];
					try {
						resuList.add(new Resu(
								WebUtils.clip(s, "id=\"", "\" data-date=\"NG\""),
								WebUtils.clip(s, "<span class=\"name\"><b>", "</b></span><span class=\"date\">"),
								WebUtils.clip(s, "</b></span><span class=\"date\">", "</span><span class=\"uid\">"),
								WebUtils.clip(s, "<div class=\"message\"><span class=\"escaped\">", "</span></div></div><br>").split("<br>")
								));
					} catch (Exception e) {
						e.printStackTrace();
					}
					//System.out.println(s);
					//System.out.println(++successCount);
				}
			}
		}
		
		public String getTitle() {
			return this.title;
		}
		
		public List<Resu> getResu() { 
			return this.resuList;
		}
	}
	
	public static class Resu {
		
		private final String id, userID, date;
		
		private String[] resu;

		private Resu(String id, String userID, String date, String[] resu) {
			super();
			this.id = id;
			this.userID = userID;
			this.date = date;
			this.resu = resu;
		}
		
		public String getInfo() {
			return String.format("%s §2%s §8%s", this.id, this.userID, this.date);
		}

		public String[] getResu() {
			return resu;
		}
	}

}
