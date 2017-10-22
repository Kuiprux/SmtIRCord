package com.jimdo.jjh4296.bots;

import java.io.IOException;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import com.jimdo.jjh4296.SmtIRCord;

public class IRC extends PircBot {
	IRC(String name, String ip, int port, String channel) {
		try {
			this.setVerbose(true);
			this.setName(name);
			this.connect(ip, port);
			this.joinChannel(channel);
		} catch (IOException | IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class IRCWithEvent extends IRC {
	IRCWithEvent(String name, String ip, int port, String channel) {
		super(name, ip, port, channel);
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		SmtIRCord.botHandler.sendMsg(BotHandler.ALL, BotHandler.IRC, sender, message);
		SmtIRCord.botHandler.cmdHandler.performCmd(BotHandler.IRC, sender, message);
	}
}