package commands;

import bot.Command;
import bot.ModTools;
import bot.MusicController;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class ResumeCommand implements Command
{

	private final String HELP = "USAGE: !resume";
	private static int numOfVotes = 0;
	private static final ArrayList<Member> voted = new ArrayList<>();

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

		// Get all the channels
		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();
		VoiceChannel myChannel = member.getVoiceState().getChannel();

		List<Member> connectedMembers;
		boolean isMod = ModTools.isMod(member);
		int rounded;

		try
		{
			connectedMembers = myChannel.getMembers();    // Get all the members connected in the voice channel
		}
		catch (Exception ex)
		{
			channel.sendMessage("You are not in a channel!").queue();
			return;
		}
		if (connectedMembers != null)    // If the voice channel is not empty, require 40% of the members to vote to resume before actually resuming
		{
			int numOfMembers = connectedMembers.size() - 1;
			double fourtyPercent = numOfMembers * .4;
			rounded = (int) Math.ceil(fourtyPercent);

			if (voted.contains(member))    // Checks if the user has voted before
			{
				channel.sendMessage("You have already voted!").queue();
			}
			else if (isMod)    // If a mod calls the command, it will automatically resume
			{
				MusicController.resume(channel);
				numOfVotes = 0;
				voted.clear();
			}
			else if (numOfVotes < rounded)        // Checks if the amount of votes needed is reached, then reset if resumed a song
			{
				voted.add(member);
				numOfVotes++;
				if (numOfVotes == rounded)
				{
					MusicController.resume(channel);
					numOfVotes = 0;
					voted.clear();
				}
				else
				{
					channel.sendMessage("Need " + (rounded - numOfVotes) + " more votes").queue();
				}
			}
			else
			{
				channel.sendMessage("Error resuming").queue();
			}
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
