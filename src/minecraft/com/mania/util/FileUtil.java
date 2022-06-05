package com.mania.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import com.mania.Mania;

public class FileUtil {
	
	public static final File CONFIG_DIR = new File(Mania.name, "configs"),
			SCRIPT_DIR = new File(Mania.name, "scripts"),
			ACCOUNT_DIR = new File(Mania.name, "accounts"),
			MUSIC_DIR = new File(Mania.name, "musics");
	
	public static String readFile(String fileName) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("assets/minecraft/mania/" + fileName)));
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        }
        return stringBuilder.toString();
    }

}
