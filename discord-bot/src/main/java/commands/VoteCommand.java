package commands;

import java.util.ArrayList;
import bot.Command;
import bot.Global;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VoteCommand implements Command
{
	
	private final String HELP = "USAGE: !vote";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{
		String vote = "NO_VOTE";
		if(args.length != 0)
		{
			vote = args[0];
			vote.toLowerCase();
		}
		Guild guild = event.getGuild();
		TextChannel channel = event.getTextChannel();
		Member member = event.getMember();
		if(Global.isPolling.containsKey(guild) && Global.isPolling.get(guild))
		{
			int[] pollStats = Global.poll.get(guild);
			ArrayList<Member> votedMembers = Global.hasVoted.get(guild);
			int yesStats;
			int noStats;
			
			yesStats = pollStats[0];
			noStats = pollStats[1];
		
			if(!votedMembers.contains(member))
			{
				if(Global.yes.contains(vote))
				{
					yesStats++;
					pollStats[0] = yesStats;
					votedMembers.add(member);
					Global.poll.put(guild, pollStats);
					Global.hasVoted.put(guild, votedMembers);
					channel.sendMessage("Added vote to \"Yes\"").queue();
				}
				else if(Global.no.contains(vote))
				{
					noStats++;
					pollStats[1] = noStats;
					votedMembers.add(member);
					Global.poll.put(guild, pollStats);
					Global.hasVoted.put(guild, votedMembers);
					channel.sendMessage("Added vote to \"No\"").queue();
				}
				else if(vote.equals("NO_VOTE"))
				{
					channel.sendMessage("Please enter \"yes\" or \"no\" after the command").queue();
				}
				else
				{
					channel.sendMessage("Error voting").queue();
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
