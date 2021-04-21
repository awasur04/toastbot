package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SuperBowl extends ListenerAdapter {
     ArrayList<Game> games = new ArrayList<>();

    public SuperBowl() {
        Game sb1 = new Game("Dallas Cowboys", 1, "17-3", "158434296443437057");
        this.games.add(sb1);
        Game sb2 = new Game("Green Bay Packers", 2, "19-1", "171796083565920257");
        this.games.add(sb2);
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] messageSend = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().getUser().isBot()) {
            if (event.getMessage().getContentRaw().startsWith("!superbowl") || event.getMessage().getContentRaw().startsWith("!sb")) {
                try {
                    event.getChannel().sendMessage(messageBuilder()).queue();
                }
                catch (Exception e) {
                }
            }
        }
    }

    public MessageEmbed messageBuilder() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("SuperBowl History", null);
        eb.setDescription("");
        for (Game game: games) {
            eb.appendDescription(game.getId() + ". " + game.getTeam() + " (" + game.getRecord() + ")   User: <@" + game.getWinningId() + ">\n");
        }
        return eb.build();
    }
}
