
package com.boredream.fightwithoutend.controller;

import com.boredream.fightwithoutend.domain.Monster;
import com.boredream.fightwithoutend.domain.Treasure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * �����¼�������
 * 
 * @author boredream
 */
public class ProbabilityEventController {

    private static Random random = new Random();

    private static int MAX_TREASURE_PROBABILITY = 100;

    /**
     * �����¼�����
     * 
     * @param currentAllMonsters ��ǰ�������������й���
     * @return �����ĸ�����
     */
    public static Monster encounterMonster(ArrayList<Monster> currentAllMonsters) {
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

    public static List<Treasure> dropTreasure(Monster monster) {
        List<Treasure> possibleDropTreasures = monster.getPossibleDropTreasure();
        List<Treasure> realDropTreasures = new ArrayList<Treasure>();
        for (Treasure treasure : possibleDropTreasures) {
            int dropProbability = treasure.getDropProbability();
            if (random.nextInt(MAX_TREASURE_PROBABILITY) + 1 <= dropProbability) {
                realDropTreasures.add(treasure);
            }
        }
        return realDropTreasures;
    }

}
