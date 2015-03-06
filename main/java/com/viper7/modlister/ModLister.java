package com.viper7.modlister;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Level;
 
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;

@Mod(modid = ModLister.MODID, version = ModLister.VERSION)
public class ModLister {
    public static final String MODID = "modlister";
    public static final String VERSION = "0.1";
	public static final String DEPENDENCIES = "after:*";
	
	File mcDir;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		mcDir = event.getModConfigurationDirectory().getParentFile();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		List<String> lines = new ArrayList();
		for(ModContainer mod : Loader.instance().getModList())
			lines.add(createLine(mod));
 
		try {
			File file = new File(mcDir, "Mod List.txt");

			if(!file.exists())
				file.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for(String s : lines)
				writer.write(s);
			
			writer.close();
			
			FMLLog.log(Level.INFO, "ModLister wrote mod data properly.");
		} catch(IOException e) {
			FMLLog.log(Level.ERROR, "ModLister failed to write mod data!");
			e.printStackTrace();
		}
	}
	
	private String createLine(ModContainer container) {
		StringBuilder builder = new StringBuilder();
		builder.append(container.getName());
		builder.append(" (");
		builder.append(container.getModId());
		builder.append(") |  Version: ");
		builder.append(container.getVersion());
		builder.append(" | Loaded From ");
		builder.append(container.getSource().getName());
		builder.append(" on ");
		builder.append(container.getSource() == null || container.getSource().getParentFile() == null ? "N/A" : container.getSource().getParentFile().getName());
		builder.append(" | Website: ");
		builder.append(container.getMetadata().url.isEmpty() ? "N/A" : container.getMetadata().url);
		builder.append("\r\n");
		
		return builder.toString();
	}
}