package net.tv.twitch.chrono_fish.werewolf.instance;

public enum TimeZone {
    WAITING("§7準備中§f",0,"white"),
    DAY("昼",10, "white"),
    VOTE("投票",10,"green"),
    NIGHT("夜",10,"red");

    private final String timeName;
    private int time;
    private final String color;

    TimeZone(String timeName, int time, String color){
        this.timeName = timeName;
        this.time=time;
        this.color = color;
    }

    public String getTimeName() {return timeName;}

    public int getTime() {return time;}
    public void setTime(int time) {this.time = time;}

    public String getColor() {return color;}
}
