package net.tv.twitch.chrono_fish.werewolf.command;

import net.tv.twitch.chrono_fish.werewolf.Main;
import net.tv.twitch.chrono_fish.werewolf.game.DummyPlayer;
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
import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandExecutor {

    private final Game game;

    public Commands(Main main){this.game = main.getGame();}

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player && game.getGamePlayer((Player) commandSender)!=null){
            Player sender = (Player) commandSender;
            GamePlayer gamePlayer = game.getGamePlayer(sender);
            if(command.getName().equalsIgnoreCase("game")){
                if(args.length==1){
                    if(sender.isOp()){
                        String args_0 = args[0];
                        switch(args_0){
                            case "start":
                                game.start(gamePlayer);
                                break;

                            case "finish":
                                game.finish();
                                break;
                        }
                    }else{
                        sender.sendMessage("§c権限がありません");
                    }
                }else{
                    sender.sendMessage("§c/game {start, finish}");
                }
            }
            if(command.getName().equalsIgnoreCase("cpu")){
                if(args.length==1){
                    String args_0 = args[0];
                    switch(args_0){
                        case "add":
                            game.addCpu();
                            break;

                        case "remove":
                            if(game.getDummyPlayers().size()>0){
                                game.removeCpu();
                            }else{
                                gamePlayer.sendMessage("§cCPUがいません");
                            }
                            break;

                        case "list":
                            sender.sendMessage("[CPU一覧]");
                            int index = 1;
                            for (DummyPlayer dummyPlayer : game.getDummyPlayers()) {
                                sender.sendMessage(index+": "+dummyPlayer.getName());
                                index ++;
                            }
                            break;

                        default:
                            sender.sendMessage("§c/cpu {add, remove, list}");
                            break;
                    }
                }else{
                    sender.sendMessage("§c /cpu {add, remove, list}");
                }
            }
            if(command.getName().equalsIgnoreCase("timezone")){
                if(args.length==0){
                    sender.sendMessage("[時間設定]");
                    for (TimeZone timeZone : TimeZone.values()) {
                        sender.sendMessage(timeZone.getTimeName()+": §e"+timeZone.getTime()+"§f秒");
                    }
                }
                if(args.length==1){
                    if(args[0].equalsIgnoreCase("list")){
                        sender.sendMessage("[時間設定]");
                        for (TimeZone timeZone : TimeZone.values()) {
                            if(timeZone.equals(TimeZone.WAITING)) continue;
                            sender.sendMessage(timeZone.getTimeName()+": §e"+timeZone.getTime()+"§f秒");
                        }
                    }else{
                        sender.sendMessage("§c/time list");
                        sender.sendMessage("§c/time {DAY, VOTE, NIGHT} {time}");
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
                            sender.sendMessage("§c/time list");
                            sender.sendMessage("§c/time {DAY, VOTE, NIGHT} {time}");
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
                if(args.length==1){
                    if(args[0].equalsIgnoreCase("list")){
                        gamePlayer.sendMessage("[役職一覧]");
                        Collections.sort(game.getRoles());
                        for (Role r : game.getRoles()) {
                            gamePlayer.sendMessage(r.getRoleName());
                        }
                    }else{
                        gamePlayer.sendMessage("/role list");
                        gamePlayer.sendMessage("/role {add, remove} {role}");
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

                            default:
                                gamePlayer.sendMessage("/role list");
                                gamePlayer.sendMessage("/role {add, remove} {role}");
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

                        case "protect":
                            sender.openInventory(game.getCustomInventory().getActionInventory(Action.PROTECT));
                            break;
                    }
                }
            }
            if(command.getName().equalsIgnoreCase("ww")){
                HashMap<String, String> commandDescriptions = new HashMap<>();
                commandDescriptions.put("game", "ゲームの開始や終了を管理します");
                commandDescriptions.put("cpu", "CPUプレイヤーの追加や削除を行います");
                commandDescriptions.put("timezone", "タイムゾーンを設定または取得します");
                commandDescriptions.put("role", "役割の追加や削除、リストを表示します");
                commandDescriptions.put("ww", "コマンドの説明を表示します");

                sender.sendMessage("§6=== コマンド一覧 ===");
                for (Map.Entry<String, String> entry : commandDescriptions.entrySet()) {
                    sender.sendMessage(String.format("§e/%s §7- %s", entry.getKey(), entry.getValue()));
                }
            }
        }
        return false;
    }
}
