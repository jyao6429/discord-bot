package commands;

import java.util.ArrayList;
import java.util.List;

import bot.Command;
import bot.ModTools;
import bot.MusicController;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.*;

public class SkipCommand implements Command
{

	private final String HELP = "USAGE: !skip";
	private static int numOfSkips = 0;
	private static ArrayList<Member> skips = new ArrayList<Member>();

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
		if (connectedMembers != null)	// If the voice channel is not empty, require 80% of the members to vote to skip before skipping
		{
			int numOfMembers = connectedMembers.size() - 1;
			double eightyPercent = numOfMembers * .8;
			rounded = (int) Math.ceil(eightyPercent);
			
			if (skips.contains(member))	// Checks if the user has skipped before
			{
				channel.sendMessage("You have already skipped").queue();
			}
			else if (isMod)	// If a mod calls the command, it will automatically skip
			{
				MusicController.skipTrack(channel);
				numOfSkips = 0;
				skips.clear();
			}
			else if (numOfSkips < rounded)		// Checks if the amount of skips needed is reached, then reset if skipped a song
			{
				skips.add(member);
				numOfSkips++;
				if (numOfSkips == rounded)
				{
					MusicController.skipTrack(channel);
					numOfSkips = 0;
					skips.clear();
				}
				else
				{
					channel.sendMessage("Need " + (rounded - numOfSkips) + " more skips").queue();
				}
			}
			else
			{
				channel.sendMessage("Error skipping").queue();
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
