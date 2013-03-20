
package com.boredream.fightwithoutend.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FightOneturnData {

    private static final String TAG = "FightOneturnData";

    public static final int FIGHT_OUTCOME_HERO_IS_WIN = 110;
    public static final int FIGHT_OUTCOME_MONSTER_IS_WIN = 119;

    private Monster monster;
    private int fightOutcome;
    private int expGaint;
    private List<Treasure> treasureGaint = new ArrayList<Treasure>();
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

    /**
     * 获得经验,战斗胜利时会获得怪物所值经验;失败时不获取经验
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

    /**
     * 战斗结束后,捡取战利品
     * 
     * @param dropTreasures 掉落的战利品宝物
     */
    public void addTreasureGaint(List<Treasure> dropTreasures) {

        if (fightOutcome != FIGHT_OUTCOME_HERO_IS_WIN) {
            return;
        }

        if (dropTreasures.size() >= 2) {
            Log.i(TAG, "一次掉落2个以上道具");
        }
        for (Treasure treasure : dropTreasures) {
            // 如果英雄胜利了,并且包袱里没有这个宝物,则捡取
            if (!containsTreasures(treasureGaint, treasure)) {
                treasureGaint.add(treasure);
                Log.i(TAG, "捡取一个道具" + treasure.getName());
            }
        }

        // 全部捡取(重复装备仍然捡取)
        // if (fightOutcome == FIGHT_OUTCOME_HERO_IS_WIN) {
        // this.treasureGaint.addAll(dropTreasures);
        // }
    }

    /**
     * 包袱中是否已经包含了此宝物
     * 
     * @param treasureGaint 包袱内的宝物
     * @param dropTreasures 需要检测是否已有的目标宝物
     * @return
     */
    private boolean containsTreasures(List<Treasure> treasureGaint, Treasure targetTreasures) {

        for (Treasure treasure : treasureGaint) {
            if (treasure.getId() == targetTreasures.getId()) {
                return true;
            }
        }
        return false;
    }

    public List<Treasure> getTreasureGaint() {
        return treasureGaint;
    }

}
