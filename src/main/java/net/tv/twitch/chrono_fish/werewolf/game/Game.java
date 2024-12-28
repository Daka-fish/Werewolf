package net.tv.twitch.chrono_fish.werewolf.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.werewolf.Main;
import net.tv.twitch.chrono_fish.werewolf.instance.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private final Main main;
    private Timer timer;
    private final CustomInventory customInventory;

    private boolean isRunning;
    private final ArrayList<GamePlayer> participants;
    private final ArrayList<DummyPlayer> dummyPlayers;

    private ArrayList<Role> roles;

    private int dayCount;
    private TimeZone currentTime;

    private final GameBossBar gameBossBar;
    private final GameScoreboard gameScoreboard;

    public Game(Main main){
        this.main = main;
        this.customInventory = new CustomInventory(this);
        this.isRunning = false;
        this.participants = new ArrayList<>();
        this.dummyPlayers = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.dayCount = 0;
        this.currentTime = TimeZone.WAITING;

        gameBossBar = new GameBossBar();
        gameScoreboard = new GameScoreboard();
        gameBossBar.setBossBar(currentTime);
    }

    public CustomInventory getCustomInventory() {return customInventory;}

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean running) {
        isRunning = running;
        if(isRunning){
            sendMessage("ゲームが開始しました");
        }else{
            sendMessage("ゲームが終了しました");
        }
    }

    public ArrayList<GamePlayer> getParticipants() {return participants;}
    public ArrayList<DummyPlayer> getDummyPlayers() {return dummyPlayers;}

    public ArrayList<Role> getRoles() {return roles;}
    public void setRoles(ArrayList<Role> roles) {this.roles = roles;}

    public int getDayCount() {return dayCount;}
    public void setDayCount(int dayCount) {
        if(isRunning){
            this.dayCount = dayCount;
            sendMessage("§e"+dayCount+"§f日目になりました");
        }
    }

    public TimeZone getCurrentTime() {return currentTime;}
    public void setCurrentTime(TimeZone currentTime) {
        if(isRunning){
            this.currentTime = currentTime;
            if(currentTime.equals(TimeZone.VOTE)){
                sendMessage("§e"+currentTime.getTimeName()+"の時間§fになりました。チャットをクリックして投票して下さい");
                return;
            }
            sendMessage("§e"+currentTime.getTimeName()+"§fになりました");
        }
    }

    public GameBossBar getGameBossBar() {return gameBossBar;}

    public void join(GamePlayer gamePlayer){
        participants.add(gamePlayer);
        sendMessage("§e"+gamePlayer.getName()+"§fが参加しました");
    }

    public void leave(GamePlayer gamePlayer){
        participants.remove(gamePlayer);
        gameBossBar.hideBossBar(gamePlayer);
        sendMessage("§e"+gamePlayer.getName()+"§fが退室しました");
    }

    public void addCpu(){
        DummyPlayer cpu = new DummyPlayer(this,"nano"+(dummyPlayers.size()+1));
        participants.add(cpu);
        dummyPlayers.add(cpu);
        sendMessage("§e"+cpu.getName()+"§fが参加しました(cpu)");
    }

    public void removeCpu(){
        if(dummyPlayers.size()>0){
            DummyPlayer cpu = dummyPlayers.get(dummyPlayers.size()-1);
            participants.remove(cpu);
            dummyPlayers.remove(cpu);
            sendMessage("§e"+cpu.getName()+"§fを削除しました");
        }
    }

    public void addRole(Role role){
        roles.add(role);
        Collections.sort(roles);
    }

    public void removeRole(Role role){
        roles.remove(role);
        Collections.sort(roles);
    }

    public void sendMessage(String message){participants.forEach(gamePlayer -> gamePlayer.sendMessage("[人狼]"+message));}

    public GamePlayer getGamePlayer(Player player){
        for (GamePlayer participant : participants) {
            if(participant.getPlayer() != null && participant.getPlayer().equals(player)) return participant;
        }
        return null;
    }

    public boolean isJoined(Player player){
        for (GamePlayer participant : participants) {
            if(participant.getPlayer().equals(player)) return true;
        }
        return false;
    }

    public int countTeam(int teamNumber){
        int count=0;
        for (GamePlayer participant : participants) {
            if(participant.getRole().getTeam() == teamNumber && participant.isAlive()){
                count ++;
            }
        }
        return count;
    }

    public int countRole(Role role){
        int count = 0;
        for (Role existRole : roles) {
            if(existRole == role) count ++;
        }
        return count;
    }

    public void start(GamePlayer gamePlayer){
        if(!isRunning){
            if(participants.size()<3){
                sendMessage("§c参加者が少ないためゲームを開始できませんでした(あと§e"+(3-participants.size())+"§c人)");
                return;
            }
            dayCount = 0;
            setRunning(true);
            setCurrentTime(TimeZone.NIGHT);
            assignRole();

            participants.forEach(participant -> {
                participant.setVoteCount(0);
                participant.setHasVoted(false);
                participant.setHasActioned(false);
                participant.setAlive(true);
                gameBossBar.setBossBar(currentTime);
                gameBossBar.showBossBar(participant);
                gameScoreboard.addBoard(participant);
                gameScoreboard.resetRole(participant);
                gameScoreboard.show(participant);
            });

            timer = new Timer(this);
            timer.setTime(currentTime.getTime());
            timer.runTaskTimer(main,0,20);
        }else{
            gamePlayer.sendMessage("§c既にゲームが進行中のためゲームを開始できませんでした");
        }
    }

    public void finish(){
        setRunning(false);
        setCurrentTime(TimeZone.WAITING);
        timer.cancel();
        StringBuilder message = new StringBuilder();
        for (GamePlayer participant : participants) {
            gameBossBar.setBossBar(TimeZone.WAITING);
            gameBossBar.setTime((float) 1);
            gameBossBar.hideBossBar(participant);
            gameScoreboard.remove(participant);
            message.append("\n").append(participant.getName()).append(" : ").append(participant.getRole().getRoleName());
        }
        sendMessage("役職一覧"+ message);
    }

    public void checkWin(){
        int white = countTeam(0);
        int black = countTeam(1);
        if(black>=white){
            finish();
            return;
        }
        if(black==0){
            finish();
        }
    }

    public void sendActionMessage(GamePlayer gamePlayer){
        if(!gamePlayer.isAlive()) return;
        switch(gamePlayer.getRole()){
            case WOLF:
                gamePlayer.sendMessage(Component.text("§e"+getDayCount()+"§f日目: ").append(Action.KILL.getText()));
                break;

            case SEER:
                gamePlayer.sendMessage(Component.text("§e"+getDayCount()+"§f日目: ").append(Action.PREDICT.getText()));
                break;

            case MEDIUM:
                gamePlayer.sendMessage(Component.text("§e"+getDayCount()+"§f日目: ").append(Action.SEE_DEAD.getText()));
                break;

            case KNIGHT:
                gamePlayer.sendMessage(Component.text("§e"+getDayCount()+"§f日目: ").append(Action.PROTECT.getText()));
                break;
        }
    }

    public void doTimeEnd(){
        switch (currentTime){
            case DAY:
                setCurrentTime(TimeZone.VOTE);
                participants.forEach(gamePlayer -> {
                    if(gamePlayer.isAlive()) gamePlayer.sendMessage(Component.text("§e"+getDayCount()+"§f日目: ").append(Action.VOTE.getText()));
                });
                dummyPlayers.forEach(DummyPlayer::vote);
                break;

            case VOTE:
                kickMostVoted();
                checkWin();
                if(isRunning){
                    setCurrentTime(TimeZone.NIGHT);
                    participants.forEach(this::sendActionMessage);
                }
                break;

            case NIGHT:
                dummyPlayers.forEach(DummyPlayer::action);
                if(getDayCount()!=0){
                    kill();
                }
                checkWin();
                setCurrentTime(TimeZone.DAY);
                setDayCount(getDayCount()+1);

                participants.forEach(gamePlayer -> {
                    gamePlayer.setHasVoted(false);
                    gamePlayer.setVoteCount(0);
                    gamePlayer.setHasActioned(false);
                    gamePlayer.setProtected(false);
                });
                break;
        }
        if(isRunning){
            gameBossBar.setBossBar(currentTime);
            timer = new Timer(this);
            timer.setTime(currentTime.getTime());
            timer.runTaskTimer(main,0,20);
        }
    }

    public void assignRole(){
        Collections.shuffle(roles);
        for(int i=0; i< participants.size(); i++){
            if(i< roles.size()){
                participants.get(i).setRole(roles.get(i));
            }else{
                participants.get(i).setRole(Role.INNOCENT);
            }
        }
    }

    public void kickMostVoted(){
        GamePlayer target = null;
        int maxVoted = 0;
        for (GamePlayer participant : participants) {
            if(participant.getVoteCount()>maxVoted) target = participant;
        }
        if(target != null){
            target.setAlive(false);
            sendMessage("§e"+target.getName()+"§fが追放された");
        }
    }

    public void kill(){
        ArrayList<GamePlayer> targets = new ArrayList<>();
        for (GamePlayer participant : participants) {
            if(participant.getRole().equals(Role.WOLF)){
                targets.add(participant.getActionTarget());
            }
        }
        Collections.shuffle(targets);
        GamePlayer gamePlayer = targets.get(0);
        if(gamePlayer!=null && !gamePlayer.isProtected()){
            gamePlayer.setAlive(false);
            sendMessage("§e"+gamePlayer.getName()+"§cが何者かによって殺害された");
        }else{
            sendMessage("§a昨晩の犠牲者はいなかった");
        }
    }
}
