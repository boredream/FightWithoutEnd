
package com.boredream.fightwithoutend.controller;

import android.util.Log;

import com.boredream.fightwithoutend.domain.FightOneKickData;
import com.boredream.fightwithoutend.domain.FightOneturnData;
import com.boredream.fightwithoutend.domain.Hero;
import com.boredream.fightwithoutend.domain.Monster;

public class FightDataInfoController {
    private static final String TAG = "FightDataInfoController";

    public static Hero hero = Hero.initHero();

    public static final int TYPE_H2M = 1;
    public static final int TYPE_M2H = 2;
    public static final int TYPE_OVER = 3;

    // 1��������, 2�����﹥��, 3ս�����
    public static int type = TYPE_H2M;

    public static int h2mHarm = 0;
    public static int m2hHarm = 0;
    public static boolean heroIsWin = false;

    public static FightOneturnData runOneTurn(Monster monster) {

        Log.i(TAG, "runOneTurn(" + monster + ")");

        type = TYPE_H2M;

        monster.initHp();
        hero.setHp(20);

        FightOneturnData oneturnData = new FightOneturnData(monster);
        FightOneKickData oneKickData;
        String oneKickInfo = "";
        while (type != TYPE_OVER) {
            oneKickData = new FightOneKickData();
            if (type == TYPE_H2M) {
                h2mHarm = hero.getAttackValue() - monster.getDefenseValue();
                if (h2mHarm < 0) {
                    h2mHarm = 0;
                }
                oneKickInfo = "->\"" + hero.getName() + "\"������\"����:" + monster.getName() +
                                "\",����� " + h2mHarm + " ���˺�";
                monster.setHp(monster.getHp() - h2mHarm);

                if (monster.getHp() <= 0) {
                    type = TYPE_OVER;
                    oneKickInfo += "\n\"����:" + monster.getName()
                            + "\"��������Ϊ0,��ʤ����!";
                    oneturnData.setFightOutcome(FightOneturnData.FIGHT_OUTCOME_HERO_IS_WIN);
                    oneturnData.addTreasureGaint(ProbabilityEventController.dropTreasure(monster));
                } else {
                    type = TYPE_M2H;
                }

                Log.i(TAG, "��ս'" + monster.getName() + "'һ��ս����Ϣ -------------------- "
                        + oneKickInfo);

                // newһ��"һ�λ�����Ϣ"��bean
                oneKickData.setDescribe(oneKickInfo);
                oneKickData.setHarmValue(h2mHarm);
                oneKickData.setHarmType(FightOneKickData.H2M);
                oneKickData.setHeroCurrentHp(hero.getHp());
                oneKickData.setMonsterCurrentHp(monster.getHp());

            } else if (type == TYPE_M2H) {
                m2hHarm = monster.getAttackValue() - hero.getDefenseValue();
                if (m2hHarm < 0) {
                    m2hHarm = 0;
                }
                oneKickInfo = "<-\"����:" + monster.getName() + "\"������\"" + hero.getName() +
                                "\",����� " + m2hHarm + " ���˺�";
                hero.setHp(hero.getHp() - m2hHarm);

                if (hero.getHp() <= 0) {
                    type = TYPE_OVER;
                    oneKickInfo += "\n��������Ϊ0,\"" + hero.getName()
                            + "\"�˽�!";
                    oneturnData.setFightOutcome(FightOneturnData.FIGHT_OUTCOME_MONSTER_IS_WIN);
                } else {
                    type = TYPE_H2M;
                }

                Log.i(TAG, "��ս'" + monster.getName() + "'һ��ս����Ϣ ---------- "
                        + oneKickInfo);

                // newһ��"һ�λ�����Ϣ"��bean
                oneKickData.setDescribe(oneKickInfo);
                oneKickData.setHarmValue(m2hHarm);
                oneKickData.setHarmType(FightOneKickData.M2H);
                oneKickData.setHeroCurrentHp(hero.getHp());
                oneKickData.setMonsterCurrentHp(monster.getHp());
            }
            oneturnData.oneKickData.add(oneKickData);
        }
        expRise(oneturnData);
        return oneturnData;
    }

    /**
     * ��������
     * 
     * @param oneturnData һ��ս����Ϣ
     */
    public static void expRise(FightOneturnData oneturnData) {
        int expGaint = oneturnData.getExpGaint();
        hero.exp += expGaint;
        Log.i(TAG, "��þ���:" + expGaint);
        checkLevelRise();
    }

    /**
     * ���鵱ǰ�����Ƿ��㹻����
     */
    public static void checkLevelRise() {
        // �����㹻,����
        if (hero.exp >= hero.currentLevelNeedExp()) {
            hero.exp -= hero.currentLevelNeedExp();
            hero.level++;
            hero.setAttackValue(hero.getAttackValue() + Hero.ATR_RISE);
            hero.setDefenseValue(hero.getDefenseValue() + Hero.DEF_RISE);
            Log.i(TAG, "����! �ȼ�:" + (hero.level - 1) + "->" + hero.level);
            Log.i(TAG, "����! ����:" + (hero.getAttackValue() - Hero.ATR_RISE)
                    + "->" + hero.getAttackValue());
            Log.i(TAG, "����! ����:" + (hero.getDefenseValue() - Hero.DEF_RISE)
                    + "->" + hero.getDefenseValue());
        }
    }
}
