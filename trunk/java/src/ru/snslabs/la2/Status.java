package ru.snslabs.la2;

import java.math.BigDecimal;

/**
 * Status of player (under atack, MP/hp, etc...)
 */
public class Status {

    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    private BigDecimal expPercent;
    private String targetName;
    private boolean targetAlive;
    private BigDecimal targetHp;

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public String toString() {
        return "HP: " + hp + "/" + maxHp + " " +
                "MP: " + mp + "/" + maxMp + " " +
                "EX: " + expPercent + "% " +
                "TARGET: " + targetName+" "+
                "ALIVE: " + targetAlive+" "+
                "TARGET HP%: "+ targetHp+"% ";
        
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public void setExpPercent(BigDecimal expPercent) {
        this.expPercent = expPercent;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setTargetAlive(boolean b) {
        this.targetAlive = b;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMp() {
        return mp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public BigDecimal getExpPercent() {
        return expPercent;
    }

    public String getTargetName() {
        return targetName;
    }

    public boolean isTargetAlive() {
        return targetAlive;
    }

    public BigDecimal getTargetHp() {
        return targetHp;
    }

    public void setTargetHp(BigDecimal targetHp) {
        this.targetHp = targetHp;
    }
}
