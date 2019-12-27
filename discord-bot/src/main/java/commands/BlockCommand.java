// Used example from JDA

package commands;

import bot.Command;
import bot.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

public class BlockCommand implements Command
{

	private final String HELP = "USAGE: !block";

	@Override public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override public void action(String[] args, MessageReceivedEvent event)
	{
		MessageChannel channel = event.getChannel();
		try
		{
			// Note the fact that complete returns the Message object!
			// The complete() overload queues the Message for execution and will return when
			// the message was sent
			// It does handle rate limits automatically
			Message sentMessage = channel.sendMessage("I blocked and will return the message!").complete();
			// This should only be used if you are expecting to handle rate limits yourself
			// The completion will not succeed if a rate limit is breached and throw a
			// RateLimitException
			channel.sendMessage("I expect rate limitation and know how to handle it!").complete(false);

			Main.logMessage("Sent a message using blocking! Luckly I didn't get Ratelimited... MessageId: " + sentMessage.getId());
		}
		catch (RateLimitedException e)
		{
			Main.logMessage("Whoops! Got ratelimited when attempting to use a .complete() on a RestAction! RetryAfter: " + e.getRetryAfter());
		}
		// Note that RateLimitException is the only checked-exception thrown by
		// .complete()
		catch (RuntimeException e)
		{
			Main.logMessage("Unfortunately something went wrong when we tried to send the Message and .complete() threw an Exception.");
			e.printStackTrace();
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
