package net.tv.twitch.chrono_fish.werewolf.instance;

public enum Role {
    //team 白:0, 黒1
    INNOCENT("市民",0),
    WOLF("人狼",1);

    private final String roleName;
    private final int team;

    Role(String roleName, int team){
        this.roleName = roleName;
        this.team = team;
    }

    public String getRoleName() {return roleName;}
    public int getTeam() {return team;}
}
