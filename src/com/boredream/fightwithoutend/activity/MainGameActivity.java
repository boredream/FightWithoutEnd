
package com.boredream.fightwithoutend.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.boredream.fightwithoutend.domain.Treasure;

import java.util.ArrayList;
import java.util.List;

public class MainGameActivity extends Activity implements OnClickListener, OnItemClickListener {
    // 战斗刷新速度
    private long refreshInfoSpeed = 500;

    // 战斗信息
    private ScrollView mainInfoSv;
    private LinearLayout mainInfoPlatform;
    private int fightInfoTotalCount = 50;
    private int fightInfoSize = 20;

    // 英雄
    private Hero hero;

    private TextView itembarContri;
    private LinearLayout itemContri;
    private TextView mainContriHp;
    private TextView mainContriAtt;
    private TextView mainContriDef;
    private TextView mainContriLv;
    private TextView mainContriNdExp;
    private TextView mainContriCurExp;

    private TextView itembarEquip;
    private LinearLayout itemEquip;
    private TextView equipWeapon;
    private TextView equipArmor;

    private TextView itembarGoods;
    private LinearLayout itemGoods;
    private ListView itemGoodsCountainer;
    private ItemGoodsAdapter itemGoodsAdapter;

    // 怪物
    private ArrayList<Monster> monsters;
    private Monster monster;

    private GameThread gameThread;

    private boolean gameThreadRunning;
    // 是否正在进行一轮战斗,是 正在进行;否 战斗已经结束
    private boolean isOneTurnFinghting = false;

    private FightOneturnData runOneTurn = null;
    private int oneTurnIndex = 0;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            monster = getMonster();

            switch (msg.what) {
                case 10:
                 if (!isOneTurnFinghting) {
                     // 随即获取一个怪物

                     TextView metMonInfo = new TextView(MainGameActivity.this);
                     metMonInfo.setTextSize(fightInfoSize);

                     metMonInfo.setText("你遇到了 " + monster.getName());
                     mainInfoPlatform.addView(metMonInfo);
                     // test
                     System.out.println("你遇到了 " + monster.getName());

                     runOneTurn = FightDataInfoController.runOneTurn(monster);

                     isOneTurnFinghting = true;

                 } else {
                     // 如果一轮战斗信息没有显示完(一轮战斗尚未结束)
                     if (oneTurnIndex < runOneTurn.oneKickData.size()) {
                         TextView oneKickInfo = new TextView(MainGameActivity.this);
                         // 获取一次击打信息,保存一次击打信息数据
                         FightOneKickData fightOneKickData = runOneTurn.oneKickData
                                 .get(oneTurnIndex);
                         int oneKickType = fightOneKickData.getHarmType();
                         int heroCurrentHp = fightOneKickData.getHeroCurrentHp();
                         String oneKickStr = fightOneKickData.getDescribe();
                         // 将一次击打信息数据显示到页面中
                         oneKickInfo.setText(oneKickStr);
                         mainInfoPlatform.addView(oneKickInfo);
                         // test
                         System.out.println(oneKickStr);
                         if (FightOneKickData.M2H == oneKickType) {
                             mainContriHp.setText(heroCurrentHp + "");
                         }
                         // 遍历到下一次击打
                         oneTurnIndex++;
                     } else {
                         // 一轮战斗结束了
                         isOneTurnFinghting = false;
                         oneTurnIndex = 0;
                         mainContriHp.setText(Hero.MAX_HP + "");
                         mainContriCurExp.setText(hero.exp + "");
                         mainContriNdExp.setText(hero.currentLevelNeedExp() + "");
                         mainContriLv.setText(hero.level + "");
                         // 掉落装备,只有英雄胜利时才会触发
                         if (runOneTurn.getFightOutcome() == FightOneturnData.FIGHT_OUTCOME_HERO_IS_WIN) {
                             TextView dropTreasureInfo = new TextView(MainGameActivity.this);
                             List<Treasure> dropTreasures = runOneTurn.getTreasureGaint();
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
                             // test
                             System.out.println(sb.toString());
                         }
                         TextView fightEndSeparatorInfo = new TextView(MainGameActivity.this);
                         fightEndSeparatorInfo.setText("--------------------");
                         mainInfoPlatform.addView(fightEndSeparatorInfo);
                         // test
                         System.out.println("--------------------");
                     }
                 }
                 if (mainInfoPlatform.getChildCount() > fightInfoTotalCount) {
                     mainInfoPlatform.removeViewAt(0);
                 }

                 // mainInfoSv.fullScroll(ScrollView.FOCUS_DOWN);
                 scrollToBottom(mainInfoSv, mainInfoPlatform);
                 break;

             case 11:
                 if (hero.currentWeapon != null) {
                     equipWeapon.setText(hero.currentWeapon.getName());
                 }
                 if (hero.currentArmor != null) {
                     equipArmor.setText(hero.currentArmor.getName());
                 }
                 mainContriAtt.setText(hero.getAttackValue() + "");
                 mainContriDef.setText(hero.getDefenseValue() + "");
                 itemGoodsAdapter.notifyDataSetChanged();
                 break;
         }

