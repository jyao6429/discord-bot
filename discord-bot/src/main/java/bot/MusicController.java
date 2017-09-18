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

    public static void go()
    {
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private static synchronized GuildMusicManager getGuildAudioPlayer(Guild guild)
    {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null)
        {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }
    public static void loadAndPlay(final TextChannel channel, final String trackUrl, MessageReceivedEvent event)
    {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        member = event.getMember();

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null)
                {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches()
            {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception)
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

    private static void connectToMusicVoiceChannel(Guild guild, AudioManager audioManager)
    {
    		VoiceChannel myChannel = member.getVoiceState().getChannel();
//System.out.println(myChannel);
    		if(myChannel != null)
    		{
    			audioManager.openAudioConnection(myChannel);
    		}
    		else if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect())
    		{
    			VoiceChannel musicChannel = guild.getVoiceChannelsByName("Music", true).get(0);
    			audioManager.openAudioConnection(musicChannel);
    		}
	}

	public static void skipTrack(TextChannel channel)
    {
    	GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }
	public static void stopPlaying(TextChannel channel)
	{
    	GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
    	Guild guild = channel.getGuild();
		musicManager.scheduler.clearQueue();
    	guild.getAudioManager().closeAudioConnection();
	}
}