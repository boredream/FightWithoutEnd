
package com.boredream.fightwithoutend.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 怪物
 * 
 * @author boredream
 *
 */
public class Monster {

    private int id;
    private String name;
    private int hp;
    private int attackValue;
    private int defenseValue;
    private int expValue;
    private int encounterProbability;
    private List<Treasure> possibleDropTreasure;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    public int getExpValue() {
        return expValue;
    }

    public void setExpValue(int expValue) {
        this.expValue = expValue;
    }

    public int getEncounterProbability() {
        return encounterProbability;
    }

    public void setEncounterProbability(int encounterProbability) {
        this.encounterProbability = encounterProbability;
    }

    public List<Treasure> getPossibleDropTreasure() {
        return possibleDropTreasure;
    }

    public Monster(int id, String name, int hp, int attackValue, int defenseValue, int expValue,
            int encounterProbability, List<Treasure> possibleDropTreasure) {
        super();
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
        this.expValue = expValue;
        this.encounterProbability = encounterProbability;
        this.possibleDropTreasure = possibleDropTreasure;
    }

    @Override
    public String toString() {
        return "Monster [id=" + id + ", name=" + name + ", hp=" + hp + ", attackValue="
                + attackValue + ", defenseValue=" + defenseValue + ", expValue=" + expValue
                + ", encounterProbability=" + encounterProbability + "]";
    }

    public static ArrayList<Monster> getMonsters() {
        ArrayList<Monster> monsters = new ArrayList<Monster>();

        List<Treasure> possibleDropTreasures1 = new ArrayList<Treasure>();
        possibleDropTreasures1.add(Treasure.findTreasureById(1));
        Monster monsterHaigui = new Monster(1, "大海龟", 20, 5, 5, 10, 50, possibleDropTreasures1);
        monsters.add(monsterHaigui);

        List<Treasure> possibleDropTreasures2 = new ArrayList<Treasure>();
        possibleDropTreasures2.add(Treasure.findTreasureById(2));
        possibleDropTreasures2.add(Treasure.findTreasureById(3));
        Monster monsterJuwa = new Monster(2, "巨蛙", 25, 10, 7, 18, 35, possibleDropTreasures2);
        monsters.add(monsterJuwa);

        List<Treasure> possibleDropTreasures3 = new ArrayList<Treasure>();
        possibleDropTreasures3.add(Treasure.findTreasureById(4));
        possibleDropTreasures3.add(Treasure.findTreasureById(5));
        Monster monsterHaimaochong = new Monster(3, "海毛虫", 50, 20, 10, 30, 15,
                possibleDropTreasures3);
        monsters.add(monsterHaimaochong);
        return monsters;
    }

    public void initHp() {
        switch (this.getId()) {
            case 1:
                this.setHp(10);
                break;
            case 2:
                this.setHp(15);
                break;
            case 3:
                this.setHp(30);
                break;

            default:
                break;
        }
    }

    public void monstersExps() {
        // 东海湾 0级 414
        //
        // 沉船1层 2级 724
        // 沉船2层 4级 931
        //
        // 东海岩洞 4级 931
        // 江南野外 6级 1138
        //
        // 大雁塔1层 8级 1345
        // 大雁塔2层 12级 1759
        // 大雁塔3层 16级 2173
        // 大雁塔4层 20级 2587
        // 大雁塔5层 24级 3001
        // 大雁塔6层 28级 3415
        //
        // 大唐国境 11级 1656
        // 大唐境外 20级 2587
    }

}