         super.handleMessage(msg);
     }

    };

    // SrcollView滚之最底部
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

    protected Monster getMonster() {
        if (monster == null) {
            monster = ProbabilityEventController.encounterMonster(monsters);
        } else {
            if (monster.getHp() <= 0 || hero.getHp() <= 0) {
                monster = ProbabilityEventController.encounterMonster(monsters);
            }
        }
        return monster;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_gameview);

        initGameData();

        mainInfoSv = (ScrollView) findViewById(R.id.main_info_sv);
        mainInfoPlatform = (LinearLayout) findViewById(R.id.main_info_ll);

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
                AlertDialog dialog = new Builder(this).create();
                dialog.setTitle("是否退出游戏");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainGameActivity.this.finish();
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
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initGameData() {
        hero = FightDataInfoController.hero;
        // 属性
        itembarContri = (TextView) findViewById(R.id.itembar_contribute);
        itemContri = (LinearLayout) findViewById(R.id.item_contribute);
        mainContriHp = (TextView) findViewById(R.id.main_contri_hp);
        mainContriAtt = (TextView) findViewById(R.id.main_contri_att);
        mainContriDef = (TextView) findViewById(R.id.main_contri_def);
        mainContriLv = (TextView) findViewById(R.id.main_contri_level);
        mainContriNdExp = (TextView) findViewById(R.id.main_contri_needexp);
        mainContriCurExp = (TextView) findViewById(R.id.main_contri_currentexp);

        mainContriHp.setText(hero.getHp() + "");
        mainContriAtt.setText(hero.getAttackValue() + "");
        mainContriDef.setText(hero.getDefenseValue() + "");
        mainContriLv.setText(hero.level + "");
        mainContriNdExp.setText(hero.currentLevelNeedExp() + "");
        mainContriCurExp.setText(hero.exp + "");
        // 装备
        itembarEquip = (TextView) findViewById(R.id.itembar_equip);
        itemEquip = (LinearLayout) findViewById(R.id.item_equip);
        equipWeapon = (TextView) findViewById(R.id.equip_weapon);
        equipArmor = (TextView) findViewById(R.id.equip_armor);

        equipWeapon.setText("无");
        equipArmor.setText("无");
        // 物品
        itembarGoods = (TextView) findViewById(R.id.itembar_goods);
        itemGoods = (LinearLayout) findViewById(R.id.item_goods);
        itemGoodsCountainer = (ListView) findViewById(R.id.item_goods_container);
        itemGoodsAdapter = new ItemGoodsAdapter();
        itemGoodsCountainer.setAdapter(itemGoodsAdapter);
        itemGoodsCountainer.setOnItemClickListener(this);

        // itembar的点击监听
        itembarContri.setOnClickListener(this);
        itembarEquip.setOnClickListener(this);
        itembarGoods.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.itembar_contribute:
                itembarContri.setBackgroundResource(R.color.current_item);
                itemContri.setVisibility(View.VISIBLE);
                itembarEquip.setBackgroundResource(R.color.transparent);
                itemEquip.setVisibility(View.GONE);
                itembarGoods.setBackgroundResource(R.color.transparent);
                itemGoods.setVisibility(View.GONE);
                break;

            case R.id.itembar_equip:
                itembarContri.setBackgroundResource(R.color.transparent);
                itemContri.setVisibility(View.GONE);
                itembarEquip.setBackgroundResource(R.color.current_item);
                itemEquip.setVisibility(View.VISIBLE);
                itembarGoods.setBackgroundResource(R.color.transparent);
                itemGoods.setVisibility(View.GONE);
                break;
            case R.id.itembar_goods:
                itembarContri.setBackgroundResource(R.color.transparent);
                itemContri.setVisibility(View.GONE);
                itembarEquip.setBackgroundResource(R.color.transparent);
                itemEquip.setVisibility(View.GONE);
                itembarGoods.setBackgroundResource(R.color.current_item);
                itemGoods.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    class ItemGoodsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return hero.totalObtainTreasure.size();
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
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainGameActivity.this,
                        R.layout.treasure_countainer_item, null);
                holder.treasureName = (TextView) convertView.findViewById(R.id.treasure_item_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.treasureName.setText(getItem(position).getName());
            return convertView;
        }

    }

    static class ViewHolder {
        TextView treasureName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Treasure treasure = itemGoodsAdapter.getItem(position);
        hero.equip(treasure);
        handler.sendEmptyMessage(11);
    }

}
