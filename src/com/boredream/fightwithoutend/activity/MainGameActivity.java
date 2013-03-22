
package com.boredream.fightwithoutend.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boredream.fightwithoutend.R;
import com.boredream.fightwithoutend.controller.FightDataInfoController;
import com.boredream.fightwithoutend.controller.ProbabilityEventController;
import com.boredream.fightwithoutend.domain.FightOneKickData;
import com.boredream.fightwithoutend.domain.FightOneturnData;
import com.boredream.fightwithoutend.domain.Hero;
import com.boredream.fightwithoutend.domain.Monster;
import com.boredream.fightwithoutend.domain.Skill;
import com.boredream.fightwithoutend.domain.Treasure;

import java.util.ArrayList;
import java.util.List;

public class MainGameActivity extends Activity implements OnClickListener, OnItemClickListener {
    private static final String TAG = "MainGameActivity";

    // 战斗刷新速度
    private long refreshInfoSpeed = 500;

    // 战斗信息
    private ScrollView mainInfoSv;
    private LinearLayout mainInfoPlatform;
    private int fightInfoTotalCount = 50;
    private int fightInfoSize = 20;

    // 英雄
    private Hero hero;

    // 根信息栏
    private TextView rootItemBarCharacter; // 按钮-人物
    private TextView rootItemBarOther; // 按钮-其他

    // 人物信息栏
    private LinearLayout rootItemCharacter;

    private TextView itembarContri; // 按钮-属性
    private TextView itembarEquip; // 按钮-装备
    private TextView itembarGoods; // 按钮-物品

    private LinearLayout itemContri; // 主体内容-属性
    private TextView mainContriHp;
    private TextView mainContriAtt;
    private TextView mainContriDef;
    private TextView mainContriLv;
    private TextView mainContriNdExp;
    private TextView mainContriCurExp;
    private TextView mainContriSp;

    private LinearLayout itemEquip; // 主体内容-装备
    private TextView equipWeapon;
    private TextView equipArmor;

    private LinearLayout itemGoods; // 主体内容-物品
    private ListView itemGoodsCountainer;
    private ItemGoodsAdapter itemGoodsAdapter;

    // 其他信息栏
    private LinearLayout rootItemOther;

    private TextView itembarShop; // 按钮-商店
    private TextView itembarMap; // 按钮-地图
    private TextView itembarSkill; // 按钮-技能

    private LinearLayout itemShop; // 主体内容-商店

    private LinearLayout itemMap; // 主体内容-地图

    private LinearLayout itemSkill; // 主体内容-技能
    private ListView itemSkillCountainer;
    private ItemSkillAdapter itemSkillAdapter;

    // 怪物
    private ArrayList<Monster> monsters;
    private Monster monster;

    private boolean gameThreadRunning;
    // 是否正在进行一轮战斗,是 正在进行;否 战斗已经结束
    private boolean isOneTurnFinghting = false;

    private GameThread gameThread;

