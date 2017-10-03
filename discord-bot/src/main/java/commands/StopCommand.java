package commands;

import bot.Command;
import bot.ModTools;
import bot.MusicController;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCommand implements Command
{
	
	private final String HELP = "USAGE: !stop";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{
		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();
		boolean isMod = ModTools.isMod(member);
		if(isMod)
		{
			MusicController.stopPlaying(channel);
			channel.sendMessage("Stopped music").queue();
		}
		else
		{
			channel.sendMessage("Must be a mod to stop music").queue();
		}
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
