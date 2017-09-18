package commands;

import java.util.ArrayList;
import java.util.List;

import bot.Command;
import bot.Global;
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
		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();
		VoiceChannel myChannel = member.getVoiceState().getChannel();
		List<Member> connectedMembers = null;
		boolean isMod = Global.isMod(member);
		boolean skipped = false;
		int rounded = 0;
		
		try
		{
			connectedMembers = myChannel.getMembers();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		if(connectedMembers != null)
		{
			int numOfMembers = connectedMembers.size() - 1;
			double eightyPercent = numOfMembers * .8;
			rounded = (int) Math.ceil(eightyPercent);
		}
		if(skips.contains(member))
		{
			skipped = true;
		}

		if(isMod)
		{
			MusicController.skipTrack(channel);
		}
		else if(skipped)
		{
			channel.sendMessage("You have already skipped").queue();
		}
		else if(numOfSkips < rounded)
		{
			skips.add(member);
			numOfSkips++;
			if(numOfSkips == rounded)
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
