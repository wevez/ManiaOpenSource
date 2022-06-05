package wtf.mania.util.login;

import java.net.Proxy;
import java.util.List;
import java.util.Map;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.ServiceSwitcher;

import net.minecraft.util.Session;
import wtf.mania.MCHook;
import wtf.mania.gui.screen.alt.Alt;
import wtf.mania.gui.screen.alt.GuiAltManager;
import wtf.mania.util.sound.SoundUtils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AltUtils implements MCHook {
	
	public static final ServiceSwitcher altService = new ServiceSwitcher();
	private final YggdrasilUserAuthentication authentication;
	
	private static final String API_URL = "http://api.isla.jp:3579/api/mania/",
			API_KEY = "23cfa3360c7b1f87a91034ab59c1bae1ab69792353a55cd141aa8199767eacb3",
			AUTH_KEY = "zUuzWhr2wJFkpGzR9nekkNzWxHDfSc";
	
	public static Response getAlt() {
		try {
	        HttpPost post = new HttpPost("http://api.isla.jp:3579/api/mania/");

	        post.addHeader("Content-Type", "application/json");
	        post.addHeader("Pragma", "no-cache");

	        post.setEntity(new StringEntity("{\"AuthenticationSecret\": \"" + AUTH_KEY + "\", \"API-Key\": \""+API_KEY+"\"}"));

	        try (CloseableHttpClient httpClient = HttpClients.createDefault();
	            CloseableHttpResponse response = httpClient.execute(post)) {
	        	return new Response(EntityUtils.toString(response.getEntity()), response.getStatusLine().getStatusCode(), null);
	        }
		} catch (Exception e) {
		}
		return null;
	}
	
	public static class Response {
		public String text;
		public int status;
		public Map<String, List<String>> header;
		
		public Response(String text, int status, Map<String, List<String>> header) {
			this.text = text;
			this.status = status;
			this.header = header;
		}
	}
	
	private AltUtils() {
	    this.authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
	}
	
	public static AltUtils getInstance() {
		return new AltUtils();
	}
	
	public final AltUtils token(String token) {
	    altService.switchToService(AlteningServiceType.THEALTENING);
	    authentication.setUsername(token);
	    authentication.setPassword("TheAltening");
	    return this;
	}
	
	public final AltUtils username(String username) {
	    altService.switchToService(AlteningServiceType.MOJANG);
	    authentication.setUsername(username);
	    return this;
	}
	
	public final AltUtils password(String password) {
	    authentication.setPassword(password);
	    return this;
	}
	
	public final Session session() {
	    try {
	        authentication.logIn();
	        GameProfile profile = authentication.getSelectedProfile();
	        return new Session(profile.getName(), profile.getId().toString(), authentication.getAuthenticatedToken(), "MOJANG");
	    } catch (AuthenticationException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public boolean loginCheck(String username, String password) {
	    if (username.contains("@alt.com")) {
	        this.token(username);
	    } else {
	        this.username(username).password(password);
	    }
	
	    try {
	        authentication.logIn();
	        return true;
	    } catch (AuthenticationException e) {
	        return false;
	    }
	}
	
	public boolean login(int a) {
	    Session session = session();
	    GuiAltManager.instance.loading = null;
	    if (session == null) {
	    	GuiAltManager.instance.failedTimer.reset();
	    	SoundUtils.playSound("Failed.wav", 2000);
	    	return false;
	    }
	    SoundUtils.playSound("Succeed.wav", 2000);
	    mc.session = session;
	    if(GuiAltManager.instance.alts.get(a).type == AlteningServiceType.MOJANG)
	    	GuiAltManager.instance.alts.set(a, new Alt(mc.session.getUsername(), mc.session.getProfile().getId().toString(), GuiAltManager.instance.password.text, GuiAltManager.instance.mail.text, AlteningServiceType.MOJANG));
	    else
	    	GuiAltManager.instance.alts.set(a, new Alt(mc.session.getUsername(), mc.session.getProfile().getId().toString(), GuiAltManager.instance.password.text, "", AlteningServiceType.THEALTENING));
	    GuiAltManager.instance.focusedAlt = GuiAltManager.instance.alts.get(a);
	    GuiAltManager.instance.adding = false;
	    GuiAltManager.instance.mail.text = "";
	    GuiAltManager.instance.password.text = "";
	    GuiAltManager.instance.succeedTimer.reset();
	    return true;
	}
	
	public YggdrasilUserAuthentication getAuthentication() {
	    return authentication;
	}

}
