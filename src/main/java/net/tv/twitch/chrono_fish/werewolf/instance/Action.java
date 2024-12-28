package net.tv.twitch.chrono_fish.werewolf.instance;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;

public enum Action {
    VOTE(Component.text("投票する").decorate(TextDecoration.UNDERLINED)
            .clickEvent(ClickEvent.runCommand("/action vote")),
            Component.text("Who do you vote?").decorate(TextDecoration.BOLD)),
    KILL(Component.text("殺害する").decorate(TextDecoration.UNDERLINED)
            .clickEvent(ClickEvent.runCommand("/action kill")),
            Component.text("Who do you kill?").decorate(TextDecoration.BOLD)),
    PREDICT(Component.text("占う").decorate(TextDecoration.UNDERLINED)
            .clickEvent(ClickEvent.runCommand("/action predict")),
            Component.text("Who do you predict?").decorate(TextDecoration.BOLD)),
    SEE_DEAD(Component.text("死体を見る").decorate(TextDecoration.UNDERLINED)
            .clickEvent(ClickEvent.runCommand("/action see_dead")),
            Component.text("Who do you see?").decorate(TextDecoration.BOLD)),
    PROTECT(Component.text("守る").decorate(TextDecoration.UNDERLINED)
            .clickEvent(ClickEvent.runCommand("/action protect")),
            Component.text("Who do you protect?").decorate(TextDecoration.BOLD));

    private final Component text;
    private final Component inventory_title;

    Action(Component text, Component inventory_title){
        this.text = text;
        this.inventory_title = inventory_title;
    }

    public Component getText() {return text;}
    public Component getInventory_title() {return inventory_title;}
}
