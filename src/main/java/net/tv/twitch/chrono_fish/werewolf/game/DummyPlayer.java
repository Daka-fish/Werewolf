package net.tv.twitch.chrono_fish.werewolf.game;

import net.tv.twitch.chrono_fish.werewolf.Main;
import net.tv.twitch.chrono_fish.werewolf.instance.Role;

import java.util.ArrayList;
import java.util.Collections;

public class DummyPlayer extends GamePlayer{

    private final Main main;
    private final Game game;
    private final String name;

    public DummyPlayer(Game game, String name) {
        super();
        this.game = game;
        this.name = name;
        this.main = game.getMain();
    }

    @Override
    public String getName() {return name;}

    public void action(){
        if(!isAlive()) return;
        switch (this.getRole()){
            case WOLF:
                this.addKillPool();
                break;
        }
    }

    public void vote() {
        if(!this.isAlive()) return;
        ArrayList<GamePlayer> alivePlayers = new ArrayList<>();
        for (GamePlayer participant : game.getAlivePlayers()) {
            if(!participant.equals(this)){
                alivePlayers.add(participant);
            }
        }
        Collections.shuffle(alivePlayers);
        GamePlayer target = alivePlayers.get(0);
        target.setVoteCount(target.getVoteCount()+1);
        main.consoleLog("Day "+game.getDayCount()+": "+name+" vote to "+target.getName());
        super.setHasVoted(true);
    }

    public void addKillPool() {
        setActionTarget(null);
        ArrayList<GamePlayer> pool = new ArrayList<>();
        GamePlayer target = null;
        for (GamePlayer participant : game.getAlivePlayers()) {
            if(!participant.equals(this) && !participant.getRole().equals(Role.WOLF)){
                pool.add(participant);
            }
        }
        Collections.shuffle(pool);
        if(pool.size()>0){
            target = pool.get(0);
            setActionTarget(target);
        }
        main.consoleLog("Day "+game.getDayCount()+": "+name+"'s target is "+((target != null) ? target.getName() : "---"));
        setHasActioned(true);
    }
}
