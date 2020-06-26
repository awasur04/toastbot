package utils;

import net.dv8tion.jda.api.entities.Member;

public class Account {
    private Member discordAccount;
    private int permissionLevel;

    public Account(Member tempDiscord, int tempPerms) {
        this.discordAccount = tempDiscord;
        this.permissionLevel = tempPerms;
    }

    public void setPermissionLevel(int permisisonLevel) {
        this.permissionLevel = permisisonLevel;
    }


    public int getPermissionLevel() {
        return this.permissionLevel;
    }
    public String getDiscordId() {
        return this.discordAccount.getId();
    }
}
