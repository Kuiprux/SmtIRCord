package com.jimdo.jjh4296;

import com.jimdo.jjh4296.bots.BotHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SmtIRCord.MODID, name = SmtIRCord.MODNAME, version = SmtIRCord.VERSION, acceptableRemoteVersions = "*")
public class SmtIRCord {

	public static final String MODID = "SmtIRCord";
	public static final String MODNAME = "SmtIRCord";
	public static final String VERSION = "1.0";
	
	public static BotHandler botHandler;

	@Instance
	public static SmtIRCord instance = new SmtIRCord();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		botHandler = new BotHandler();
	}
}
