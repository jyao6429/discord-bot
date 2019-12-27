package commands;

import bot.Command;
import bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class HelpCommand implements Command
{
	private final String avatarURL = Main.jda.getSelfUser().getAvatarUrl();
	private final Message embedded = new MessageBuilder()
			  .append("Here you go!")
			  .setEmbed(new EmbedBuilder()
			    .setTitle("!help")
			    .setDescription("This thing")
			    .setColor(new Color(4285679))
			    .setFooter("Made by jyao6429", null)
			    .setThumbnail(avatarURL)
			    .setAuthor("Bot Commands", null, avatarURL)
			    .addField("!ping", "Ping-pong!", false)
			    .addField("!roll", "Roll a die", false)
			    .addField("!kick @mention", "Kick someone (Mods only)", false)
			    .addField("!play [URL] (or) [search query]", "Play music in the voice channel you are in (defaults to the Music channel if you are not in one) by giving a URL or keywords for the bot to search on YouTube\n"
			    		+ "*(URL supports: Youtube, SoundCloud, BandCamp, Vimeo, Twitch)*", false)
			    .addField("!skip", "Skips the current song if enough votes are given", false)
			    .addField("!pause", "Pauses music that is playing", false)
			    .addField("!resume", "Resumes music that is paused", false)
			    .addField("!stop", "Stops the music and disconnects the bot (Mods only)", false)
			    .addField("!startpoll {[option1],[option2],...}", "Starts a new poll given the different voting possibilities\n"
			    		+ "*(The curly braces and commas are required, but the brackets are not)* ***DO NOT INCLUDE SPACES***"
			    		+ "\nIf no arguments are given, the options are \"yes\" and \"no\"\n"
			    		+ "(Mods only) (Only one running poll per text channel)", false)
			    .addField("!endpoll or !stoppoll", "Stops the current poll in that text channel and displays the results", false)
			    .addField("!vote [your vote]", "Vote in a running poll", false)
			    .addField("!lmgtfy [search query]", "Returns a LMGTFY link with the search parameter because we are lazy", false)
			    .addField("!block", "I have absolutely no idea", false)
			    .build())
			  .build();

	private final String HELP = "USAGE: !help";

	@Override public boolean called(String[] args, MessageReceivedEvent event)
	{
		return true;
	}

	@Override public void action(String[] args, MessageReceivedEvent event)
	{
		// Print all channel commands
		User sender = event.getAuthor();

		if (event.isFromType(ChannelType.TEXT))
		{
			TextChannel channel = event.getTextChannel();

			sender.openPrivateChannel().queue(
					(privateChannel) -> privateChannel.sendMessage(embedded).queue(        // Open a PrivateChannel and attempt to send the embedded message
							(success) -> channel.sendMessage(sender.getAsMention() + " Sent to you in a DM!").queue(), 	// If it succeeds, then mention the user in the TextChannel that the info was sent in a PM
							(failure) -> channel.sendMessage(embedded)
									.queue()));        // If it fails (ie. user doesn't allow PMs), then just directly send the info in the TextChannel
		}
		else if (event.isFromType(ChannelType.PRIVATE))
		{
			event.getPrivateChannel().sendMessage(embedded).queue();	// Same, except now it's if the command was sent in a PM, so no need for the success/failures
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
