package net.tv.twitch.chrono_fish.werewolf.command;

import net.tv.twitch.chrono_fish.werewolf.instance.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        switch(command.getName()){
            case "game":
                suggestions.add("start");
                suggestions.add("finish");
                break;

            case "cpu":
                suggestions.add("list");
                suggestions.add("add");
                suggestions.add("remove");
                break;

            case "timezone":
                if(args.length==1){
                    suggestions.add("day");
                    suggestions.add("night");
                    suggestions.add("vote");
                    suggestions.add("list");
                }
                break;

            case "role":
                if(args.length==1){
                    suggestions.add("add");
                    suggestions.add("remove");
                    suggestions.add("list");
                }
                if(args.length==2 && !args[0].equalsIgnoreCase("list")){
                    for (Role role : Role.values()) {
                        suggestions.add(role.name().toLowerCase());
                    }
                }
                break;
        }
        List<String> filteredSuggestions = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                filteredSuggestions.add(suggestion);
            }
        }
        return filteredSuggestions;
    }
}
