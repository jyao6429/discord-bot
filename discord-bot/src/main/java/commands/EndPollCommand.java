package commands;

import java.util.ArrayList;
import bot.Command;
import bot.ModTools;
import bot.PollHandler;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class EndPollCommand implements Command
{

	private final String HELP = "USAGE: !endpoll";

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
		
		Guild guild = event.getGuild();
		Member member = event.getMember();
		TextChannel channel = event.getTextChannel();
		
		// First check if the user is a mod
		boolean isMod = ModTools.isMod(member);
		if (isMod)
		{
			if (PollHandler.isPolling.containsKey(guild) && PollHandler.isPolling.get(guild))	//Checks if there is a poll running
			{
				// Get the results
				int[] pollResults = PollHandler.poll.get(guild);
				int yesResults = pollResults[0];
				int noResults = pollResults[1];

				// Send the results
				channel.sendMessage("Poll results: " + yesResults + " Yes, " + noResults + " No").queue();
				
				// Reset the poll status for that guild
				PollHandler.isPolling.put(guild, false);
				PollHandler.hasVoted.put(guild, new ArrayList<Member>());
				PollHandler.poll.put(guild, new int[2]);
			}
			else
			{
				channel.sendMessage("Not currently polling").queue();
			}
		}
		else
		{
			channel.sendMessage("Must be a mod to stop poll").queue();
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
