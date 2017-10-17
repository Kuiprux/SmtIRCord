package com.jimdo.jjh4296.bots;

import javax.security.auth.login.LoginException;

import com.jimdo.jjh4296.SmtIRCord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordBot {

	private String channelId;
	
	private MessageChannel serverChannel;
	private String serverPrefix;
	private String serverPostfix;
	private JDABuilder serverBuilder;
	private JDA server;

	private MessageChannel ircChannel;
	private boolean ircEn;
	private String ircPrefix;
	private String ircPostfix;
	private JDABuilder ircBuilder;
	private JDA irc;

	private MessageChannel cmdChannel;
	private boolean cmdEn;
	private String cmdPrefix;
	private JDABuilder cmdBuilder;
	private JDA cmd;

	DiscordBot(boolean[] enables, String channel, String[] ids, String[] tokens, String[] games, String[] prefixes, String[] postfixes) {
		channelId = channel;
		
		serverPrefix = prefixes[0];
		serverPostfix = postfixes[0];

		this.ircEn = enables[0];
		this.ircPrefix = prefixes[1];
		this.ircPostfix = postfixes[1];

		this.cmdEn = enables[1];
		this.cmdPrefix = prefixes[2];

		try {
			serverBuilder = new JDABuilder(AccountType.BOT).setToken(tokens[0]).setStatus(OnlineStatus.ONLINE);
			server = serverBuilder.buildBlocking();
			serverChannel = server.getTextChannelById(channelId);
			server.getPresence().setGame(Game.of(games[0]));
			server.addEventListener(new ListenerAdapter() {
				@Override
				public void onMessageReceived(MessageReceivedEvent event) {
					String sender = event.getAuthor().getId();
					String msg = event.getMessage().toString();
					if(event.getChannel().equals(serverChannel)) {
						SmtIRCord.botHandler.sendMsg(BotHandler.ALL, BotHandler.DISCORD, sender, msg);
						SmtIRCord.botHandler.cmdHandler.performCmd(BotHandler.ALL, sender, msg);
					} else {
						SmtIRCord.botHandler.cmdHandler.performCmd(BotHandler.DISCORD, sender, msg);
					}
				}
			});
			
			if(ircEn) {
				ircBuilder = new JDABuilder(AccountType.BOT).setToken(tokens[0]).setStatus(OnlineStatus.ONLINE);
			} else {
				ircBuilder = serverBuilder;
			}
			irc = ircBuilder.buildBlocking();
			ircChannel = irc.getTextChannelById(channelId);
			irc.getPresence().setGame(Game.of(games[1]));
			
			if(cmdEn) {
				cmdBuilder = new JDABuilder(AccountType.BOT).setToken(tokens[0]).setStatus(OnlineStatus.ONLINE);
			} else {
				cmdBuilder = serverBuilder;
			}
			cmd = cmdBuilder.buildBlocking();
			cmdChannel = cmd.getTextChannelById(channelId);
			cmd.getPresence().setGame(Game.of(games[2]));
			
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(int from, String sender, String msg) {
		switch(from) {
		case BotHandler.SERVER:
			serverChannel.sendMessage(serverPrefix + sender + serverPostfix + msg);
		case BotHandler.DISCORD:
			break;
		case BotHandler.IRC:
			ircChannel.sendMessage(ircPrefix + sender + ircPostfix + msg);
			break;
		case BotHandler.COMMAND:
			cmdChannel.sendMessage(cmdPrefix + msg);
			break;
		}
	}
}
