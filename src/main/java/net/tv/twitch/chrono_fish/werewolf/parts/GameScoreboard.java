package net.tv.twitch.chrono_fish.werewolf.parts;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.game.Game;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import net.tv.twitch.chrono_fish.werewolf.instance.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class GameScoreboard {

    private final GamePlayer gamePlayer;
    private final Player player;

    private final Scoreboard scoreboard;
    private final Objective objective;

    private final String yourRole = "+あなたの役職";
    private final String yourTarget = "+昨夜の標的";
    private final String yourBuddy = "+仲間";

    private String roleScore;
    private String actionTarget;
    private String buddy;

    private final Game game;

    public GameScoreboard(Game game, GamePlayer gamePlayer){
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.player = gamePlayer.getPlayer();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, Component.text("-§l Info §r-"));
    }

    public void setBoard(){
        if(gamePlayer.getPlayer()==null) return;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("").setScore(0);
        roleScore = "  └ "+gamePlayer.getRole().getRoleName();
        actionTarget = "  └ "+"§7該当なし";
        buddy = "  └ "+"§7該当なし";
    }

    public void resetRole(){
        if(player==null) return;
        objective.getScore(roleScore).resetScore();
        roleScore = "  └ "+gamePlayer.getRole().getRoleName();
        objective.getScore(yourRole).setScore(-1);
        objective.getScore(roleScore).setScore(-2);
    }

    public void resetTarget(){
        if(player==null) return;
        objective.getScore(actionTarget).resetScore();
        actionTarget = "  └ "+gamePlayer.getActionTarget().getName();
        objective.getScore(" ").setScore(-3);
        objective.getScore(yourTarget).setScore(-4);
        objective.getScore(actionTarget).setScore(-5);
    }

    public void resetBuddy(){
        if(player==null) return;
        objective.getScore(buddy).resetScore();
        StringBuilder buddies = new StringBuilder();
        for (GamePlayer participant : game.getParticipants()) {
            if(participant.getRole().equals(Role.WOLF) && !participant.equals(gamePlayer)){
                buddies.append(participant.getName());
                buddies.append(" ");
            }
        }
        buddy = "  └ " + buddies;
        objective.getScore("   ").setScore(-6);
        objective.getScore(yourBuddy).setScore(-7);
        objective.getScore(buddy).setScore(-8);
    }

    public void setScore(){
        objective.getScore("").setScore(0);
        resetRole();
        switch(gamePlayer.getRole()){
            case WOLF:
                resetBuddy();
                break;

            case KNIGHT:
                resetTarget();
                break;
        }
    }

    public void removeScores(){
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
        objective.getScore("").setScore(0);
    }

    public void show(){if(gamePlayer.getPlayer()!=null) player.setScoreboard(scoreboard);}
    public void remove(){if(gamePlayer.getPlayer()!=null) player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);}
}
