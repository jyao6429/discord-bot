// Used example from JDA

package commands;

import bot.Command;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class RollCommand implements Command
{

	private final String HELP = "USAGE: !roll";

	@Override public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override public void action(String[] args, MessageReceivedEvent event)
	{
		MessageChannel channel = event.getChannel();
		Random rand = new Random();
		int roll = rand.nextInt(6) + 1; // This results in 1 - 6 (instead of 0 - 5)
		channel.sendMessage("Your roll: " + roll).queue(sentMessage -> // This is called a lambda statement. If you
				// don't know
		{ // what they are or how they work, try google!
			if (roll < 3)
			{
				channel.sendMessage("The roll for messageId: " + sentMessage.getId() + " wasn't very good... Must be bad luck!\n").queue();
			}
		});
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
