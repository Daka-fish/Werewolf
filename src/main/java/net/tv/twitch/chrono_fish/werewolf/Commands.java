package net.tv.twitch.chrono_fish.werewolf;

import net.tv.twitch.chrono_fish.werewolf.game.Game;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import net.tv.twitch.chrono_fish.werewolf.instance.Action;
import net.tv.twitch.chrono_fish.werewolf.instance.Role;
import net.tv.twitch.chrono_fish.werewolf.instance.TimeZone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class Commands implements CommandExecutor {

    private final Game game;

    public Commands(Main main){
        this.game = main.getGame();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player && game.isJoined((Player) commandSender)){
            Player sender = (Player) commandSender;
            GamePlayer gamePlayer = game.getGamePlayer(sender);
            if(command.getName().equalsIgnoreCase("start")){
                if(sender.isOp()){
                    game.start(gamePlayer);
                }
            }
            if(command.getName().equalsIgnoreCase("finish")){
                if(sender.isOp()){
                    game.finish();
                }
            }
            if(command.getName().equalsIgnoreCase("add")){
                if(game.isJoined(sender)){
                    game.addCpu();
                }
            }
            if(command.getName().equalsIgnoreCase("remove")){
                if(game.getDummyPlayers().size()>0){
                    game.removeCpu();
                }else{
                    gamePlayer.sendMessage("§cCPUがいません");
                }
            }
            if(command.getName().equalsIgnoreCase("time")){
                if(args.length==0){
                    sender.sendMessage("[時間設定]");
                    for (TimeZone timeZone : TimeZone.values()) {
                        sender.sendMessage(timeZone.getTimeName()+": §e"+timeZone.getTime()+"§f秒");
                    }
                }
                if(args.length==2){
                    String time = args[0].toUpperCase();
                    int second = Integer.parseInt(args[1]);
                    switch(time){
                        case "DAY":
                            TimeZone.DAY.setTime(second);
                            break;

                        case "VOTE":
                            TimeZone.VOTE.setTime(second);
                            break;

                        case "NIGHT":
                            TimeZone.NIGHT.setTime(second);
                            break;

                        default:
                            sender.sendMessage("§c有効な時間ではありません("+time+")");
                            return false;
                    }
                    sender.sendMessage("§e"+time+"§fの時間を"+"§a"+second+"§f秒に変更しました");
                }
            }
            if(command.getName().equalsIgnoreCase("role")){
                if(args.length==0){
                    gamePlayer.sendMessage("[役職一覧]");
                    Collections.sort(game.getRoles());
                    for (Role role : game.getRoles()) {
                        gamePlayer.sendMessage(role.getRoleName());
                    }
                }
                if(args.length==2){
                    String roleName = args[1].toUpperCase();
                    try{
                        Role role = Role.valueOf(roleName);
                        switch(args[0]){
                            case "add":
                                game.addRole(role);
                                gamePlayer.sendMessage("§e"+role.getRoleName()+"§fを追加しました");
                                break;

                            case "remove":
                                game.removeRole(role);
                                gamePlayer.sendMessage("§e"+role.getRoleName()+"§fを削除しました");
                                break;

                            case "list":
                                gamePlayer.sendMessage("[役職一覧]");
                                Collections.sort(game.getRoles());
                                for (Role r : game.getRoles()) {
                                    gamePlayer.sendMessage(r.getRoleName());
                                }
                                break;

                            default:
                                gamePlayer.sendMessage("/role {add, remove, list}");
                                break;
                        }
                    } catch (IllegalArgumentException e) {
                        gamePlayer.sendMessage("§c役職が見つかりませんでした");
                        throw new RuntimeException(e);
                    }
                }
            }
            if(command.getName().equalsIgnoreCase("action")){
                if(args.length>0){
                    switch (args[0]){
                        case "vote":
                            sender.openInventory(game.getCustomInventory().getActionInventory(Action.VOTE));
                            break;

                        case "kill":
                            sender.openInventory(game.getCustomInventory().getActionInventory(Action.KILL));
                            break;

                        case "predict":
                            sender.openInventory(game.getCustomInventory().getActionInventory(Action.PREDICT));
                            break;

                        case "see_dead":
                            sender.openInventory(game.getCustomInventory().getActionInventory(Action.SEE_DEAD));
                            break;
                    }
                }
            }
        }
        return false;
    }
}
