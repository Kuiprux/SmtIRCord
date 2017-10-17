package com.jimdo.jjh4296.bots;

import java.io.File;
import java.util.HashMap;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.config.Configuration;

public class BotHandler {
	public static final int SERVER = 0;
	public static final int DISCORD = 1;
	public static final int IRC = 2;
	public static final int COMMAND = 3;
	public static final int ALL = 4;
	Server server;
	DiscordBot discordBot;
	IRCBot ircBot;
	CommandHandler cmdHandler;

	public BotHandler() {
		Configuration config = new Configuration(new File("config/smtIRCord.cfg"));
		config.load();

		String[] svPrefixes = new String[3];
		String[] svPostfixes = new String[2];

		svPrefixes[0] = config.get("server", "discord.prefix", "[discord] <").getString();
		svPostfixes[0] = config.get("server", "discord.postfix", ">").getString();

		svPrefixes[1] = config.get("server", "irc.prefix", "[irc] <").getString();
		svPostfixes[1] = config.get("server", "irc.postfix", ">").getString();

		svPrefixes[2] = config.get("server", "cmd.prefix", "[cmd] ").getString();

		this.server = new Server(svPrefixes, svPostfixes);

		boolean[] enables = new boolean[2];
		String[] discordIds = new String[3];
		String[] discordTokens = new String[3];
		String[] discordGames = new String[3];
		String[] discordPrefixes = new String[3];
		String[] discordPostfixes = new String[2];

		String channel = config.get("discord", "discord.channel", "").getString();

		discordIds[0] = config.get("discord", "server.id", "").getString();
		discordTokens[0] = config.get("discord", "server.token", "").getString();
		discordGames[0] = config.get("discord", "server.game", "Server").getString();
		discordPrefixes[0] = config.get("discord", "server.prefix", "<").getString();
		discordPostfixes[0] = config.get("discord", "server.postfix", ">").getString();

		enables[0] = config.get("discord", "irc.enabled", true).getBoolean();
		discordIds[1] = config.get("discord", "irc.id", "").getString();
		discordTokens[1] = config.get("discord", "irc.token", "IRC").getString();
		discordGames[0] = config.get("discord", "irc.game", "<").getString();
		discordPrefixes[1] = config.get("discord", "irc.prefix", "<").getString();
		discordPostfixes[1] = config.get("discord", "irc.postfix", ">").getString();

		enables[1] = config.get("discord", "cmd.enabled", true).getBoolean();
		discordIds[2] = config.get("discord", "cmd.id", "").getString();
		discordTokens[2] = config.get("discord", "cmd.token", "").getString();
		discordGames[0] = config.get("discord", "cmd.game", "Command").getString();
		discordPrefixes[2] = config.get("discord", "cmd.prefix", "[CMD]").getString();
		try {
			this.discordBot = new DiscordBot(enables, channel, discordIds, discordTokens, discordGames, discordPrefixes,
					discordPostfixes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] names = new String[3];
		String[] ircPrefixes = new String[3];
		String[] ircPostfixes = new String[2];

		String ircServer = config.get("irc", "irc.server", "").getString();
		int ircPort = config.get("irc", "irc.port", "6667").getInt();
		String ircChannel = config.get("irc", "irc.channel", "<").getString();

		names[0] = config.get("irc", "server.name", "Server").getString();
		ircPrefixes[0] = config.get("irc", "server.prefix", "<").getString();
		ircPostfixes[0] = config.get("irc", "server.postfix", ">").getString();

		names[1] = config.get("irc", "discord.name", "Discord").getString();
		ircPrefixes[1] = config.get("irc", "discord.prefix", "<").getString();
		ircPostfixes[1] = config.get("irc", "discord.postfix", ">").getString();

		names[2] = config.get("irc", "command.name", "Command").getString();
		ircPrefixes[2] = config.get("irc", "command.prefix", "<").getString();
		try {
			this.ircBot = new IRCBot(ircServer, ircPort, ircChannel, names, ircPrefixes, ircPostfixes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<Character, String> prefixCommands = new HashMap<Character, String>();
		HashMap<Character, String> prefixTexts = new HashMap<Character, String>();
		HashMap<String, String> wordCommands = new HashMap<String, String>();
		HashMap<String, String> wordTexts = new HashMap<String, String>();

		char dict = config.get("commands", "prefix.dictionary", "$").getString().charAt(0);
		prefixCommands.put(Character.valueOf(dict), "dictionary");
		prefixTexts.put(Character.valueOf(dict), config.get("commands", "prefix.dictionary.text", "%s").getString());

		String playerList = config.get("commands", "word.playerList", "!who").getString();
		wordCommands.put(playerList, "playerList");
		wordTexts.put(playerList,
				config.get("commands", "prefix.playerList.text", "%d player(s) online: %s").getString());

		this.cmdHandler = new CommandHandler(prefixCommands, prefixTexts, wordCommands, wordTexts);
		FMLCommonHandler.instance().bus().register(this.cmdHandler);
		config.save();
	}

	public void sendMsg(int to, int from, String sender, String msg) {
		switch (to) {
		case 0:
			this.server.sendMsg(from, sender, msg);
			break;
		case 1:
			this.discordBot.sendMsg(from, sender, msg);
			break;
		case 2:
			this.ircBot.sendMsg(from, sender, msg);
			break;
		case 4:
			this.server.sendMsg(from, sender, msg);
			this.discordBot.sendMsg(from, sender, msg);
			this.ircBot.sendMsg(from, sender, msg);
		case 3:
		}
	}
}