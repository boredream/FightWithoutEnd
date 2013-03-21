
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

    // 1攻击怪物, 2被怪物攻击, 3战斗结果
    public static int type = TYPE_H2M;

    public static int h2mHarm = 0;
    public static int m2hHarm = 0;
    public static boolean heroIsWin = false;

    public static FightOneturnData runOneTurn(Monster monster) {

        Log.i(TAG, "runOneTurn(" + monster + ")");

        type = TYPE_H2M;

        monster.initHp();
        hero.initHp();

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
                oneKickInfo = "->\"" + hero.getName() + "\"攻击了\"怪物:" + monster.getName() +
                                "\",造成了 " + h2mHarm + " 点伤害";
                monster.setHp(monster.getHp() - h2mHarm);

                if (monster.getHp() <= 0) {
                    type = TYPE_OVER;
                    oneKickInfo += "\n\"怪物:" + monster.getName()
                            + "\"的生命降为0,你胜利了!";
                    oneturnData.setFightOutcome(FightOneturnData.FIGHT_OUTCOME_HERO_IS_WIN);
                    oneturnData.addTreasureGaint(ProbabilityEventController.dropTreasure(monster));
                } else {
                    type = TYPE_M2H;
                }

                // new一个"一次击打信息"的bean
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
                oneKickInfo = "<-\"怪物:" + monster.getName() + "\"攻击了\"" + hero.getName() +
                                "\",造成了 " + m2hHarm + " 点伤害";
                hero.setHp(hero.getHp() - m2hHarm);

                if (hero.getHp() <= 0) {
                    type = TYPE_OVER;
                    oneKickInfo += "\n的生命降为0,\"" + hero.getName()
                            + "\"扑街!";
                    oneturnData.setFightOutcome(FightOneturnData.FIGHT_OUTCOME_MONSTER_IS_WIN);
                } else {
                    type = TYPE_H2M;
                }

                // new一个"一次击打信息"的bean
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
     * 经验提升
     * 
     * @param oneturnData 一轮战斗信息
     */
    public static void expRise(FightOneturnData oneturnData) {
        int expGaint = oneturnData.getExpGaint();
        hero.exp += expGaint;
        Log.i(TAG, "获得经验:" + expGaint);
        checkLevelRise();
    }

    /**
     * 检验当前经验是否足够升级
     */
    public static void checkLevelRise() {
        // 经验足够,升级
        if (hero.exp >= hero.currentLevelNeedExp()) {
            hero.exp -= hero.currentLevelNeedExp();
            hero.level++;
            Hero.MAX_HP += Hero.MAX_HP_RISE;
            hero.setAttackValue(hero.getAttackValue() + Hero.ATR_RISE);
            hero.setDefenseValue(hero.getDefenseValue() + Hero.DEF_RISE);
            Log.i(TAG, "升级! 等级:" + (hero.level - 1) + "->" + hero.level);
            Log.i(TAG, "升级! 血上限:" + (Hero.MAX_HP - Hero.MAX_HP_RISE)
                    + "->" + Hero.MAX_HP);
            Log.i(TAG, "升级! 攻击力:" + (hero.getAttackValue() - Hero.ATR_RISE)
                    + "->" + hero.getAttackValue());
            Log.i(TAG, "升级! 防御力:" + (hero.getDefenseValue() - Hero.DEF_RISE)
                    + "->" + hero.getDefenseValue());
        }
    }
}
