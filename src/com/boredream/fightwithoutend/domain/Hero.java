
package com.boredream.fightwithoutend.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Hero {
    private static final String TAG = "Hero";

    public static final int MAX_HP = 20;
    // �����ɳ�
    public static final int ATR_RISE = 2;
    // �����ɳ�
    public static final int DEF_RISE = 1;

    private String name;
    private int hp;
    private int attrackValue;
    private int defenseValue;
    public int level;
    public int exp;
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
        Hero hero = new Hero("��ʿa", MAX_HP, 10, 5, 0, 0);
        hero.totalObtainTreasure = new ArrayList<Treasure>();
        return hero;
    }

    public void equip(Treasure treasure) {
        if (treasure.getEquipLocation() == Treasure.EQUIP_LOCATION_WEAPON) {
            // ��ǰװ���ǿ�ʱ,�Ƚ���ǰװ���ƻ���Ʒ��,�ѵ�ǰװ�����
            if (currentWeapon != null) {
                Log.i(TAG, "equip(" + treasure + ") -- ж������");
                totalObtainTreasure.add(currentWeapon);
                currentWeapon = null;
            }
            // �ٽ��µ�װ������,����ǰװ��Ϊ��ʱ,��ֱ���������Ϲ���
            totalObtainTreasure.remove(treasure);
            currentWeapon = treasure;
            Log.i(TAG, "equip(" + treasure + ") -- װ��������");
        } else {
            // ��ǰװ���ǿ�ʱ,�Ƚ���ǰװ���ƻ���Ʒ��,�ѵ�ǰװ�����
            if (currentArmor != null) {
                Log.i(TAG, "equip(" + treasure + ") -- ж�·���");
                totalObtainTreasure.add(currentArmor);
                currentArmor = null;
            }
            // �ٽ��µ�װ������,����ǰװ��Ϊ��ʱ,��ֱ���������Ϲ���
            totalObtainTreasure.remove(treasure);
            currentArmor = treasure;
            Log.i(TAG, "equip(" + treasure + ") -- װ���Ϸ���");
        }
    }

    public int currentLevelNeedExp() {
        return exps4LevelRise()[level];
    }

    public static int[] exps4LevelRise() {
        // ժ���λ�����
        // 0-1�� ��40���� ����40����
        int exp0to1 = 40;
        // 1-2�� ��110���� ����150����
        int exp1to2 = 150;
        // 2-3�� ��237���� ����387����
        int exp2to3 = 387;
        // 3-4�� ��450���� ����837����
        int exp3to4 = 837;
        // 4-5�� ��779���� ����1616����
        int exp4to5 = 1616;
        // 5-6�� ��1252���� ����2868����
        int exp5to6 = 2868;
        // 6-7�� ��1898���� ����4766����
        int exp6to7 = 4766;
        // 7-8�� ��2745���� ����7511����
        int exp7to8 = 7511;
        // 8-9�� ��3822���� ����11333����
        int exp8to9 = 11333;
        // 9-0�� ��5159���� ����16492����
        int exp9to10 = 16492;
        // 10-11�� ��6784���� ����23276����
        int exp10to11 = 23276;
        // 11-12�� ��8726���� ����32002����
        int exp11to12 = 32002;
        // 12-13�� ��11013���� ����43015����
        int exp12to13 = 43015;
        // 13-14�� ��13674���� ����56689����
        int exp13to14 = 56689;
        // 14-15�� ��16739���� ����73428����
        int exp14to15 = 73428;
        // 15-16�� ��20236���� ����93664����
        int exp15to16 = 93664;
        // 16-17�� ��24194���� ����117858����
        int exp16to17 = 117858;
        // 17-18�� ��28641���� ����146499����
        int exp17to18 = 146499;
        // 18-19�� ��33606���� ����180105����
        int exp18to19 = 180105;
        // 19-20�� ��39119���� ����219224����
        int exp19to20 = 219224;
        return new int[] {
                exp0to1, exp1to2, exp2to3, exp3to4, exp4to5,
                exp5to6, exp6to7, exp7to8, exp8to9, exp9to10,
                exp10to11, exp11to12, exp12to13, exp13to14, exp14to15,
                exp15to16, exp16to17, exp17to18, exp18to19, exp19to20
        };
    }

}
