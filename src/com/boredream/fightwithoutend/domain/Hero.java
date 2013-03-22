
package com.boredream.fightwithoutend.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Hero {
    private static final String TAG = "Hero";

    // 血上限
    public static int MAX_HP = 20;

    // 血上限成长
    public static final int MAX_HP_RISE = 5;
    // SP点成长
    public static final int SP_RISE = 2;
    // 攻击成长
    public static final int ATR_RISE = 2;
    // 防御成长
    public static final int DEF_RISE = 1;

    private String name;
    private int hp;
    private int attrackValue;
    private int defenseValue;
    public int level;
    public int exp;
    public int sp;
    // private List<Skill> possibleLearnSkill; // 可能学会的技能
    private List<Skill> existSkill; // 已有的技能
    private Skill currentAttSkill;
    private Skill currentDefSkill;
    public List<Treasure> totalObtainTreasure;
    public Treasure currentWeapon;
    public Treasure currentArmor;

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
        if (currentWeapon != null) {
            return attrackValue + currentWeapon.getAttAddition();
        } else {
            return attrackValue;
        }
    }

    public void setAttackValue(int attackValue) {
        this.attrackValue = attackValue;
    }

    public int getDefenseValue() {
        if (currentArmor != null) {
            return defenseValue + currentArmor.getDefAddition();
        } else {
            return defenseValue;
        }
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    public List<Skill> getExistSkill() {
        return existSkill;
    }

    /**
     * 获取当前英雄的攻击技能
     * 
     * @return
     */
    public Skill getCurrentAttSkill() {
        if (currentAttSkill != null) {
            return currentAttSkill;
        }
        for (Skill skill : existSkill) {
            int t = skill.getType();
            if (t == Skill.TYPE_ATTRACT) {
                currentAttSkill = skill;
            }
        }
        return currentAttSkill;
    }

    /**
     * 获取当前英雄的防御技能
     * 
     * @return
     */
    public Skill getCurrentDefSkill() {
        if (currentDefSkill != null) {
            return currentDefSkill;
        }
        for (Skill skill : existSkill) {
            int t = skill.getType();
            if (t == Skill.TYPE_DEFENSE) {
                currentDefSkill = skill;
            }
        }
        return currentDefSkill;
    }

    private Hero(String name, int hp, int attackValue, int defenseValue, int level, int exp) {
        super();
        this.name = name;
        this.hp = hp;
        this.attrackValue = attackValue;
        this.defenseValue = defenseValue;
        this.level = level;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "Hero [name=" + name + ", hp=" + hp + ", attackValue=" + attrackValue
                + ", defenseValue=" + defenseValue + ", level=" + level + ", exp=" + exp + "]";
    }

    public static Hero initHero() {
        Hero hero = new Hero("勇士a", MAX_HP, 10, 5, 0, 0);
        hero.totalObtainTreasure = new ArrayList<Treasure>();
        hero.existSkill = new ArrayList<Skill>();
        hero.existSkill = Skill.getAllSkills();
        hero.sp = 5;
        return hero;
    }

    public void equip(Treasure treasure) {
        if (treasure.getEquipLocation() == Treasure.EQUIP_LOCATION_WEAPON) {
            // 当前装备非空时,先将当前装备移回物品栏,把当前装备清空
            if (currentWeapon != null) {
                Log.i(TAG, "equip(" + treasure + ") -- 卸下武器");
                totalObtainTreasure.add(currentWeapon);
                currentWeapon = null;
            }
            // 再将新的装备换上,而当前装备为空时,则直接跳过以上过程
            totalObtainTreasure.remove(treasure);
            currentWeapon = treasure;
            Log.i(TAG, "equip(" + treasure + ") -- 装备上武器");
        } else {
            // 当前装备非空时,先将当前装备移回物品栏,把当前装备清空
            if (currentArmor != null) {
                Log.i(TAG, "equip(" + treasure + ") -- 卸下防具");
                totalObtainTreasure.add(currentArmor);
                currentArmor = null;
            }
            // 再将新的装备换上,而当前装备为空时,则直接跳过以上过程
            totalObtainTreasure.remove(treasure);
            currentArmor = treasure;
            Log.i(TAG, "equip(" + treasure + ") -- 装备上防具");
        }
    }

    public int currentLevelNeedExp() {
        return exps4LevelRise()[level];
    }

    public static int[] exps4LevelRise() {
        // 摘自梦幻西游
        // 0-1级 需40经验 共用40经验
        int exp0to1 = 40;
        // 1-2级 需110经验 共用150经验
        int exp1to2 = 150;
        // 2-3级 需237经验 共用387经验
        int exp2to3 = 387;
        // 3-4级 需450经验 共用837经验
        int exp3to4 = 837;
        // 4-5级 需779经验 共用1616经验
        int exp4to5 = 1616;
        // 5-6级 需1252经验 共用2868经验
        int exp5to6 = 2868;
        // 6-7级 需1898经验 共用4766经验
        int exp6to7 = 4766;
        // 7-8级 需2745经验 共用7511经验
        int exp7to8 = 7511;
        // 8-9级 需3822经验 共用11333经验
        int exp8to9 = 11333;
        // 9-0级 需5159经验 共用16492经验
        int exp9to10 = 16492;
        // 10-11级 需6784经验 共用23276经验
        int exp10to11 = 23276;
        // 11-12级 需8726经验 共用32002经验
        int exp11to12 = 32002;
        // 12-13级 需11013经验 共用43015经验
        int exp12to13 = 43015;
        // 13-14级 需13674经验 共用56689经验
        int exp13to14 = 56689;
        // 14-15级 需16739经验 共用73428经验
        int exp14to15 = 73428;
        // 15-16级 需20236经验 共用93664经验
        int exp15to16 = 93664;
        // 16-17级 需24194经验 共用117858经验
        int exp16to17 = 117858;
        // 17-18级 需28641经验 共用146499经验
        int exp17to18 = 146499;
        // 18-19级 需33606经验 共用180105经验
        int exp18to19 = 180105;
        // 19-20级 需39119经验 共用219224经验
        int exp19to20 = 219224;
        return new int[] {
                exp0to1, exp1to2, exp2to3, exp3to4, exp4to5,
                exp5to6, exp6to7, exp7to8, exp8to9, exp9to10,
                exp10to11, exp11to12, exp12to13, exp13to14, exp14to15,
                exp15to16, exp16to17, exp17to18, exp18to19, exp19to20
        };
    }

    public void initHp() {
        hp = MAX_HP;
    }

    /**
     * 技能等级提升
     * 
     * @param skill 需要提升的技能
     * @return 提升后的技能等级
     */
    public int riseSkill(Skill skill) {
        // 如果具备这个技能
        if (existSkill.contains(skill)) {
            // 如果英雄的sp点足够技能提升所需sp
            if (sp >= skill.getSp4rise()) {
                sp -= skill.getSp4rise();
                skill.setLevel(skill.getLevel() + 1);
            }
        }
        return skill.getLevel();
    }
}
