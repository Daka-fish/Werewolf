package net.tv.twitch.chrono_fish.werewolf.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.instance.Role;
import net.tv.twitch.chrono_fish.werewolf.instance.TimeZone;
import org.bukkit.entity.Player;

public class GamePlayer {

    private final Player player;
    private Game game;
    private Role role;

    private boolean isAlive;
    private boolean hasVoted;
    private boolean hasActioned;

    private int voteCount;
    private GamePlayer actionTarget;

    public GamePlayer(Player player, Game game){
        this.player = player;
        this.game = game;
        this.role = Role.INNOCENT;
        game.join(this);
    }

    public GamePlayer(){
        this.player = null;
        this.role = Role.INNOCENT;
    }

    public Player getPlayer() {return player;}
    public String getName() {return player != null ? player.getName() : "";}

    public Role getRole() {return role;}
    public void setRole(Role role) {
        this.role = role;
        sendMessage("あなたの役職は"+role.getRoleName()+"§fです");
    }

    public boolean isAlive() {return isAlive;}
    public void setAlive(boolean alive) {isAlive = alive;}

    public boolean isHasVoted() {return hasVoted;}
    public void setHasVoted(boolean hasVoted) {this.hasVoted = hasVoted;}

    public boolean isHasActioned() {return hasActioned;}
    public void setHasActioned(boolean hasActioned) {this.hasActioned = hasActioned;}

    public int getVoteCount() {return voteCount;}
    public void setVoteCount(int voteCount) {this.voteCount = voteCount;}

    public GamePlayer getActionTarget() {return actionTarget;}
    public void setActionTarget(GamePlayer actionTarget) {this.actionTarget = actionTarget;}

    public void sendMessage(String message){if(player != null)player.sendMessage(message);}
    public void sendMessage(Component message){if(player != null)player.sendMessage(message);}

    public void vote(GamePlayer gamePlayer){
        if(game.isRunning() && this.game.getCurrentTime().equals(TimeZone.VOTE)){
            if(!hasVoted){
                if(!gamePlayer.equals(this)){
                    if(gamePlayer.isAlive()){
                        gamePlayer.setVoteCount(gamePlayer.getVoteCount()+1);
                        sendMessage("§e"+gamePlayer.getName()+"§fに投票しました");
                        setHasVoted(true);
                    }else{
                        sendMessage("§c死亡者には投票できません");
                    }
                }else{
                    sendMessage("§c自身には投票できません");
                }
            }else{
                sendMessage("§c既に投票しています");
            }
        }else{
            sendMessage("§c今は投票できません");
        }
    }

    public void addKillPool(GamePlayer gamePlayer){
        if(game.isRunning() && game.getCurrentTime().equals(TimeZone.NIGHT)){
            if(role.equals(Role.WOLF)){
                if(!gamePlayer.equals(this)){
                    if(!hasActioned){
                        setActionTarget(gamePlayer);
                        setHasActioned(true);
                        sendMessage("§e"+gamePlayer.getName()+"§fを標的にします");
                    }else{
                        sendMessage("§c既に行動しました");
                    }
                }else{
                    sendMessage("§c自身を標的にできません");
                }
            }else{
                sendMessage("§cあなたは行えません");
            }
        }else{
            sendMessage("§c今は行えません");
        }
    }
}
