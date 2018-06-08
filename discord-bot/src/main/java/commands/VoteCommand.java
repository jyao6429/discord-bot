package commands;

import bot.Command;
import bot.PollHandler;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class VoteCommand implements Command
{

	private final String HELP = "USAGE: !vote";

	@Override public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override public void action(String[] args, MessageReceivedEvent event)
	{
		if (!event.isFromType(ChannelType.TEXT))
		{
			MessageChannel otherChannel = event.getChannel();
			otherChannel.sendMessage("You must use this in a server!").queue();
			return;
		}

		String vote = "NO_VOTE";

		if (args.length != 0)    // Takes the first argument as the vote
		{
			vote = args[0];
		}

		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();

		if (PollHandler.allPolls.containsKey(channel))    // Checks if there is a poll rolling
		{
			// Gets current stats
			HashMap<String, Integer> pollStats = PollHandler.allPolls.get(channel);
			ArrayList<Member> votedMembers = PollHandler.hasVoted.get(channel);

			if (!votedMembers.contains(member))    // Makes sure user only votes once
			{
				if (PollHandler.yes.contains(vote))    // Checks if the argument is "yes" or "no"
				{
					vote = "yes";
				}
				else if (PollHandler.no.contains(vote))
				{
					vote = "no";
				}
				else if (vote.equals("NO_VOTE")) // No argument was given
				{
					channel.sendMessage("Please enter your vote after the command").queue();
					return;
				}
				try
				{
					int votes = pollStats.get(vote);    // Increments the given vote, will throw error if the vote option doesn't exist
					pollStats.put(vote, votes + 1);
					votedMembers.add(member);
					channel.sendMessage("Added a vote to: \"" + vote + "\"").queue();
				}
				catch (Exception e)
				{
					channel.sendMessage("Invalid vote").queue();    // Tell user that option does not exist
				}
			}
			else
			{
				channel.sendMessage("You have already voted").queue();
			}
		}
		else
		{
			channel.sendMessage("There is no poll running").queue();
		}
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
