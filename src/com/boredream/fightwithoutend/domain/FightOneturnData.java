
package com.boredream.fightwithoutend.domain;

import java.util.ArrayList;
import java.util.List;

public class FightOneturnData {

    private static final String TAG = "FightOneturnData";

    public static final int FIGHT_OUTCOME_HERO_IS_WIN = 110;
    public static final int FIGHT_OUTCOME_MONSTER_IS_WIN = 119;

    private Monster monster;
    private int fightOutcome;
    private int expGaint;
    private List<Treasure> dropTreasures = new ArrayList<Treasure>();
    public ArrayList<FightOneKickData> oneKickData = new ArrayList<FightOneKickData>();

    public FightOneturnData(Monster monster) {
        this.monster = monster;
    }

    public int getFightOutcome() {
        return fightOutcome;
    }

    public void setFightOutcome(int fightOutcome) {
        this.fightOutcome = fightOutcome;
    }

    public int getInfoCount() {
        return oneKickData.size();
    }

    public void setDropTreasures(List<Treasure> dropTreasures) {
        this.dropTreasures = dropTreasures;
    }

    public List<Treasure> getDropTreasures() {
        return dropTreasures;
    }

    /**
     * 获得的经验数.战斗胜利时会获得怪物所值经验;失败时不获取经验
     * 
     * @return 获得经验数量
     */
    public int getExpGaint() {
        if (fightOutcome == FIGHT_OUTCOME_HERO_IS_WIN) {
            expGaint = monster.getExpValue();
        } else {
            expGaint = 0;
        }
        return expGaint;
    }

}