    private FightOneturnData runOneTurn = null;
    private int oneTurnIndex = 0;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 10:
                 if (!isOneTurnFinghting) {
                     monster = getMonster();

                     TextView metMonInfo = new TextView(MainGameActivity.this);
                     metMonInfo.setTextSize(fightInfoSize);
                     metMonInfo.setText("你遇到了 " + monster.getName());
                     Log.i(TAG, "你遇到了:" + monster.getName());
                     mainInfoPlatform.addView(metMonInfo);

                     runOneTurn = FightDataInfoController.runOneTurn(monster);

                     isOneTurnFinghting = true;

                 } else {
                     // 如果一轮战斗信息没有显示完(一轮战斗尚未结束)
                     if (oneTurnIndex < runOneTurn.oneKickData.size()) {
                         TextView oneKickInfo = new TextView(MainGameActivity.this);
                         // 获取本轮战斗的一次击打信息
                         FightOneKickData fightOneKickData = runOneTurn.oneKickData
                                 .get(oneTurnIndex);
                         // 将一次击打信息数据显示到页面中
                         oneKickInfo.setText(fightOneKickData.getDescribe());
                         Log.i(TAG, fightOneKickData.getDescribe());
                         mainInfoPlatform.addView(oneKickInfo);
                         if (FightOneKickData.M2H == fightOneKickData.getHarmType()) {
                             mainContriHp.setText(fightOneKickData.getHeroCurrentHp() + "");
                         }
                         // 遍历到下一次击打
                         oneTurnIndex++;
                     } else {
                         // 一轮战斗结束了
                         isOneTurnFinghting = false;
                         Log.i(TAG, "战斗结束  isOneTurnFinghting=" + isOneTurnFinghting);
                         oneTurnIndex = 0;
                         mainContriHp.setText(Hero.MAX_HP + "");
                         mainContriCurExp.setText(hero.exp + "");
                         mainContriNdExp.setText(hero.currentLevelNeedExp() + "");
                         mainContriLv.setText(hero.level + "");
                         // 掉落装备,只有英雄胜利时才会触发
                         if (runOneTurn.getFightOutcome() == FightOneturnData.FIGHT_OUTCOME_HERO_IS_WIN) {
                             TextView dropTreasureInfo = new TextView(MainGameActivity.this);
                             List<Treasure> dropTreasures = runOneTurn.getDropTreasures();
                             StringBuilder sb = new StringBuilder();
                             for (int i = 0; i < dropTreasures.size(); i++) {
                                 if (i != 0) {
                                     sb.append("\n");
                                 }
                                 sb.append("获得:" + dropTreasures.get(i).getName() + ";");
                                 hero.totalObtainTreasure.add(dropTreasures.get(i));
                             }
                             dropTreasureInfo.setText(sb.toString());
                             dropTreasureInfo.setTextColor(Color.RED);
                             mainInfoPlatform.addView(dropTreasureInfo);
                         }
                         TextView fightEndSeparatorInfo = new TextView(MainGameActivity.this);
                         fightEndSeparatorInfo.setText("--------------------");
                         mainInfoPlatform.addView(fightEndSeparatorInfo);
                     }
                 }
                 if (mainInfoPlatform.getChildCount() > fightInfoTotalCount) {
                     mainInfoPlatform.removeViewAt(0);
                 }

