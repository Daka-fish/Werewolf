package net.tv.twitch.chrono_fish.werewolf.game;

import net.tv.twitch.chrono_fish.werewolf.Main;
import net.tv.twitch.chrono_fish.werewolf.instance.Role;
import net.tv.twitch.chrono_fish.werewolf.instance.TimeZone;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;

public class ConfigManager {

    private final Main main;
    private final Game game;
    private final FileConfiguration config;

    public ConfigManager(Main main, Game game){
        this.main = main;
        this.game = game;
        File file = new File(main.getDataFolder(),"config.yml");
        if(!file.exists()) main.saveDefaultConfig();
        this.config = main.getConfig();
        main.saveDefaultConfig();
    }

    public void loadOptions(){
        ConfigurationSection rolesSection = config.getConfigurationSection("game.roles");
        ArrayList<Role> roles = new ArrayList<>();
        if(rolesSection != null){
            for(String key : rolesSection.getKeys(false)){
                try{
                     Role role = Role.valueOf(key.toUpperCase());
                     int roleCount = rolesSection.getInt(key);
                     for(int i=0; i<roleCount;i++){
                         roles.add(role);
                     }
                }catch (IllegalArgumentException e) {
                    main.consoleLog("Invalid role: " + key);
                }
            }
        }
        if(roles.isEmpty()){
            roles.add(Role.WOLF);
            roles.add(Role.INNOCENT);
            roles.add(Role.INNOCENT);
            roles.add(Role.INNOCENT);
        }
        game.setRoles(roles);

        ConfigurationSection timesSection = config.getConfigurationSection("game.times");
        if (timesSection != null) {
            for (String key : timesSection.getKeys(false)) {
                try {
                    TimeZone timeZone = TimeZone.valueOf(key.toUpperCase());
                    int time = timesSection.getInt(key);
                    timeZone.setTime(time);
                } catch (IllegalArgumentException e) {
                    main.consoleLog("Invalid time zone: " + key);
                }
            }
        }
        main.consoleLog("load game options from config.yml");
    }

    public void saveOptions(){
        for (Role role : Role.values()) {
            config.set("game.roles."+role.name().toLowerCase(), game.countRole(role));
        }

        for (TimeZone timeZone : TimeZone.values()) {
            config.set("game.times."+timeZone.name().toLowerCase(),timeZone.getTime());
        }
        main.saveConfig();
        main.consoleLog("save game options from config.yml");
    }
}
