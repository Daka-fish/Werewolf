package net.tv.twitch.chrono_fish.werewolf.game;

import net.tv.twitch.chrono_fish.werewolf.instance.Role;

import java.util.ArrayList;

public class ConfigManager {
    private final Game game;

    public ConfigManager(Game game){
        this.game = game;
    }

    public void loadOptions(){
        //timezone.values.foreach.setTime(...)
        //game.setRoles(...)
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.WOLF);
        roles.add(Role.INNOCENT);
        roles.add(Role.INNOCENT);
        roles.add(Role.INNOCENT);
        game.setRoles(roles);
    }
}
