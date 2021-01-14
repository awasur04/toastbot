package events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.StrikeDatabase;
import utils.Util;

import java.util.HashMap;

public class Strike extends ListenerAdapter {

    StrikeDatabase sManager = new StrikeDatabase();
    private HashMap<Long, Integer> sDatabase = new HashMap<Long, Integer>();

    public Strike() {
        sManager.loadData();
        this.sDatabase = sManager.getDatabase();
    }


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] messageSend = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().getUser().isBot()) {
            if (event.getMessage().getContentRaw().startsWith("!strike")) {
                String[] eventMessage = event.getMessage().getContentRaw().split(" ");
                try {
                    Long targetID = event.getMessage().getMentionedUsers().get(0).getIdLong();
                    Integer strikes = sDatabase.getOrDefault(targetID, 0);
                    sDatabase.put(targetID, strikes + 1);
                    sManager.saveData(sDatabase);
                    event.getChannel().sendMessage("<@" + targetID + "> has been issued x1 strike for " + Util.getString(eventMessage, 2, eventMessage.length) + "!").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("ERROR! User could not be found.").queue();
                    System.out.println("Error Strike : " + e.getMessage());
                }
            } else if (event.getMessage().getContentRaw().startsWith("!rstrike")) {
                if (event.getMessage().getMember().getRoles().get(0).getId().equals("303374177241530368")) {
                    if (permissionCheck(event.getMember(), event.getMessage().getMentionedMembers().get(0))) {
                        Long targetID = event.getMessage().getMentionedUsers().get(0).getIdLong();
                        Integer strikes = sDatabase.getOrDefault(targetID, 0);
                        sDatabase.put(targetID, strikes - 1);
                        sManager.saveData(sDatabase);
                        System.out.println("Removed 1 strike from " + targetID);
                    }
                }
            }
        }
    }

    public boolean permissionCheck(Member author, Member target) {
        String roleId = author.getRoles().get(0).getId();
        if (roleId.equalsIgnoreCase("303374177241530368") || roleId.equalsIgnoreCase("651981208187830302") || roleId.equalsIgnoreCase("364282391969267713")) {
            if (author.getIdLong() != target.getIdLong()) {
                return true;
            }
            return false;
        }
        return false;
    }

    public Integer getStrikes(Long targetID) {
       return sDatabase.get(targetID);
    }
}
