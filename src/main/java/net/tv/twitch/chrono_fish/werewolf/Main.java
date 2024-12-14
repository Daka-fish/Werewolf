package net.tv.twitch.chrono_fish.werewolf;

import net.tv.twitch.chrono_fish.werewolf.game.ConfigManager;
import net.tv.twitch.chrono_fish.werewolf.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Game game;

    @Override
    public void onEnable() {
        this.game = new Game(this);
        getCommand("start").setExecutor(new Commands(this));
        getCommand("add").setExecutor(new Commands(this));
        getCommand("action").setExecutor(new Commands(this));
        getCommand("finish").setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new Events(game),this);
        new ConfigManager(game).loadOptions();
    }

    @Override
    public void onDisable(){

    }

    public Game getGame() {return game;}
}
