package commands;

import bot.Command;
import bot.MusicController;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCommand implements Command
{

	private final String HELP = "USAGE: !play";

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

		TextChannel channel = event.getTextChannel();
		String[] command = event.getMessage().getContentDisplay().split(" ");

		if (command.length == 1)    // Checks if there was no argument given
		{
			channel.sendMessage("Please give a search query or an URL").queue();
		}
		else if (command.length == 2 && command[1].startsWith("https://"))    // If given an URL, play that
		{
			String url = command[1];
			MusicController.loadAndPlay(channel, url, event);
		}
		else    // Otherwise, search for it on YouTube
		{
			StringBuilder searchTerms = new StringBuilder();
			for (String temp : args)
			{
				searchTerms.append(temp).append(" ");
			}
			String youtubeSearch = "ytsearch:" + searchTerms;

			MusicController.loadAndPlay(channel, youtubeSearch, event);
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
