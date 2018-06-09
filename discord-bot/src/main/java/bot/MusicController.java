// Used example from Lavaplayer Demo

package bot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class MusicController
{
	private final static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	private final static Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
	private static Member member;

	public static void go()    // Initialize everything when bot is started
	{
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}
	private static synchronized GuildMusicManager getGuildAudioPlayer(Guild guild)    // Gets the music manager for each Guild
	{
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null)
		{
			musicManager = new GuildMusicManager(playerManager, guild);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}
	public static void loadAndPlay(final TextChannel channel, final String trackUrl, MessageReceivedEvent event)    // Plays the song
	{
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		member = event.getMember();

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
		{
			@Override public void trackLoaded(AudioTrack track)
			{
				channel.sendMessage("Adding to queue: " + track.getInfo().title).queue();    //Add the song to the queue

				play(channel.getGuild(), musicManager, track);
			}

			@Override public void playlistLoaded(AudioPlaylist playlist)    // Add a playlist the the queue
			{
				channel.sendMessage("Adding playlist to queue: " + playlist.getName() + " (" + playlist.getTracks().size() + " songs)").queue();

				for (AudioTrack track : playlist.getTracks())        // Add all the tracks
				{
					play(channel.getGuild(), musicManager, track);
				}
			}

			@Override public void noMatches()
			{
				channel.sendMessage("Nothing found by " + trackUrl).queue();
			}

			@Override public void loadFailed(FriendlyException exception)
			{
				channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			}
		});
	}

	private static void play(Guild guild, GuildMusicManager musicManager, AudioTrack track)
	{
		connectToMusicVoiceChannel(guild, guild.getAudioManager());
		musicManager.scheduler.queue(track);
	}

	private static void connectToMusicVoiceChannel(Guild guild, AudioManager audioManager)    // Connects to the voice channel
	{
		VoiceChannel myChannel = member.getVoiceState().getChannel();    //Connect the the voice channel the user is in
		if (myChannel != null)
		{
			audioManager.openAudioConnection(myChannel);
		}
		else if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect())    // Otherwise connect to the "Music" channel
		{
			VoiceChannel musicChannel = guild.getVoiceChannelsByName("music", true).get(0);
			audioManager.openAudioConnection(musicChannel);
		}
	}

	public static void skipTrack(TextChannel channel)    // Skip the current track and play the next one
	{
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		channel.sendMessage("Skipped to next track").queue();
		musicManager.scheduler.nextTrack();
	}
	public static void stopPlaying(TextChannel channel)		// Reset everything and disconnect
	{
		Guild guild = channel.getGuild();
		if (guild.getAudioManager().isConnected())			// Checks if bot is connected first
		{
			GuildMusicManager musicManager = getGuildAudioPlayer(guild);
			musicManager.scheduler.clearQueue();
			guild.getAudioManager().closeAudioConnection();
			channel.sendMessage("Stopped music").queue();
		}
		else
		{
			channel.sendMessage("Already stopped").queue();
		}
	}
	public static void resume(TextChannel channel)        // Resume music that was paused
	{
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		boolean isPaused = musicManager.player.isPaused();

		if (musicManager.scheduler.getIsPlaying())		// Checks if it is playing
		{
			if (isPaused)		// Checks if it is paused
			{
				musicManager.player.setPaused(false);
			}
			else
			{
				channel.sendMessage("Already playing").queue();
			}
		}
		else
		{
			channel.sendMessage("Nothing to resume").queue();
		}
	}
	public static void pause(TextChannel channel)        // Pause music that was playing
	{
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		boolean isPaused = musicManager.player.isPaused();

		if (musicManager.scheduler.getIsPlaying())		// Checks if it is playing
		{
			if (!isPaused)        // Checks if it is resumed
			{
				musicManager.player.setPaused(true);
			}
			else
			{
				channel.sendMessage("Already paused").queue();
			}
		}
		else
		{
			channel.sendMessage("Nothing to pause").queue();
		}
	}
}
