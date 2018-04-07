package commands;

import bot.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class _COMMAND_TEMPLATE implements Command
{

	private final String HELP = "USAGE: ![COMMAND HERE]";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}
	
	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{

	}
	
	@Override
	public String help()
	{
		return HELP;
	}
	
	@Override
	public void executed(boolean success, MessageReceivedEvent event)
	{
		return;
	}
}
