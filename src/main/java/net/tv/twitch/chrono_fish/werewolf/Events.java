package net.tv.twitch.chrono_fish.werewolf;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.game.DummyPlayer;
import net.tv.twitch.chrono_fish.werewolf.game.Game;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import net.tv.twitch.chrono_fish.werewolf.instance.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
                if(e.getView().title().equals(game.getCustomInventory().getInventoryTitle(Action.VOTE))){
                    gamePlayer.vote(target);
                    gamePlayer.getPlayer().closeInventory();
                }
                if(e.getView().title().equals(game.getCustomInventory().getInventoryTitle(Action.KILL))){
                    gamePlayer.addKillPool(target);
                    gamePlayer.getPlayer().closeInventory();
                }
            }
        }
    }
}
