package net.tv.twitch.chrono_fish.werewolf.game;

import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {

    private final Game game;
    private int time;

    public Timer(Game game){
        this.game=game;
    }

    public void setTime(int time){this.time=time;}

    @Override
    public void run() {
        if(time<=0){
            cancel();
            game.doTimeEnd();
            return;
        }
        game.getGameBossBar().setTime((float)time/game.getCurrentTime().getTime());
        time--;
    }
}
