package wtf.mania.management.file.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import wtf.mania.Mania;
import wtf.mania.management.file.IFile;
import wtf.mania.module.Module;

import java.io.File;

public class ModulesFile implements IFile {

    private File file;

    @Override
    public void save(Gson gson) {
        JsonObject object = new JsonObject();

        JsonObject modulesObject = new JsonObject();

        for (Module module : Mania.instance.moduleManager.array)
            modulesObject.add(module.name, module.save());

        object.add("Modules", modulesObject);

        writeFile(gson.toJson(object), file);
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        JsonObject object = gson.fromJson(readFile(file), JsonObject.class);
        if (object.has("Modules")){
            JsonObject modulesObject = object.getAsJsonObject("Modules");

            for (Module m : Mania.instance.moduleManager.array) {
                if (modulesObject.has(m.name))
                    m.load(modulesObject.getAsJsonObject(m.name));
            }
        }
    }

    @Override
    public void setFile(File root) {
        file = new File(root, "/modules.json");
    }
}
