package commands;

import bot.Command;
import bot.MusicController;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import youtube.Search;

import java.util.Objects;

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
			String videoID = Search.searchVideo(searchTerms.toString());
			String youtubeURL = "https://www.youtube.com/watch?v=";

			if (!Objects.equals(videoID, "NONE"))
			{
				youtubeURL += videoID;
				MusicController.loadAndPlay(channel, youtubeURL, event);
			}
			else
			{
				channel.sendMessage("Unable to search for a video").queue();
			}

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
