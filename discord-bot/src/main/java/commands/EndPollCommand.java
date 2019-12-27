package commands;

import bot.Command;
import bot.ModTools;
import bot.PollHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;

public class EndPollCommand implements Command
{

	private final String HELP = "USAGE: !endpoll";

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

		Member member = event.getMember();
		TextChannel channel = event.getTextChannel();

		// First check if the user is a mod
		boolean isMod = ModTools.isMod(member);

		if (isMod)
		{
			if (PollHandler.allPolls.containsKey(channel))    //Checks if there is a poll running
			{
				// Get the results
				HashMap<String, Integer> results = PollHandler.allPolls.get(channel);

				// Send the results
				StringBuilder finalResults = new StringBuilder("Poll Results:");

				for (String tempKey : results.keySet())
				{
					finalResults.append(" {\"").append(tempKey).append("\": __**").append(results.get(tempKey)).append("**__}");
				}
				channel.sendMessage(finalResults.toString()).queue();

				// Reset the poll status for that text channel
				PollHandler.allPolls.remove(channel);
				PollHandler.hasVoted.remove(channel);
			}
			else
			{
				channel.sendMessage("Not currently polling").queue();
			}
		}
		else
		{
			channel.sendMessage("Must be a mod to stop the poll!").queue();
		}
	}

	@Override public String help()
	{
		return HELP;
	}

	@Override public void executed(boolean success, MessageReceivedEvent event)
	{
		//noinspection UnnecessaryReturnStatement
		return;
	}

}
