package net.tv.twitch.chrono_fish.werewolf.parts;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import net.tv.twitch.chrono_fish.werewolf.instance.TimeZone;


public class GameBossBar {

    private final BossBar bossBar;

    public GameBossBar(){
        this.bossBar = BossBar.bossBar(
                Component.text("-時計-"),
                1.0f,
                BossBar.Color.WHITE,
                BossBar.Overlay.NOTCHED_6
        );
    }

    public void setBossBar(TimeZone timeZone){
        bossBar.name(Component.text("- §l"+timeZone.getTimeName()+"§r -"));
        bossBar.color(BossBar.Color.valueOf(timeZone.getColor().toUpperCase()));
    }

    public void setTime(float time){bossBar.progress(time);}

    public void showBossBar(GamePlayer gamePlayer){if(gamePlayer.getPlayer() != null) gamePlayer.getPlayer().showBossBar(bossBar);}

    public void hideBossBar(GamePlayer gamePlayer){if(gamePlayer.getPlayer() != null) gamePlayer.getPlayer().hideBossBar(bossBar);}
}
