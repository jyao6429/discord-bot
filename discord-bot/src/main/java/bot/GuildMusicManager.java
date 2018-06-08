// Used example from Lavaplayer

package bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.Guild;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
class GuildMusicManager
{
	/**
	 * Audio player for the guild.
	 */
	public final AudioPlayer player;
	/**
	 * Track scheduler for the player.
	 */
	public final TrackScheduler scheduler;
	/**
	 * Creates a player and a track scheduler.
	 *
	 * @param manager
	 * Audio player manager to use for creating the player.
	 */
	private final Guild guild;
	public GuildMusicManager(AudioPlayerManager manager, Guild guild)
	{
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player, guild);
		player.addListener(scheduler);
		this.guild = guild;
	}
	/**
	 * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
	 */
	public AudioPlayerSendHandler getSendHandler()
	{
		return new AudioPlayerSendHandler(player);
	}
}
