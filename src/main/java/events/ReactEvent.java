package events;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactEvent extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        String reaction = event.getReaction().getReactionEmote().getAsCodepoints();
        if (reaction.equalsIgnoreCase("U+274c")) {
            String messageId = event.getMessageId();
            event.getChannel().deleteMessageById(messageId).queue();
        }
    }

}
