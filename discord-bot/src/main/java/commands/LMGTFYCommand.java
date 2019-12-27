package commands;

import bot.Command;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LMGTFYCommand implements Command
{

	private final String HELP = "USAGE: !lmgtfy";

	@Override public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override public void action(String[] args, MessageReceivedEvent event)
	{
		MessageChannel channel = event.getChannel();
		String msg = event.getMessage().getContentDisplay();
		String[] splitMsg = msg.split(" ", 2);

		String url = splitMsg[1];
		String encodedUrl;
		try        // Get the message and encode it as a URL
		{
			encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
			channel.sendMessage("http://lmgtfy.com/?q=" + encodedUrl).queue();    // Give the link back with argument as search query
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			channel.sendMessage("Error").queue();
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
