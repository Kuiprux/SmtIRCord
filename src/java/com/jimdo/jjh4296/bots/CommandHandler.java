package com.jimdo.jjh4296.bots;

import java.util.HashMap;

import com.jimdo.jjh4296.SmtIRCord;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandHandler {

	private HashMap<Character, String> prefixes;
	private HashMap<Character, String> prefixTexts;
	private HashMap<String, String> words;
	private HashMap<String, String> wordTexts;
	
	public CommandHandler(HashMap<Character, String> prefixCommands, HashMap<Character, String> prefixTexts, HashMap<String, String> wordCommands, HashMap<String, String> wordTexts) {
		prefixes = prefixCommands;
		this.prefixTexts = prefixTexts;
		words = wordCommands;
		this.wordTexts = wordTexts;
	}

	public void performCmd(int to, String sender, String msg) {
		// TODO Perform
		String[] args = msg.split("\\s+");
		
		char prefix = msg.charAt(0);
		String prefixCmd = prefixes.get(prefix);
		
		switch(prefixCmd) {
		case "dictionary":
			String dict = "dictTest: " + msg;
			String str = String.format(prefixTexts.get(prefix), dict);
			SmtIRCord.botHandler.sendMsg(BotHandler.COMMAND, to, sender, str);
			break;
		}

		String wordCmd = words.get(args[0]);
		switch(wordCmd) {
		case "playerList":
			int playerCount = MinecraftServer.getServer().getCurrentPlayerCount();
			EntityPlayerMP[] players = (EntityPlayerMP[]) MinecraftServer.getServer().getConfigurationManager().playerEntityList.toArray();
			StringBuilder playerNames = new StringBuilder();
			for(int i = 0; i < players.length; i++) {
				playerNames.append(players[i].getDisplayName());
				if(i-1 != players.length) {
					playerNames.append(", ");
				}
			}
			String str = String.format(wordTexts.get(wordCmd), playerCount, playerNames.toString());
			SmtIRCord.botHandler.sendMsg(BotHandler.COMMAND, to, sender, str);
			break;
		}
	}
}
