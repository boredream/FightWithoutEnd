
package com.boredream.fightwithoutend.controller;

import com.boredream.fightwithoutend.domain.Monster;
import com.boredream.fightwithoutend.domain.Skill;
import com.boredream.fightwithoutend.domain.Treasure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 概率事件控制器
 * 
 * @author boredream
 */
public class ProbabilityEventController {

    private static Random random = new Random();

    private static int MAX_PROBABILITY = 100;

    /**
     * 遇敌事件概率控制
     * 
     * @param currentAllMonsters 当前可以遇到的所有怪物
     * @return 遇到哪个怪物
     */
    public static Monster encounterNewMonster(ArrayList<Monster> currentAllMonsters) {
        int totalEncounterPros = 0;
        List<Monster> totalMons = new ArrayList<Monster>();
        for (Monster monster : currentAllMonsters) {
            for (int i = 0; i < monster.getEncounterProbability(); i++) {
                totalMons.add(monster);
            }
            totalEncounterPros += monster.getEncounterProbability();
        }
        return totalMons.get(random.nextInt(totalMons.size()));
    }

    /**
     * 宝物掉落事件概率控制
     * 
     * @param monster 打死的怪物
     * @return 爆出的宝物集合
     */
    public static List<Treasure> dropTreasure(Monster monster) {
        List<Treasure> possibleDropTreasures = monster.getPossibleDropTreasure();
        List<Treasure> realDropTreasures = new ArrayList<Treasure>();
        for (Treasure treasure : possibleDropTreasures) {
            int dropProbability = treasure.getDropProbability();
            if (random.nextInt(MAX_PROBABILITY) + 1 <= dropProbability) {
                realDropTreasures.add(treasure);
            }
        }
        return realDropTreasures;
    }

    /**
     * 触发技能事件概率控制
     * 
     * @return true为触发
     */
    public static boolean triggerSkill(Skill skill) {
        if (random.nextInt(MAX_PROBABILITY) + 1 <= skill.getOccurProbability()) {
            return true;
        }
        return false;
    }

    /**
     * 装备加星事件概率控制
     * 
     * @param treasure
     * @return RISE_STAR_SUCCESS 成功; ;
     * @return RISE_STAR_BREAK 碎了一地(star>=4时会碎)
     * @return RISE_STAR_NO_CHANGE 无变化
     */
    public static int riseTreasureStar(Treasure treasure) {
        int result = Treasure.RISE_STAR_NO_CHANGE;
        int starRiseProbility = treasure.getStarRiseProbility();
        if (random.nextInt(MAX_PROBABILITY) + 1 <= starRiseProbility) {
            result = Treasure.RISE_STAR_SUCCESS;
        } else {
            if (treasure.getStar() < 4) {
                result = Treasure.RISE_STAR_NO_CHANGE;
            } else {
                result = Treasure.RISE_STAR_BREAK;
            }
        }
        return result;
    }

}
