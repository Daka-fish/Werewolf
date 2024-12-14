package net.tv.twitch.chrono_fish.werewolf;

import net.tv.twitch.chrono_fish.werewolf.game.Game;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import net.tv.twitch.chrono_fish.werewolf.instance.Action;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
                    sender.sendMessage("§cCPUがいません");
                }
            }
            if(command.getName().equalsIgnoreCase("action")){
                if(args.length>0){
                    switch (args[0]){
                        case "vote":
                            sender.openInventory(game.getCustomInventory().getInventory(Action.VOTE));
                            break;

                        case "kill":
                            sender.openInventory(game.getCustomInventory().getInventory(Action.KILL));
                            break;
                    }
                }
            }
        }
        return false;
    }
}
