
package com.boredream.fightwithoutend.test;

import com.boredream.fightwithoutend.controller.ProbabilityEventController;
import com.boredream.fightwithoutend.domain.Monster;
import com.boredream.fightwithoutend.domain.Treasure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void dropTreasuresTest() {
        int totalTestCount = 1000;
        String noneCount = "毛都没有";
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> dropTreasures = new HashMap<String, Integer>();
        for (int i = 0; i < totalTestCount; i++) {
            List<Treasure> treasures = ProbabilityEventController.dropTreasure(Monster
                    .getMonsters().get(1));
            if (treasures.size() != 0) {
                for (Treasure treasure : treasures) {
                    if (dropTreasures.containsKey(treasure.getName())) {
                        int count = dropTreasures.get(treasure.getName());
                        dropTreasures.put(treasure.getName(), ++count);
                    } else {
                        dropTreasures.put(treasure.getName(), 1);
                    }
                }
            } else {
                if (dropTreasures.containsKey(noneCount)) {
                    int count = dropTreasures.get(noneCount);
                    dropTreasures.put(noneCount, ++count);
                } else {
                    dropTreasures.put(noneCount, 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : dropTreasures.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            String info = "获得" + key + ":" + value + "个;";
            if (!key.equals(noneCount)) {
                sb.insert(0, info);
            } else {
                sb.append(info);
            }
        }
        sb.insert(0, "共战斗了" + totalTestCount + "轮。");
        System.out.println(sb.toString());
    }
}
