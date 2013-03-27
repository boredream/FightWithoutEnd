
package com.boredream.fightwithoutend.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 技能
 * 
 * @author boredream
 *
 */
public class Skill implements Serializable {

    // 技能总类别Type
    public static final int TYPE_ATTRACT = 11;
    public static final int TYPE_DEFENSE = 12;

    // 技能具体效果SkillEffect(SE)
    public static final int SE_ATT_HARM_ADDITION = 101; // 伤害加成
    public static final int SE_DEF_BLOCK = 201; // 格挡

    private int id;
    private String name;
    private int type;
    private int occurProbability; // 触发几率,百分比数值控制触发
    private int level;
    private int skillEffect; // 技能具体效果

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

    public int getSkillEffect() {
        return skillEffect;
    }

    /**
     * 获得攻击加成指数
     * 
     * @return
     */
    public int getHarmAdditionValue() {
        if (id == 1) { // 倍击
            // 例如:重击等级为3,则伤害加成为 1.0 + 3*1 = 4倍
            return 1 + level;
        }
        return 1;
    }

    public Skill(int id, String name, int type, int skillEffect, int occurProbability) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.occurProbability = occurProbability;
        this.skillEffect = skillEffect;
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
        Skill skill1 = new Skill(1, "倍击", TYPE_ATTRACT, SE_ATT_HARM_ADDITION, 10);
        Skill skill2 = new Skill(2, "格挡", TYPE_DEFENSE, SE_DEF_BLOCK, 10);
        allSkills.add(skill1);
        allSkills.add(skill2);
        return allSkills;
    }

}
