package commands;

import bot.Command;
import bot.MusicController;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.*;
import youtube.Search;

public class PlayCommand implements Command
{

	private final String HELP = "USAGE: !play";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{
		TextChannel channel = event.getTextChannel();
		String[] command = event.getMessage().getContentDisplay().split(" ");
		
		if (command.length == 1)	// Checks if there was no argument given
		{
			channel.sendMessage("Please give a search query or an URL").queue();
		}
		else if (command.length == 2 && command[1].startsWith("https://"))	// If given an URL, play that
		{
			String url = command[1];
			MusicController.loadAndPlay(channel, url, event);
		}
		else	// Otherwise, search for it on YouTube
		{
			String searchTerms = "";
			for (String temp : args)
			{
				searchTerms += temp + " ";
			}
			String videoID = Search.searchVideo(searchTerms);
			String youtubeURL = "https://www.youtube.com/watch?v=";

			if (videoID != "NONE")
			{
				youtubeURL += videoID;
				MusicController.loadAndPlay(channel, youtubeURL, event);
			}
			else
			{
				channel.sendMessage("Unable to search for a video");
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
