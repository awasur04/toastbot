package events;


import audio.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Music extends ListenerAdapter {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private boolean currentlyPlaying = false;
    private final String BOT_ID = "705575116699861102";

    public Music() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] messageSend = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().getUser().isBot()) {
            if (event.getMessage().getContentRaw().startsWith("!radio")) {
                String[] eventMessage = event.getMessage().getContentRaw().split(" ");
                try {
                    String genre = eventMessage[1];
                    System.out.println(genre);
                    VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
                    if (genre.equalsIgnoreCase("chill")) {
                        loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=5qap5aO4i9A&feature=youtu.be", voiceChannel, genre);
                    } else if (genre.equalsIgnoreCase("stop")) {
                        stop(event.getGuild());
                    } else if (genre.equalsIgnoreCase("rap")) {
                        loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=dTtshy4KzSY", voiceChannel, genre);
                    } else if (genre.equalsIgnoreCase("trap")) {
                        loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=BwuwYELL-tg", voiceChannel, genre);
                    } else if (genre.equalsIgnoreCase("trap2")) {
                        loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=Oxj2EAr256Y", voiceChannel, "trap 2");
                    } else if (genre.equalsIgnoreCase("rap2")) {
                        loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=05689ErDUdM", voiceChannel, "rap 2");
                    }
                } catch (Exception e) {
                    event.getChannel().sendMessage("Please select a genre from the list below:\n1.Chill\n2.Rap\n3.Rap2\n4.Trap\n5.Trap2\nMore coming soon!").queue();
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (checkChannel(event)) {
            stop(event.getGuild());
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (checkChannel(event)) {
            stop(event.getGuild());
        }
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl, final VoiceChannel playHere, String genre) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                channel.sendMessage("Now playing "+ genre +" playlist").queue();
                if (currentlyPlaying) {
                    next(channel, channel.getGuild());
                }
                currentlyPlaying = true;
                play(channel.getGuild(), musicManager, audioTrack, playHere);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                channel.sendMessage("Playlist not supported yet.").queue();
                currentlyPlaying = true;
            }

            @Override
            public void noMatches() {
                channel.sendMessage("where it at do").queue();
                currentlyPlaying = false;
                System.out.println("Couldnt find stream");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("broken").queue();
                currentlyPlaying = false;
                System.out.println("Not loading.");
            }
        });
    }

    private void stop(Guild guild) {
        AudioManager audioManager = guild.getAudioManager();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.nextTrack();
        currentlyPlaying = false;

        audioManager.closeAudioConnection();
    }

    private void next(TextChannel channel, Guild guild) {
        AudioManager audioManager = guild.getAudioManager();
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, VoiceChannel playHere) {
        AudioManager audioManager = guild.getAudioManager();
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            audioManager.openAudioConnection(playHere);
        }

        musicManager.scheduler.queue(track);

    }

    private boolean checkChannel(GuildVoiceUpdateEvent event) {
        List<Member> membersInChannel = event.getChannelLeft().getMembers();
        if (!event.getEntity().getId().equals(BOT_ID)) {
            if (membersInChannel.size() <= 1) {
                for (Member member : membersInChannel) {
                    if (member.getId().equals(BOT_ID)) {
                        System.out.println("Leaving");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
