package net.tv.twitch.chrono_fish.werewolf;

import net.tv.twitch.chrono_fish.werewolf.game.ConfigManager;
import net.tv.twitch.chrono_fish.werewolf.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Game game;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.game = new Game(this);
        this.configManager = new ConfigManager(this, game);

        //コマンドの見直し
        getCommand("start").setExecutor(new Commands(this));
        getCommand("add").setExecutor(new Commands(this));
        getCommand("remove").setExecutor(new Commands(this));
        getCommand("action").setExecutor(new Commands(this));
        getCommand("finish").setExecutor(new Commands(this));
        getCommand("time").setExecutor(new Commands(this));
        getCommand("role").setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new Events(game),this);
        configManager.loadOptions();
    }

    @Override
    public void onDisable(){configManager.saveOptions();}

    public Game getGame() {return game;}

    public void consoleLog(String message){getLogger().info(message);}
}
