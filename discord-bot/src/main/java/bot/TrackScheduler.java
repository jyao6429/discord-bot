// Used example from Lavaplayer

package bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of
 * tracks.
 */
public class TrackScheduler extends AudioEventAdapter
{
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	private final Guild guild;
	private boolean isPlaying = false;

	/**
	 * @param player The audio player this scheduler uses
	 */
	public TrackScheduler(AudioPlayer player, Guild guild)
	{
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.guild = guild;
	}
	/**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track The track to play or add to queue.
	 */
	public void queue(AudioTrack track)
	{
		// Calling startTrack with the noInterrupt set to true will start the track only
		// if nothing is currently playing. If
		// something is playing, it returns false and does nothing. In that case the
		// player was already playing so this
		// track goes to the queue instead.
		if (!player.startTrack(track, true))
		{
			queue.offer(track);
		}
	}
	/**
	 * Start the next track, stopping the current one if it is playing.
	 */
	public void nextTrack()
	{
		// Start the next track, regardless of if something is already playing or not.
		// In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply stop the
		// player.
		player.startTrack(queue.poll(), false);
	}

	@Override public void onTrackStart(AudioPlayer player, AudioTrack track)
	{
		// Print the name of the song that starts playing in the #music channel
		TextChannel channel = guild.getTextChannelsByName("music", true).get(0);
		channel.sendMessage("Now playing: " + track.getInfo().title).queue();
		isPlaying = true;
	}

	@Override public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
	{
		// Only start the next track if the end reason is suitable for it (FINISHED or
		// LOAD_FAILED)
		isPlaying = false;
		if (endReason.mayStartNext)
		{
			nextTrack();
		}
	}

	@Override public void onPlayerPause(AudioPlayer player)    //Send a message when music is paused
	{
		TextChannel channel = guild.getTextChannelsByName("music", true).get(0);
		channel.sendMessage("Music paused").queue();
	}

	@Override public void onPlayerResume(AudioPlayer player)    //Send a message when music is resumed
	{
		TextChannel channel = guild.getTextChannelsByName("music", true).get(0);
		channel.sendMessage("Music resumed").queue();
	}
	public void clearQueue()
	{
		queue.clear();
		player.startTrack(queue.poll(), false);
	}
	public boolean getIsPlaying()
	{
		return isPlaying;
	}
}
