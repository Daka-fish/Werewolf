package net.tv.twitch.chrono_fish.werewolf.instance;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.tv.twitch.chrono_fish.werewolf.game.Game;
import net.tv.twitch.chrono_fish.werewolf.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class CustomInventory {

    private final Game game;
    private final Component vote;
    private final Component kill;

    public CustomInventory(Game game){
        this.game = game;
        this.vote = Component.text("Who do you vote?").decorate(TextDecoration.BOLD);
        this.kill = Component.text("Who do you kill?").decorate(TextDecoration.BOLD);
    }

    public Inventory getInventory(Action action){
        Inventory inventory = Bukkit.createInventory(null,27, getInventoryTitle(action));
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

    public Component getInventoryTitle(Action action){
        switch (action){
            case VOTE:
                return vote;

            case KILL:
                return kill;
        }
        return Component.text("");
    }
}
