package com.mania.management.font;

import java.util.ArrayList;
import java.util.List;

public class FontManager {
	
	private final List<TTFFontRenderer> instanceList;
	
	public FontManager() {
		this.instanceList = new ArrayList<>();
	}
	
	public TTFFontRenderer getFont(String name, int size) {
		for (TTFFontRenderer f : this.instanceList) if (f.isSame(name, size)) return f;
		final TTFFontRenderer font = new TTFFontRenderer(name, size);
		this.instanceList.add(font);
		return font;
	}
	
	public void printInfo() {
		System.out.println(String.format("size : %d", instanceList.size()));
	}

}
