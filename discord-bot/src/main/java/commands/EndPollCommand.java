package commands;

import java.util.ArrayList;
import bot.Command;
import bot.Global;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
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
		Guild guild = event.getGuild();
		Member member = event.getMember();
		TextChannel channel = event.getTextChannel();
		
		boolean isMod = Global.isMod(member);
		
		if(isMod)
		{
			if(Global.isPolling.containsKey(guild) && Global.isPolling.get(guild))
			{
				int[] pollResults = Global.poll.get(guild);
				int yesResults = pollResults[0];
				int noResults = pollResults[1];
				
				channel.sendMessage("Poll results: " + yesResults + " Yes, " + noResults + " No").queue();
				Global.isPolling.put(guild, false);
				Global.hasVoted.put(guild, new ArrayList<Member>());
				Global.poll.put(guild, new int[2]);
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
