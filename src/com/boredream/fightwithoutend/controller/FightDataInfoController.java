
package com.boredream.fightwithoutend.controller;

import android.util.Log;

import com.boredream.fightwithoutend.domain.FightOneKickData;
import com.boredream.fightwithoutend.domain.FightOneturnData;
import com.boredream.fightwithoutend.domain.Hero;
import com.boredream.fightwithoutend.domain.Monster;
import com.boredream.fightwithoutend.domain.Skill;
import com.boredream.fightwithoutend.domain.Treasure;

import java.util.List;

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
                if (ProbabilityEventController.triggerSkill(hero.getCurrentAttSkill())) {
                    if (hero.getCurrentAttSkill().getSkillEffect() == Skill.SE_ATT_HARM_ADDITION) {
                        h2mHarm *= hero.getCurrentAttSkill().getHarmAdditionValue();
                    }
                    Log.i(TAG, "触发了倍击,造成 " + h2mHarm + "点伤害(" +
                            hero.getCurrentAttSkill().getHarmAdditionValue() + "倍)");
                }
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
                    List<Treasure> dropTreasures = ProbabilityEventController.dropTreasure(monster);
                    oneturnData.setDropTreasures(dropTreasures);
                    pickTreasures(dropTreasures);
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

    public static void equip(Treasure treasure) {
        if (treasure.getEquipLocation() == Treasure.EQUIP_LOCATION_WEAPON) {
            // 当前装备非空时,先将当前装备移回物品栏,把当前装备清空
            if (hero.currentWeapon != null) {
                Log.i(TAG, "equip(" + treasure + ") -- 卸下武器");
                hero.totalObtainTreasure.add(hero.currentWeapon);
                hero.currentWeapon = null;
            }
            // 再将新的装备换上,而当前装备为空时,则直接跳过以上过程
            hero.totalObtainTreasure.remove(treasure);
            hero.currentWeapon = treasure;
            Log.i(TAG, "equip(" + treasure + ") -- 装备上武器");
        } else {
            // 当前装备非空时,先将当前装备移回物品栏,把当前装备清空
            if (hero.currentArmor != null) {
                Log.i(TAG, "equip(" + treasure + ") -- 卸下防具");
                hero.totalObtainTreasure.add(hero.currentArmor);
                hero.currentArmor = null;
            }
            // 再将新的装备换上,而当前装备为空时,则直接跳过以上过程
            hero.totalObtainTreasure.remove(treasure);
            hero.currentArmor = treasure;
            Log.i(TAG, "equip(" + treasure + ") -- 装备上防具");
        }
    }

    /**
     * 提升英雄某个技能的等级
     * 
     * @param skill 需要提升的技能
     * @return 提升后的技能等级
     */
    public static int riseHeroSkill(Skill skill) {
        int skillLevel = skill.getLevel();
        if (hero.sp >= skill.getSp4rise()) {
            hero.sp -= skill.getSp4rise();
            skill.setLevel(skillLevel + 1);
        }
        return skillLevel;
    }

    /**
     * 装备强化加星数据处理方法
     * 
     * @param treasure
     * @return
     */
    public static int riseTreasureStar(Treasure treasure) {

        int riseStarResult = ProbabilityEventController.riseTreasureStar(treasure);
        switch (riseStarResult) {
            case Treasure.RISE_STAR_SUCCESS:
                treasure.setStar(treasure.getStar() + 1);
                break;
            case Treasure.RISE_STAR_BREAK:
                hero.totalObtainTreasure.remove(treasure);
                break;
            case Treasure.RISE_STAR_NO_CHANGE:
                // nothing
                Log.i(TAG, "rise star - no change");
                break;

            default:
                break;
        }

        return riseStarResult;
    }

    /**
     * 捡取宝物
     * 
     * @param dropTreasure
     */
    private static void pickTreasures(List<Treasure> dropTreasures) {

        // 包袱里东西到50时,不再捡取
        if (hero.totalObtainTreasure != null
                && hero.totalObtainTreasure.size() > Hero.MAX_GOODS_COUNT) {
            return;
        }

        if (!heroIsWin) {
            return;
        }

        if (dropTreasures.size() >= 2) {
            Log.i(TAG, "一次掉落2个以上道具");
        }
        for (Treasure treasure : dropTreasures) {
            // 如果英雄胜利了,并且包袱里没有这个宝物,则捡取
            if (!containsTreasures(hero.totalObtainTreasure, treasure)) {
                hero.totalObtainTreasure.add(treasure);
                Log.i(TAG, "捡取道具:" + treasure.getName());
            }
        }

    }

    /**
     * 包袱中是否已经包含了此宝物
     * 
     * @param treasureGaint 包袱内的宝物
     * @param dropTreasures 需要检测是否已有的目标宝物
     * @return
     */
    private static boolean containsTreasures(List<Treasure> treasureGaint, Treasure targetTreasures) {

        for (Treasure treasure : treasureGaint) {
            if (treasure.getId() == targetTreasures.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 经验提升
     * 
     * @param oneturnData 一轮战斗信息
     */
    private static void expRise(FightOneturnData oneturnData) {
        int expGaint = oneturnData.getExpGaint();
        hero.exp += expGaint;
        Log.i(TAG, "获得经验:" + expGaint);
        checkLevelRise();
    }

    /**
     * 检验当前经验是否足够升级
     */
    private static void checkLevelRise() {
        // 经验足够,升级
        if (hero.exp >= hero.currentLevelNeedExp()) {
            hero.exp -= hero.currentLevelNeedExp();
            hero.level++;
            hero.sp += Hero.SP_RISE;
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
