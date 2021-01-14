package events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class HelloEvent extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageSend = event.getMessage().getContentRaw();
        Role role = event.getGuild().getRoleById("651981208187830302");
        List<Member> currentOwners = event.getGuild().getMembersWithRoles(role);
        if (!event.getMember().getUser().isBot()) {
            if (messageSend.equalsIgnoreCase("!help")) {
                event.getChannel().sendMessage("Commands:\n1. !radio <station | stop>\n2. !strike <@user> <reason>\n3. !tk <show | add @user>\n4. !about <@user>").queue();
            } else if (messageSend.equalsIgnoreCase("!owners")) {
                for(Member m : currentOwners) {
                    System.out.println(m.getId());
                }
            }
        }
    }
}
