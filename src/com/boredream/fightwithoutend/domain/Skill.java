
package com.boredream.fightwithoutend.domain;

import java.util.ArrayList;
import java.util.List;

public class Skill {

    public static final int TYPE_ATTRACT = 11;
    public static final int TYPE_DEFENSE = 12;

    private int id;
    private String name;
    private int type;
    private int occurProbability; // 触发几率,百分比数值控制触发
    private int level;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getOccurProbability() {
        return occurProbability;
    }

    public void setOccurProbability(int occurProbability) {
        this.occurProbability = occurProbability;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Skill(int id, String name, int type, int occurProbability) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.occurProbability = occurProbability;
        this.level = 1;
    }

    /**
     * 获取每一级所需sp技能点
     * <p>
     * 根据当前不同等级返回各等级所需技能点
     * </p>
     * 
     * @return 需要sp点
     */
    public int getSp4rise() {
        int sp4rise = 2;
        switch (level) {
            case 1:
                sp4rise = 2;
                break;
            case 2:
                sp4rise = 3;
                break;
            case 3:
                sp4rise = 4;
                break;
            case 4:
                sp4rise = 6;
                break;
            case 5:
                sp4rise = 8;
                break;
            case 6:
                sp4rise = 10;
                break;
            case 7:
                sp4rise = 14;
                break;
            case 8:
                sp4rise = 20;
                break;
        }

        return sp4rise;
    }

    public static List<Skill> getAllSkills() {
        List<Skill> allSkills = new ArrayList<Skill>();
        Skill skill1 = new Skill(1, "重击", TYPE_ATTRACT, 20);
        Skill skill2 = new Skill(2, "格挡", TYPE_DEFENSE, 10);
        allSkills.add(skill1);
        allSkills.add(skill2);
        return allSkills;
    }

}
