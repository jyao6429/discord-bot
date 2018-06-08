package commands;

import java.util.ArrayList;
import java.util.HashMap;

import bot.Command;
import bot.ModTools;
import bot.PollHandler;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StartPollCommand implements Command
{

	private final String HELP = "USAGE: !startpoll";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{
		if(!event.isFromType(ChannelType.TEXT))
		{
			MessageChannel otherChannel = event.getChannel();
			otherChannel.sendMessage("You must use this in a server!").queue();
			return;
		}
		
		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();
		
		// Checks if user is a mod
		boolean isMod = ModTools.isMod(member);
		if (isMod)
		{
			HashMap<String, Integer> pollStats = new HashMap<>();
			
			if (args.length != 0 && args[0].startsWith("{"))
			{
				String[] votes = args[0].substring(1, args[0].length() - 1).split(",");
				
				for (String temp : votes)
				{
					pollStats.put(temp, 0);
				}
				
			}
			else
			{
				pollStats.put("yes", 0);
				pollStats.put("no", 0);
			}
			PollHandler.allPolls.put(channel, pollStats);
			PollHandler.hasVoted.put(channel, new ArrayList<Member>());
			channel.sendMessage("Started poll").queue();
		}
		else
		{
			channel.sendMessage("Must be a mod to start a poll!").queue();
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
