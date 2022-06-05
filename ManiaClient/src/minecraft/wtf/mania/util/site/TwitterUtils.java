package wtf.mania.util.site;

import java.io.InputStreamReader;

import wtf.mania.util.WebUtils;

public class TwitterUtils {
	
	private static TwitterAccount currentAccount;
	
	public static TwitterAccount loadAccount(String url) {
		return currentAccount = new TwitterAccount(url);
	}
	
	public static TwitterAccount getCurrentAccount() {
		return currentAccount;
	}
	
	public static class TwitterAccount {
		
		private final String name;
		private final String url;
		
		private TwitterAccount(String url) {
			super();
			final InputStreamReader reader = WebUtils.getReader(url);
			final String html = WebUtils.getHTML(reader).replaceAll("Å@", "");
			System.out.println(html);
			this.name = "";
			this.url = url;
		}
		
		public String getName() {
			return this.name;
		}
		
	}
	
	public static class Tweet {
		
		private final String content;
		private final TwitterAccount sender;
		
		private Tweet(String content, TwitterAccount sender) {
			super();
			this.content = content;
			this.sender = sender;
		}
		
	}

}
