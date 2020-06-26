package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tkcounter.TkCounter;
import utils.Util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TKEvent extends ListenerAdapter {

    TkCounter tkCounter = new TkCounter();
    private HashMap<Long, Integer> tkDatabase = new HashMap<>();

    public TKEvent() {
        tkCounter.loadData();
        this.tkDatabase = tkCounter.getTkDatabase();
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] messageSend = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().getUser().isBot()) {
            if (event.getMessage().getContentRaw().startsWith("!tk")) {
                String[] pieces = event.getMessage().getContentRaw().split(" ");
                try {
                    String command = pieces[1];
                    if (pieces[1].equalsIgnoreCase("show")) {
                        event.getChannel().sendMessage(messageBuilder()).queue();
                    } else if (pieces[1].equalsIgnoreCase("add")) {
                        try {
                            Long id = event.getMessage().getMentionedUsers().get(0).getIdLong();
                            Integer tks = tkDatabase.getOrDefault(id, 0);
                            tkDatabase.put(id, tks + 1);
                            tkCounter.saveData(tkDatabase);
                            event.getChannel().sendMessage("Added 1 tk to <@" + id + ">").queue();
                        } catch (Exception e) {
                            event.getChannel().sendMessage("Error: please specify a user");
                        }
                    }

                }
                catch (Exception e) {
                    event.getChannel().sendMessage("Team Kill Commands:\n1. !tk show - Show the current team kill leaderboard.\n2. !tk add @user - Adds a tk to the user defined").queue();
                }
            }
        }
    }

    public MessageEmbed messageBuilder() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Tarkov Team Kill Leaderboard", null);
        eb.setDescription("");

        Object[] sorted = tkDatabase.entrySet().toArray();
        Arrays.sort(sorted, new Comparator() {
            public int compare (Object o1, Object o2) {
                return ((Map.Entry<Long, Integer>) o2).getValue().compareTo(((Map.Entry<Long, Integer>) o1).getValue());
            }
        });



        int i = 1;
        for (Object id : sorted) {
            eb.appendDescription(i + ". <@" + (((Map.Entry<Long, Integer>) id).getKey()) + "> has " + ((Map.Entry<Long, Integer>) id).getValue() + " tks.\n");
            i++;
        }
        return eb.build();
    }
}
