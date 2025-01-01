package net.tv.twitch.chrono_fish.werewolf.parts;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class GameScoreboard {

    private final HashMap<GamePlayer, Scoreboard> scoreMap;
    private final HashMap<GamePlayer, Objective> objMap;

    private String roleScore;

    public GameScoreboard(){
        this.scoreMap = new HashMap<>();
        this.objMap = new HashMap<>();
    }

    public void addBoard(GamePlayer gamePlayer){
        if(gamePlayer.getPlayer()==null) return;
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, Component.text("-§l Info §r-"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("").setScore(0);
        objective.getScore("+あなたの役職").setScore(-1);
        scoreMap.put(gamePlayer,scoreboard);
        objMap.put(gamePlayer,objective);
        roleScore = "  └ "+gamePlayer.getRole().getRoleName();
    }

    public void resetRole(GamePlayer gamePlayer){
        if(gamePlayer.getPlayer()==null) return;
        Objective gpObj = objMap.get(gamePlayer);
        gpObj.getScore(roleScore).resetScore();
        roleScore = "  └ "+gamePlayer.getRole().getRoleName();
        gpObj.getScore(roleScore).setScore(-2);
    }

    public void show(GamePlayer gamePlayer){if(gamePlayer.getPlayer()!=null) gamePlayer.getPlayer().setScoreboard(scoreMap.get(gamePlayer));}
    public void remove(GamePlayer gamePlayer){if(gamePlayer.getPlayer()!=null) gamePlayer.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);}
}
