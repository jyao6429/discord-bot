package commands;

import bot.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand implements Command
{

	private final String HELP = "USAGE: !ping";

	@Override public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override public void action(String[] args, MessageReceivedEvent event)
	{
		event.getChannel().sendMessage("PONG!").queue();
	}

	@Override public String help()
	{
		return HELP;
	}

	@Override public void executed(boolean success, MessageReceivedEvent event)
	{
		return;
	}
}
