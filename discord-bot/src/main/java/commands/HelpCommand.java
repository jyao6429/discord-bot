package commands;

import bot.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand implements Command
{

	private final String HELP = "USAGE: !help";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event)
	{
		// Print all channel commands
		event.getTextChannel().sendMessage("Channel Commands:\n" + "!help = this thing\n" + "!ping = pingpong\n"
				+ "!roll = dice\n" + "!kick @mention = kick someone (Mods only)\n"
				+ "!block = I have absolutely no idea\n"
				+ "!play [URL] (or) [search query] = play music in the Music voice channel by giving a URL or keywords for the bot to search on YouTube (URL supports: Youtube, SoundCloud, BandCamp, Vimeo, Twitch)\n"
				+ "!skip = skips the current song if enough votes are given\n"
				+ "!stop = stops the music and disconnects the bot (Mods only)\n"
				+ "!lmgtfy [search query] = returns a LMGTFY link with the search parameter because we are lazy\n"
				+ "!startpoll = starts a new poll (Mods only) (Only one running poll per server)\n"
				+ "!vote [yes or no] = vote in a running poll\n"
				+ "!endpoll or !stoppoll = stops the current running poll and displays the results").queue();
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
