package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageSend = event.getMessage().getContentRaw();
        if (!event.getMember().getUser().isBot()) {
            if (messageSend.equalsIgnoreCase("!help")) {
                event.getChannel().sendMessage("Commands:\n1. !radio <station | stop>\n2.!strike <@user> <reason>\n3. !tk <show | add @user>").queue();
            }
        }
    }
}
