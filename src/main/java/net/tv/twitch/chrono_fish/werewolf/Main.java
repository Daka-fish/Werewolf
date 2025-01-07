package net.tv.twitch.chrono_fish.werewolf;

import net.tv.twitch.chrono_fish.werewolf.command.Commands;
import net.tv.twitch.chrono_fish.werewolf.command.TabCompleter;
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
        addCommand();
        Bukkit.getPluginManager().registerEvents(new Events(game),this);
        configManager.loadOptions();
    }

    @Override
    public void onDisable(){configManager.saveOptions();}

    public void addCommand(){
        String[] commands = {"game", "cpu", "timezone", "role", "action", "ww"};
        for (String command : commands) {
            getCommand(command).setExecutor(new Commands(this));
            getCommand(command).setTabCompleter(new TabCompleter());
        }
    }

    public Game getGame() {return game;}

    public void consoleLog(String message){getLogger().info(message);}
}
