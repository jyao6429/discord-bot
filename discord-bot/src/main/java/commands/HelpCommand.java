package commands;

import java.awt.Color;

import bot.Command;
import bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
			    .addField("!play [URL] (or) [search query]", "Play music in the voice channel you are in (defaults to the Music channel if you are not in one) by giving a URL or keywords for the bot to search on YouTube\n*(URL supports: Youtube, SoundCloud, BandCamp, Vimeo, Twitch)*", false)
			    .addField("!skip", "Skips the current song if enough votes are given", false)
			    .addField("!pause", "Pauses music that is playing", false)
			    .addField("!resume", "Resumes music that is paused", false)
			    .addField("!stop", "Stops the music and disconnects the bot (Mods only)", false)
			    .addField("!startpoll", "Starts a new poll (Mods only) (Only one running poll per server)", false)
			    .addField("!endpoll or !stoppoll", "Stops the current running poll and displays the results", false)
			    .addField("!vote [yes or no]", "Vote in a running poll", false)
			    .addField("!lmgtfy [search query]", "Returns a LMGTFY link with the search parameter because we are lazy", false)
			    .addField("!block", "I have absolutely no idea", false)
			    .build())
			  .build();

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
		User sender = event.getAuthor();
		ChannelType type = event.getChannelType();

		if(!type.equals(ChannelType.PRIVATE))
		{
			TextChannel channel = event.getTextChannel();

			sender.openPrivateChannel().queue(
					(privateChannel) -> privateChannel.sendMessage(embedded).queue(										// Open a PrivateChannel and attempt to send the embedded message
							(success) -> channel.sendMessage(sender.getAsMention() + " Sent to you in a DM!").queue(), 	// If it succeeds, then mention the user in the TextChannel that the info was sent in a PM
							(failure) -> channel.sendMessage(embedded).queue()));										// If it fails (ie. user doesn't allow PMs), then just directly send the info in the TextChannel
		}
		else
		{
			event.getPrivateChannel().sendMessage(embedded).queue();	// Same, except now it's if the command was sent in a PM, so no need for the success/failures
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
