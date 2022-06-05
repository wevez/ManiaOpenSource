package wtf.mania.gui.screen.alt;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.thealtening.auth.service.AlteningServiceType;

public class Alt {
	
	public float animatedX, animatedExpand;
	
	public final String id, mail, passowrd, uuid;
	public final AlteningServiceType type;
	
	public final List<Server> servers;
	
	private Date lastUsed;
	
	public Alt(String id, String uuid, String mail, String passowrd, AlteningServiceType type) {
		this.id = id;
		this.uuid = uuid;
		this.mail = mail;
		this.type = type;
		this.passowrd = passowrd;
		servers = new LinkedList<>();
		lastUsed = new Date();
	}

}
