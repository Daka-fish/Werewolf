package net.tv.twitch.chrono_fish.werewolf.parts;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.game.Game;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import net.tv.twitch.chrono_fish.werewolf.instance.Action;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class CustomInventory {

    private final Game game;

    public CustomInventory(Game game){this.game = game;}

    public Inventory getActionInventory(Action action){
        Inventory inventory = Bukkit.createInventory(null,27, action.getInventory_title());
        for (GamePlayer participant : game.getParticipants()) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            if(participant.getPlayer() != null) skullMeta.setOwningPlayer(participant.getPlayer());
            skullMeta.displayName(Component.text(participant.getName()));
            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text("状況：" + (participant.isAlive()?"§a生存":"§c死亡") ));
            skullMeta.lore(lore);
            head.setItemMeta(skullMeta);
            inventory.addItem(head);
        }
        return inventory;
    }
}
