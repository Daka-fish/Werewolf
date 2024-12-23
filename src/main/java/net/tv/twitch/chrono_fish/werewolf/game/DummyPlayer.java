package net.tv.twitch.chrono_fish.werewolf.game;

import net.tv.twitch.chrono_fish.werewolf.instance.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DummyPlayer extends GamePlayer{

    private final Game game;
    private final String name;

    public DummyPlayer(Game game, String name) {
        super();
        this.game = game;
        this.name = name;
    }

    @Override
    public String getName() {return name;}

    public void vote() {
        if(!this.isAlive()) return;
        ArrayList<GamePlayer> alivePlayers = new ArrayList<>();
        for (GamePlayer participant : game.getParticipants()) {
            if(participant.isAlive()){
                alivePlayers.add(participant);
            }
        }
        Random rand = new Random();
        GamePlayer target = alivePlayers.get(rand.nextInt(alivePlayers.size()));
        target.setVoteCount(target.getVoteCount()+1);
        super.setHasVoted(true);
    }

    public void action(){
        for (DummyPlayer dummyPlayer : game.getDummyPlayers()) {
            if(!dummyPlayer.isAlive()) return;
            if(dummyPlayer.getRole().equals(Role.WOLF)){
                ArrayList<GamePlayer> targetPool = new ArrayList<>();
                for (GamePlayer participant : game.getParticipants()) {
                    if(participant.isAlive() && !participant.getRole().equals(Role.WOLF)){
                        targetPool.add(participant);
                    }
                }
                Collections.shuffle(targetPool);
                GamePlayer target = targetPool.get(0);
                setActionTarget(target);
            }
            if(dummyPlayer.getRole().equals(Role.SEER)){
                ArrayList<GamePlayer> targetPool = new ArrayList<>();
                for (GamePlayer participant : game.getParticipants()) {
                    if(participant.isAlive()){
                        targetPool.add(participant);
                    }
                }
                Collections.shuffle(targetPool);
                GamePlayer target = targetPool.get(0);
                setActionTarget(target);
            }
            setHasActioned(true);
        }
    }
}
