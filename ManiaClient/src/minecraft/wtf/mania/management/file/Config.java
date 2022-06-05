package wtf.mania.management.file;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonObject;

import wtf.mania.Mania;
import wtf.mania.module.Module;

public final class Config {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.CONFIGS_DIR, name + ConfigManager.EXTENTION);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Module module : Mania.instance.moduleManager.array)
            modulesObject.add(module.name, module.save());

        jsonObject.add("Modules", modulesObject);

        return jsonObject;
    }

    public void load(JsonObject object) {
        if (object.has("Modules")) {
            JsonObject modulesObject = object.getAsJsonObject("Modules");

            for (Module module : Mania.instance.moduleManager.array) {
                if (modulesObject.has(module.name))
                    module.load(modulesObject.getAsJsonObject(module.name));
            }
        }
    }
}
