package net.tv.twitch.chrono_fish.werewolf;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.game.DummyPlayer;
import net.tv.twitch.chrono_fish.werewolf.game.Game;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import net.tv.twitch.chrono_fish.werewolf.instance.Action;
import net.tv.twitch.chrono_fish.werewolf.instance.Role;
import net.tv.twitch.chrono_fish.werewolf.instance.TimeZone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Events implements Listener {
    private final Game game;

    public Events(Game game){
        this.game = game;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new GamePlayer(e.getPlayer(),game);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {if(game.getGamePlayer(e.getPlayer())!=null) game.leave(game.getGamePlayer(e.getPlayer()));}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getCurrentItem() != null && e.getClickedInventory()!=null &&e.getClickedInventory().getHolder()==null){
            e.setCancelled(true);
            GamePlayer gamePlayer = game.getGamePlayer((Player) e.getWhoClicked());
            ItemStack clickedItem = e.getCurrentItem();
            SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
            GamePlayer target = null;
            if((skullMeta.getOwningPlayer()!=null)){
                target = game.getGamePlayer((Player) skullMeta.getOwningPlayer());
            }else{
                for (DummyPlayer dummyPlayer : game.getDummyPlayers()) {
                    if(Component.text(dummyPlayer.getName()).equals(skullMeta.displayName())){
                        target = dummyPlayer;
                    }
                }
            }
            if(target != null){
                if(e.getView().title().equals(Action.VOTE.getInventory_title())){
                    gamePlayer.vote(target);
                    gamePlayer.getPlayer().closeInventory();
                }
                if(e.getView().title().equals(Action.KILL.getInventory_title())){
                    gamePlayer.addKillPool(target);
                    gamePlayer.getPlayer().closeInventory();
                }
                if(e.getView().title().equals(Action.PREDICT.getInventory_title())){
                    gamePlayer.predict(target);
                    gamePlayer.getPlayer().closeInventory();
                }
                if(e.getView().title().equals(Action.SEE_DEAD.getInventory_title())){
                    gamePlayer.see(target);
                    gamePlayer.getPlayer().closeInventory();
                }
                if(e.getView().title().equals(Action.SEE_DEAD.getInventory_title())){
                    gamePlayer.see(target);
                    gamePlayer.getPlayer().closeInventory();
                }
                if(e.getView().title().equals(Action.PROTECT.getInventory_title())){
                    gamePlayer.protect(target);
                    gamePlayer.getPlayer().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        GamePlayer gamePlayer = game.getGamePlayer(e.getPlayer());
        if(gamePlayer != null && e.getItem()!=null){
            switch(e.getItem().getType()){
                case PAPER:
                    gamePlayer.openInventory(game.getCustomInventory().getActionInventory(Action.VOTE));
                    break;

                case NETHERITE_AXE:
                    gamePlayer.openInventory(game.getCustomInventory().getActionInventory(Action.KILL));
                    break;

                case HEART_OF_THE_SEA:
                    gamePlayer.openInventory(game.getCustomInventory().getActionInventory(Action.PREDICT));

                case WOODEN_SHOVEL:
                    gamePlayer.openInventory(game.getCustomInventory().getActionInventory(Action.SEE_DEAD));
                    break;
            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent e){
        if(!game.isRunning()) return;
        Component message = e.message();
        GamePlayer gamePlayer = game.getGamePlayer(e.getPlayer());
        if(!gamePlayer.isAlive()){
            e.setCancelled(true);
            gamePlayer.sendActionBar("§cあなたは死亡しました。発言はできません");
            return;
        }
        if(game.getCurrentTime().equals(TimeZone.NIGHT)){
            if(gamePlayer.getPlayer() == null) return;
            e.setCancelled(true);
            if(gamePlayer.getRole().equals(Role.WOLF)){
                game.wolfBroadCast(message);
            }else{
                gamePlayer.sendActionBar("§c夜の間は発言できません");
            }
        }else{
            e.setCancelled(true);
            game.getParticipants().forEach(participant ->
                    participant.sendMessage(Component.text("["+gamePlayer.getName()+"] ").append(message)));
        }
    }
}
