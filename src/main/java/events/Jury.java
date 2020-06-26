package events;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Util;

public class Jury extends ListenerAdapter {
    boolean juryActive = false;
    int totalVotesYes = -1;
    int totalVoteNo = -1;
    Message juryMessage;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] messageSend = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().getUser().isBot()) {
            if (event.getMessage().getContentRaw().startsWith("!jury")) {
                String[] eventMessage = event.getMessage().getContentRaw().split(" ");
                try {
                    User target = event.getMessage().getMentionedUsers().get(0);
                    juryActive = true;
                    event.getChannel().sendMessage("<@" + target.getId() + "> is at trial for " + Util.getString(eventMessage, 2, eventMessage.length) + "! Please vote below.").queue(message -> {
                        message.addReaction("U+1F44D").queue();
                        message.addReaction("U+1F44E").queue();
                        juryMessage = message;
                    });
                } catch (Exception e) {
                    juryActive = false;
                    event.getChannel().sendMessage("ERROR! User could not be found.").queue();
                    System.out.println("Error Jury : " + e.getMessage());
                }
            }
        }
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        String reaction = event.getReaction().getReactionEmote().getAsCodepoints();
        if (reaction.equalsIgnoreCase("U+1F44D") && juryCheck(event)) {
            totalVotesYes++;
            System.out.println(totalVotesYes);
        } else if (reaction.equalsIgnoreCase("U+1F44E") && juryCheck(event)) {
            totalVoteNo++;
            System.out.println(totalVoteNo);
        }
    }

    public boolean juryCheck(GuildMessageReactionAddEvent event) {
        String testMessage = event.getMessageId();
        if (juryActive && testMessage.equals(juryMessage.getId())) {
            return true;
        }
        return false;
    }
}
