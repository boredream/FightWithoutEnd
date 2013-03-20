
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
     * ��þ���,ս��ʤ��ʱ���ù�����ֵ����;ʧ��ʱ����ȡ����
     * 
     * @return ��þ�������
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
     * ս��������,��ȡս��Ʒ
     * 
     * @param dropTreasures �����ս��Ʒ����
     */
    public void addTreasureGaint(List<Treasure> dropTreasures) {

        if (fightOutcome != FIGHT_OUTCOME_HERO_IS_WIN) {
            return;
        }

        if (dropTreasures.size() >= 2) {
            Log.i(TAG, "һ�ε���2�����ϵ���");
        }
        for (Treasure treasure : dropTreasures) {
            // ���Ӣ��ʤ����,���Ұ�����û���������,���ȡ
            if (!containsTreasures(treasureGaint, treasure)) {
                treasureGaint.add(treasure);
                Log.i(TAG, "��ȡһ������" + treasure.getName());
            }
        }

        // ȫ����ȡ(�ظ�װ����Ȼ��ȡ)
        // if (fightOutcome == FIGHT_OUTCOME_HERO_IS_WIN) {
        // this.treasureGaint.addAll(dropTreasures);
        // }
    }

    /**
     * �������Ƿ��Ѿ������˴˱���
     * 
     * @param treasureGaint �����ڵı���
     * @param dropTreasures ��Ҫ����Ƿ����е�Ŀ�걦��
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
