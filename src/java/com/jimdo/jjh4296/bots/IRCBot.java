package com.jimdo.jjh4296.bots;

public class IRCBot {
	
	private String channel;
	
	private IRCWithEvent serverBot;
	private String serverName;
	private String serverPrefix;
	private String serverPostfix;

	private IRC discordBot;
	private String discordName;
	private String discordPrefix;
	private String discordPostfix;

	private IRC cmdBot;
	private String cmdName;
	private String cmdPrefix;

	public IRCBot(String ip, int port, String channel, String[] names, String[] prefixes, String[] postfixes) {
		this.channel = channel;
		
		serverName = names[0];
		serverBot = new IRCWithEvent(serverName, ip, port, channel);
		serverPrefix = prefixes[0];
		serverPostfix = postfixes[0];

		discordName = names[1];
		discordBot = new IRC(discordName, ip, port, channel);
		discordPrefix = prefixes[1];
		discordPostfix = postfixes[1];

		cmdName = names[2];
		cmdBot = new IRC(cmdName, ip, port, channel);
		cmdPrefix = prefixes[2];
		
		
	}

	public void sendMsg(int from, String sender, String msg) {
		switch (from) {
		case BotHandler.SERVER:
			serverBot.sendMessage(channel, serverPrefix + sender + serverPostfix + msg);
		case BotHandler.DISCORD:
			discordBot.sendMessage(channel, discordPrefix + sender + discordPostfix + msg);
			break;
		case BotHandler.IRC:
			break;
		case BotHandler.COMMAND:
			cmdBot.sendMessage(channel, cmdPrefix + msg);
			break;
		}
	}

}
