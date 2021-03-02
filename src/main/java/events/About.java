package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.StrikeDatabase;

import java.time.OffsetDateTime;
import java.util.HashMap;


public class About extends ListenerAdapter {
    Strike strikeDatabase;

    public About(Strike sDatabase) {
        this.strikeDatabase = sDatabase;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] messageSend = event.getMessage().getContentRaw().split(" ");
        if (!event.getMember().getUser().isBot()) {
            if (event.getMessage().getContentRaw().startsWith("!about")) {
                String[] eventMessage = event.getMessage().getContentRaw().split(" ");
                try {
                    Member target = event.getMessage().getMentionedMembers().get(0);
                    event.getChannel().sendMessage(messageBuilder(target)).queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("Error: User not found");
                }
            }
        }
    }

    public MessageEmbed messageBuilder(Member targetMember) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Detective Toasty");
        eb.setDescription("");
        eb.appendDescription("About <@" + targetMember.getUser().getIdLong() + ">\n");

        //Add users image
        try {
            eb.setThumbnail(targetMember.getUser().getAvatarUrl());
        } catch (Exception e) {}

        //get users discord name
        try {
            eb.appendDescription("Discord Id :" + targetMember.getUser().getName() + "#" + targetMember.getUser().getDiscriminator() + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Get users role and add to the message
        try {
            switch(targetMember.getId()) {
                case "169623711006195712":
                    eb.appendDescription("Role: Founding Father\n");
                    break;
                case "171796083565920257":
                    eb.appendDescription("Role: Co-Founder\n");
                    break;
                case "149290395115847680":
                    eb.appendDescription("Role: Co-Founder\n");
                    break;
                case "158434296443437057":
                    eb.appendDescription("Role: Lead Owner\n");
                    break;
                default:
                    Role currentRole = targetMember.getRoles().get(0);
                    if (currentRole.getId().equals("651981208187830302")) {
                        eb.appendDescription("Role: Owner\n");
                        break;
                    } else if (currentRole.getId().equals("364282391969267713")) {
                        eb.appendDescription("Role: VIP\n");
                        break;
                    } else if (currentRole.getId().equals("303374177241530368")) {
                        eb.appendDescription("Role: Admin\n");
                    } else {
                        eb.appendDescription("Role: " + currentRole.getName() + "\n");
                        break;
                    }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        //get users join time
        try {
            if (targetMember.hasTimeJoined()) {
                OffsetDateTime joinedDate = targetMember.getTimeJoined();
                eb.appendDescription("Joined: " + joinedDate.getMonthValue() + "-" + joinedDate.getDayOfMonth() + "-" + joinedDate.getYear() + "\n");
            } else {
                eb.appendDescription("Joined: Error finding join time");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //get users boost time
        try {
            OffsetDateTime boostDate = targetMember.getTimeBoosted();
            if (boostDate != null) {
                eb.appendDescription("Boosting since: "+ boostDate.getMonthValue() + "-" + boostDate.getDayOfMonth() + "-" + boostDate.getYear() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            Integer currentStrikes = strikeDatabase.getStrikes(targetMember.getIdLong());
            if (currentStrikes != null) {
                eb.appendDescription("Strikes: " + currentStrikes);
            } else {
                eb.appendDescription("Stikes: 0");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return eb.build();
    }
}
