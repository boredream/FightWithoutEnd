
package com.boredream.fightwithoutend.domain;

public class FightOneKickData {

    public static final int DEFAULT = -1;
    public static final int H2M = 1;
    public static final int M2H = 2;

    private String describe;
    private int harmValue;
    private int harmType;
    private int heroCurrentHp;
    private int MonsterCurrentHp;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getHarmValue() {
        return harmValue;
    }

    public void setHarmValue(int harmValue) {
        this.harmValue = harmValue;
    }

    public int getHarmType() {
        return harmType;
    }

    public void setHarmType(int harmType) {
        this.harmType = harmType;
    }

    public int getHeroCurrentHp() {
        if (heroCurrentHp < 0) {
            heroCurrentHp = 0;
        }
        return heroCurrentHp;
    }

    public void setHeroCurrentHp(int heroCurrentHp) {
        this.heroCurrentHp = heroCurrentHp;
    }

    public int getMonsterCurrentHp() {
        return MonsterCurrentHp;
    }

    public void setMonsterCurrentHp(int monsterCurrentHp) {
        MonsterCurrentHp = monsterCurrentHp;
    }

}
