package events;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Util;

public class Strike extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] messageSend = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().getUser().isBot()) {
            if (event.getMessage().getContentRaw().startsWith("!strike")) {
                String[] eventMessage = event.getMessage().getContentRaw().split(" ");
                try {
                    User target = event.getMessage().getMentionedUsers().get(0);
                    event.getChannel().sendMessage("<@" + target.getId() + "> has been issued x1 strike for " + Util.getString(eventMessage, 2, eventMessage.length) + "!").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("ERROR! User could not be found.").queue();
                    System.out.println("Error Strike : " + e.getMessage());
                }
            }
        }
    }
}
