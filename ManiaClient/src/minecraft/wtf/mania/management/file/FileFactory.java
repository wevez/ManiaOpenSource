package wtf.mania.management.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileFactory extends Container<IFile> {

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    private File root;

    @Override
    public void add(IFile item) {
        item.setFile(root);
        super.add(item);
    }

    public void saveFile(Class<? extends IFile> iFile) {
        IFile file = findByClass(iFile);
        if (file != null) {
            file.save(GSON);
        }
    }

    public void loadFile(Class<? extends IFile> iFile) {
        IFile file = findByClass(iFile);
        if (file != null) {
            file.load(GSON);
        }
    }

    public void save() {
        forEach(file -> file.save(GSON));
    }

    public void load() {
        forEach(file -> file.load(GSON));
    }

    public void setupRoot(String name) {
        // make the root directory
        root = new File(Minecraft.getMinecraft().mcDataDir, name);
    }
}