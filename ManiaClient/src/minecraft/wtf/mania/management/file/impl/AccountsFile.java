package wtf.mania.management.file.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.thealtening.auth.service.AlteningServiceType;

import wtf.mania.gui.screen.alt.Alt;
import wtf.mania.gui.screen.alt.GuiAltManager;
import wtf.mania.management.file.IFile;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class AccountsFile implements IFile {

    private File file;

    @Override
    public void save(Gson gson) {
        JsonArray array = new JsonArray();

        GuiAltManager.instance.alts.forEach(alt -> {
            JsonObject object = new JsonObject();

            object.addProperty("id", alt.id);
            object.addProperty("mail", alt.mail);
            object.addProperty("password", alt.passowrd);
            object.addProperty("uuid", alt.uuid);
            object.addProperty("type", alt.type.toString());
            

            array.add(object);
        });

        writeFile(gson.toJson(array), file);
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }
        Account[] accounts = gson.fromJson(readFile(file), Account[].class);
        if (accounts != null) {
            Arrays.stream(accounts).forEach(account -> GuiAltManager.instance.alts.add(new Alt(account.id, account.uuid, account.mail, account.passowrd, toType(account.type))));
        }
    }

    @Override
    public void setFile(File root) {
        file = new File(root, "/accounts.txt");
    }
    
    private AlteningServiceType toType(String type) {
    	for (AlteningServiceType t : AlteningServiceType.values()) {
    		if (type.equals(t.toString())) return t;
    	}
		return null;
    }

    private final class Account {

        @SerializedName("id")
        private final String id;

        @SerializedName("uuid")
        private final String uuid;

        @SerializedName("mail")
        private final String mail;
        
        @SerializedName("password")
        private final String passowrd;
        
        @SerializedName("type")
        private final String type;

        public Account(String id, String uuid, String mail, String passowrd, String type) {
    		this.id = id;
    		this.uuid = uuid;
    		this.mail = mail;
			this.passowrd = passowrd;
    		this.type = type;
    	}

    }

}
