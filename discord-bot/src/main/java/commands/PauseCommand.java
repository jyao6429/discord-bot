package commands;

import java.util.ArrayList;
import java.util.List;

import bot.Command;
import bot.ModTools;
import bot.MusicController;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCommand implements Command
{

	private final String HELP = "USAGE: !pause";
	private static int numOfVotes = 0;
	private static ArrayList<Member> voted = new ArrayList<Member>();

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}
	
	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{
		// Get all the channels
		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();
		VoiceChannel myChannel = member.getVoiceState().getChannel();
		
		List<Member> connectedMembers = null;
		boolean isMod = ModTools.isMod(member);
		int rounded = 0;

		try
		{
			connectedMembers = myChannel.getMembers();	// Get all the members connected in the voice channel
		}
		catch (Exception ex)
		{
			channel.sendMessage("You are not in a channel!").queue();
			return;
		}
		if (connectedMembers != null)	// If the voice channel is not empty, require 40% of the members to vote to pause before actually pausing
		{
			int numOfMembers = connectedMembers.size() - 1;
			double fourtyPercent = numOfMembers * .4;
			rounded = (int) Math.ceil(fourtyPercent);
			
			if (voted.contains(member))	// Checks if the user has voted before
			{
				channel.sendMessage("You have already voted!").queue();
			}
			else if (isMod)	// If a mod calls the command, it will automatically pause
			{
				MusicController.pause(channel);
				numOfVotes = 0;
				voted.clear();
			}
			else if (numOfVotes < rounded)		// Checks if the amount of votes needed is reached, then reset if paused a song
			{
				voted.add(member);
				numOfVotes++;
				if (numOfVotes == rounded)
				{
					MusicController.pause(channel);
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
				channel.sendMessage("Error pausing").queue();
			}
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