                 // mainInfoSv.fullScroll(ScrollView.FOCUS_DOWN);
                 scrollToBottom(mainInfoSv, mainInfoPlatform);
                 break;
         }

         super.handleMessage(msg);
     }

    };

    /**
     * SrcollView战斗信息栏滚到最底部
     * 
     * @param scroll
     * @param inner
     */
    public void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }

                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }

    /**
     * 获取一个怪物
     * <p>
     * 没有怪物,战斗结束(人或怪物死了)时,随即遭遇一个新的怪物;
     * </p>
     * 
     * @return
     */
    private Monster getMonster() {
        if (monster == null) {
            monster = ProbabilityEventController.encounterNewMonster(monsters);
        } else {
            if (monster.getHp() <= 0 || hero.getHp() <= 0) {
                monster = ProbabilityEventController.encounterNewMonster(monsters);
            }
        }
        return monster;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_gameview);

        Log.i(TAG, "start game~");

        initGameData();

        gameThreadRunning = true;
        gameThread = new GameThread();
        gameThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameThreadRunning = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showExitDialog();
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出退出程序提示框
     */
    private void showExitDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("是否退出游戏");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainGameActivity.this.finish();
                        System.exit(0);
                    }

                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }

    private void initGameData() {
        // 英雄
        hero = FightDataInfoController.hero;
        // 左侧战斗信息
        mainInfoSv = (ScrollView) findViewById(R.id.main_info_sv);
        mainInfoPlatform = (LinearLayout) findViewById(R.id.main_info_ll);
        // 根信息栏
        rootItemBarCharacter = (TextView) findViewById(R.id.root_itembar_character);
        rootItemBarOther = (TextView) findViewById(R.id.root_itembar_other);
        // ---- 人物信息栏
        rootItemCharacter = (LinearLayout) findViewById(R.id.root_item_character);
        itembarContri = (TextView) findViewById(R.id.character_itembar_contribute);
        itembarEquip = (TextView) findViewById(R.id.character_itembar_equip);
        itembarGoods = (TextView) findViewById(R.id.character_itembar_goods);
        // ---- ---- 属性
        itemContri = (LinearLayout) findViewById(R.id.character_item_contribute);
        mainContriHp = (TextView) findViewById(R.id.main_contri_hp);
        mainContriAtt = (TextView) findViewById(R.id.main_contri_att);
        mainContriDef = (TextView) findViewById(R.id.main_contri_def);
        mainContriLv = (TextView) findViewById(R.id.main_contri_level);
        mainContriNdExp = (TextView) findViewById(R.id.main_contri_needexp);
        mainContriCurExp = (TextView) findViewById(R.id.main_contri_currentexp);
        mainContriSp = (TextView) findViewById(R.id.main_contri_sp);

        mainContriHp.setText(hero.getHp() + "");
        mainContriAtt.setText(hero.getAttackValue() + "");
        mainContriDef.setText(hero.getDefenseValue() + "");
        mainContriLv.setText(hero.level + "");
        mainContriNdExp.setText(hero.currentLevelNeedExp() + "");
        mainContriCurExp.setText(hero.exp + "");
        mainContriSp.setText(hero.sp + "");
        // ---- ---- 装备
        itemEquip = (LinearLayout) findViewById(R.id.character_item_equip);
        equipWeapon = (TextView) findViewById(R.id.equip_weapon);
        equipArmor = (TextView) findViewById(R.id.equip_armor);

        equipWeapon.setText("无");
        equipArmor.setText("无");
        // ---- ---- 物品
        itemGoods = (LinearLayout) findViewById(R.id.character_item_goods);
        itemGoodsCountainer = (ListView) findViewById(R.id.item_goods_container);

        itemGoodsAdapter = new ItemGoodsAdapter();
        itemGoodsCountainer.setAdapter(itemGoodsAdapter);
        itemGoodsCountainer.setOnItemClickListener(this);
        // ---- 其他信息栏
        rootItemOther = (LinearLayout) findViewById(R.id.root_item_other);
        itembarShop = (TextView) findViewById(R.id.other_itembar_shop);
        itembarMap = (TextView) findViewById(R.id.other_itembar_map);
        itembarSkill = (TextView) findViewById(R.id.other_itembar_skill);
        // ---- ---- 商店
        itemShop = (LinearLayout) findViewById(R.id.other_item_shop);
        // ---- ---- 地图(待添加)
        itemMap = (LinearLayout) findViewById(R.id.other_item_map);
        // ---- ---- 技能
        itemSkill = (LinearLayout) findViewById(R.id.other_item_skill);
        itemSkillCountainer = (ListView) findViewById(R.id.item_skill_container);

        itemSkillAdapter = new ItemSkillAdapter();
        itemSkillCountainer.setAdapter(itemSkillAdapter);
        itemSkillCountainer.setOnItemClickListener(this);

        // itembar的点击监听
        rootItemBarCharacter.setOnClickListener(this);
        rootItemBarOther.setOnClickListener(this);
        itembarContri.setOnClickListener(this);
        itembarEquip.setOnClickListener(this);
        itembarGoods.setOnClickListener(this);
        itembarShop.setOnClickListener(this);
        itembarMap.setOnClickListener(this);
        itembarSkill.setOnClickListener(this);

        monsters = Monster.getMonsters();
    }

    private class GameThread extends Thread {

        @Override
        public void run() {
            while (gameThreadRunning) {
                try {
                    Thread.sleep(refreshInfoSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(10);

            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick() -- " + v.getId() + " -- 被点击了");
        switch (v.getId()) {
            case R.id.root_itembar_character:
                rootItemBarCharacter.setBackgroundResource(R.color.current_item);
                rootItemCharacter.setVisibility(View.VISIBLE);
                rootItemBarOther.setBackgroundResource(R.color.transparent);
                rootItemOther.setVisibility(View.GONE);
                break;
            case R.id.root_itembar_other:
                rootItemBarCharacter.setBackgroundResource(R.color.transparent);
                rootItemCharacter.setVisibility(View.GONE);
                rootItemBarOther.setBackgroundResource(R.color.current_item);
                rootItemOther.setVisibility(View.VISIBLE);
                break;

            case R.id.character_itembar_contribute:
                itembarContri.setBackgroundResource(R.color.current_item);
                itemContri.setVisibility(View.VISIBLE);
                itembarEquip.setBackgroundResource(R.color.transparent);
                itemEquip.setVisibility(View.GONE);
                itembarGoods.setBackgroundResource(R.color.transparent);
                itemGoods.setVisibility(View.GONE);
                break;

            case R.id.character_itembar_equip:
                itembarContri.setBackgroundResource(R.color.transparent);
                itemContri.setVisibility(View.GONE);
                itembarEquip.setBackgroundResource(R.color.current_item);
                itemEquip.setVisibility(View.VISIBLE);
                itembarGoods.setBackgroundResource(R.color.transparent);
                itemGoods.setVisibility(View.GONE);
                break;
            case R.id.character_itembar_goods:
                itembarContri.setBackgroundResource(R.color.transparent);
                itemContri.setVisibility(View.GONE);
                itembarEquip.setBackgroundResource(R.color.transparent);
                itemEquip.setVisibility(View.GONE);
                itembarGoods.setBackgroundResource(R.color.current_item);
                itemGoods.setVisibility(View.VISIBLE);
                break;

            case R.id.other_itembar_shop:
                itembarShop.setBackgroundResource(R.color.current_item);
                itemShop.setVisibility(View.VISIBLE);
                itembarMap.setBackgroundResource(R.color.transparent);
                itemMap.setVisibility(View.GONE);
                itembarSkill.setBackgroundResource(R.color.transparent);
                itemSkill.setVisibility(View.GONE);
                break;
            case R.id.other_itembar_map:
                itembarShop.setBackgroundResource(R.color.transparent);
                itemShop.setVisibility(View.GONE);
                itembarMap.setBackgroundResource(R.color.current_item);
                itemMap.setVisibility(View.VISIBLE);
                itembarSkill.setBackgroundResource(R.color.transparent);
                itemSkill.setVisibility(View.GONE);
                break;
            case R.id.other_itembar_skill:
                System.out.println("点击了 skill");
                itembarShop.setBackgroundResource(R.color.transparent);
                itemShop.setVisibility(View.GONE);
                itembarMap.setBackgroundResource(R.color.transparent);
                itemMap.setVisibility(View.GONE);
                itembarSkill.setBackgroundResource(R.color.current_item);
                itemSkill.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    class ItemSkillAdapter extends BaseAdapter {

        private List<Skill> skills;

        public ItemSkillAdapter() {
            skills = hero.getExistSkill();
        }

        @Override
        public int getCount() {
            return skills.size();
        }

        @Override
        public Skill getItem(int position) {
            return skills.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SkillViewHolder holder;
            if (convertView == null) {
                holder = new SkillViewHolder();
                convertView = View.inflate(MainGameActivity.this,
                        R.layout.skill_item, null);
                holder.skillName = (TextView) convertView.findViewById(R.id.skill_item_name);
                holder.skillLevel = (TextView) convertView.findViewById(R.id.skill_item_level);
                holder.skillRise = (ImageButton) convertView.findViewById(R.id.skill_item_rise);
                convertView.setTag(holder);
            } else {
                holder = (SkillViewHolder) convertView.getTag();
            }
            final Skill skill = getItem(position);
            holder.skillName.setText(skill.getName());
            holder.skillLevel.setText(skill.getLevel() + "");
            holder.skillRise.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // hero.riseSkill(skill);
                    FightDataInfoController.riseHeroSkill(skill);
                    mainContriSp.setText(hero.sp + "");
                    Log.i(TAG, "the skill " + skill.getName() + "'lv is up : "
                            + (skill.getLevel() - 1) + "->" + skill.getLevel());
                    itemSkillAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    static class SkillViewHolder {
        TextView skillName;
        TextView skillLevel;
        ImageButton skillRise;
    }

    class ItemGoodsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Hero.MAX_GOODS_COUNT;
        }

        @Override
        public Treasure getItem(int position) {
            return hero.totalObtainTreasure.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TreasureViewHolder holder;
            if (convertView == null) {
                holder = new TreasureViewHolder();
                convertView = View.inflate(MainGameActivity.this,
                        R.layout.treasure_countainer_item, null);
                holder.treasureName = (TextView) convertView.findViewById(R.id.treasure_item_name);
                convertView.setTag(holder);
            } else {
                holder = (TreasureViewHolder) convertView.getTag();
            }
            holder.treasureName.setText(getItem(position).getName());
            return convertView;
        }

    }

    static class TreasureViewHolder {
        TextView treasureName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 物品item点击
        if (parent == itemGoodsCountainer) {
            Log.i(TAG, "onItemClick() -- position=" + position +
                    ";obj=" + itemGoodsAdapter.getItem(position));
            Treasure treasure = itemGoodsAdapter.getItem(position);
            FightDataInfoController.equip(treasure);
            if (hero.currentWeapon != null) {
                equipWeapon.setText(hero.currentWeapon.getName());
            }
            if (hero.currentArmor != null) {
                equipArmor.setText(hero.currentArmor.getName());
            }
            mainContriAtt.setText(hero.getAttackValue() + "");
            mainContriDef.setText(hero.getDefenseValue() + "");
            itemGoodsAdapter.notifyDataSetChanged();
        }
        // 技能item点击
        else if (parent == itemSkillCountainer) {
            Log.i(TAG, "onItemClick() -- position=" + position +
                    ";obj=" + itemSkillAdapter.getItem(position));
        }
    }

}
