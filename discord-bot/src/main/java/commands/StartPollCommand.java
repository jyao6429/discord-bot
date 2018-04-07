package commands;

import java.util.ArrayList;
import bot.Command;
import bot.ModTools;
import bot.PollHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
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
		Guild guild = event.getGuild();
		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();
		
		// Checks if user is a mod
		boolean isMod = ModTools.isMod(member);
		if (isMod)
		{
			if (!PollHandler.isPolling.containsKey(guild) || !PollHandler.isPolling.get(guild))	// Makes sure no poll is currently running
			{
				PollHandler.isPolling.put(guild, true);		// Now the poll is running

				// Create indexes for the Guild if it does not already exist
				if (!PollHandler.poll.containsKey(guild))
				{
					PollHandler.poll.put(guild, new int[2]);
				}
				if (!PollHandler.hasVoted.containsKey(guild))
				{
					PollHandler.hasVoted.put(guild, new ArrayList<Member>());
				}

				channel.sendMessage("Started poll").queue();
			}
			else if (PollHandler.isPolling.get(guild))
			{
				channel.sendMessage("There is already a running poll").queue();
			}
		}
		else
		{
			channel.sendMessage("Must be a mod to start a poll").queue();
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
