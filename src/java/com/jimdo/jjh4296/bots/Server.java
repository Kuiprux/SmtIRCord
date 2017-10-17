package com.jimdo.jjh4296.bots;

import com.jimdo.jjh4296.SmtIRCord;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class Server {
	
	private String discordPrefix;
	private String discordPostfix;
	
	private String ircPrefix;
	private String ircPostfix;
	
	private String cmdPrefix;

	public Server(String[] prefixes, String[] postfixes) {
		discordPrefix = prefixes[0];
		discordPostfix = postfixes[0];
		
		ircPrefix = prefixes[1];
		ircPostfix = postfixes[1];
		
		cmdPrefix = prefixes[2];
	}

	@SubscribeEvent
	public void ServerChatEvent(String message, String username, EntityPlayerMP player, ChatComponentTranslation component) {
		SmtIRCord.botHandler.sendMsg(BotHandler.ALL, BotHandler.SERVER, player.getDisplayName(), component.getUnformattedText());
		SmtIRCord.botHandler.cmdHandler.performCmd(BotHandler.SERVER, player.getDisplayName(), component.getUnformattedText());
	}
	
	public void sendMsg(int from, String sender, String msg) {
		String str = "";
		switch (from) {
		case BotHandler.SERVER:
			break;
		case BotHandler.DISCORD:
			str = discordPrefix + sender + discordPostfix + msg;
			break;
		case BotHandler.IRC:
			str = ircPrefix + sender + ircPostfix + msg;
			break;
		case BotHandler.COMMAND:
			str = cmdPrefix +  msg;
			break;
		}
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(str));
	}
}
