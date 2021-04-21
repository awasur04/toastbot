import events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Bot {

    public static void main(String[] args) throws Exception{
        JDA jda = JDABuilder.createDefault("NzA1NTc1MTE2Njk5ODYxMTAy.Xqt0NA.UeAloDEXjFlwLDQEJ_fVXnqh6fQ").setStatus(OnlineStatus.ONLINE).setActivity(Activity.playing("!help")).build();

        Strike strikes = new Strike();

        jda.addEventListener(new HelloEvent());
        jda.addEventListener(new ReactEvent());
        ///jda.addEventListener(new Jury());
        jda.addEventListener(strikes);
        jda.addEventListener(new Music());
        jda.addEventListener(new TKEvent());
        jda.addEventListener(new About(strikes));
        jda.addEventListener(new SuperBowl());
    }
}
