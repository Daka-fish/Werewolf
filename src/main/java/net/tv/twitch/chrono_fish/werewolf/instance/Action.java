package net.tv.twitch.chrono_fish.werewolf.instance;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;

public enum Action {
    VOTE(Component.text("投票する").decorate(TextDecoration.UNDERLINED)
            .clickEvent(ClickEvent.runCommand("/action vote"))),
    KILL(Component.text("殺害する").decorate(TextDecoration.UNDERLINED)
            .clickEvent(ClickEvent.runCommand("/action kill")));

    private final Component text;

    Action(Component text){
        this.text = text;
    }

    public Component getText() {return text;}
}
