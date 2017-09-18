package commands;

import java.util.ArrayList;
import bot.Command;
import bot.Global;
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
		boolean isMod = Global.isMod(member);
		
		if(isMod)
		{
			if(!Global.isPolling.containsKey(guild) || !Global.isPolling.get(guild))
			{
				Global.isPolling.put(guild, true);
				
				if(!Global.poll.containsKey(guild))
				{
					Global.poll.put(guild, new int[2]);
				}
				if(!Global.hasVoted.containsKey(guild))
				{
					Global.hasVoted.put(guild, new ArrayList<Member>());
				}
				
				channel.sendMessage("Started poll").queue();
			}
			else if(Global.isPolling.get(guild))
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
