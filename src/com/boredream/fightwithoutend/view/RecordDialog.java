
package com.boredream.fightwithoutend.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.fightwithoutend.R;
import com.boredream.fightwithoutend.activity.MainGameActivity;
import com.boredream.fightwithoutend.activity.MainMenuActivity;
import com.boredream.fightwithoutend.controller.FightDataInfoController;
import com.boredream.fightwithoutend.controller.RecordFileController;
import com.boredream.fightwithoutend.domain.Record;

import java.util.Date;
import java.util.List;

public class RecordDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "ReadLoadDialog";

    private Context context;
    private Activity activity;

    private List<Record> records;

    private RelativeLayout save1;
    private TextView sv1heroname;
    private TextView sv1duration;
    private FrameLayout sv1blur;
    private TextView sv1none;

    private RelativeLayout save2;
    private TextView sv2heroname;
    private TextView sv2duration;
    private FrameLayout sv2blur;
    private TextView sv2none;

    private RelativeLayout save3;
    private TextView sv3heroname;
    private TextView sv3duration;
    private FrameLayout sv3blur;
    private TextView sv3none;

    private Button btnSave;
    private Button btnLoad;
    private Button btnCacnel;

    private int svIndex = -1;

    public RecordDialog(Context context, Activity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.record_dialog);

        save1 = (RelativeLayout) findViewById(R.id.record_dialog_save1);
        sv1heroname = (TextView) findViewById(R.id.record_dialog_save1_heroname);
        sv1duration = (TextView) findViewById(R.id.record_dialog_save1_duration);
        sv1blur = (FrameLayout) findViewById(R.id.record_dialog_save1_blur);
        sv1none = (TextView) findViewById(R.id.record_dialog_save1_none);
        save1.setOnClickListener(this);

        save2 = (RelativeLayout) findViewById(R.id.record_dialog_save2);
        sv2heroname = (TextView) findViewById(R.id.record_dialog_save2_heroname);
        sv2duration = (TextView) findViewById(R.id.record_dialog_save2_duration);
        sv2blur = (FrameLayout) findViewById(R.id.record_dialog_save2_blur);
        sv2none = (TextView) findViewById(R.id.record_dialog_save2_none);
        save2.setOnClickListener(this);

        save3 = (RelativeLayout) findViewById(R.id.record_dialog_save3);
        sv3heroname = (TextView) findViewById(R.id.record_dialog_save3_heroname);
        sv3duration = (TextView) findViewById(R.id.record_dialog_save3_duration);
        sv3blur = (FrameLayout) findViewById(R.id.record_dialog_save3_blur);
        sv3none = (TextView) findViewById(R.id.record_dialog_save3_none);
        save3.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.record_dialog_save);
        btnSave.setOnClickListener(this);
        btnLoad = (Button) findViewById(R.id.record_dialog_load);
        btnLoad.setOnClickListener(this);
        btnCacnel = (Button) findViewById(R.id.record_dialog_cancel);
        btnCacnel.setOnClickListener(this);
        if(activity instanceof MainMenuActivity) {
            btnSave.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
        }

        // 初始化记录列表
        records = RecordFileController.getAllRecords();
        Record sv1 = records.get(0);
        Record sv2 = records.get(1);
        Record sv3 = records.get(2);

        if (sv1 == null) {
            Log.i(TAG, "sv1 is null");
            sv1none.setVisibility(View.VISIBLE);
        } else {
            sv1heroname.setText(sv1.getHeroInfo().getName());
            sv1duration.setText(sv1.getFormatedDate());
        }
        if (sv2 == null) {
            Log.i(TAG, "sv2 is null");
            sv2none.setVisibility(View.VISIBLE);
        } else {
            sv2heroname.setText(sv2.getHeroInfo().getName());
            sv2duration.setText(sv2.getFormatedDate());
        }
        if (sv3 == null) {
            Log.i(TAG, "sv3 is null");
            sv3none.setVisibility(View.VISIBLE);
        } else {
            sv3heroname.setText(sv3.getHeroInfo().getName());
            sv3duration.setText(sv3.getFormatedDate());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_dialog_save1:
                sv1blur.setVisibility(View.VISIBLE);
                sv2blur.setVisibility(View.GONE);
                sv3blur.setVisibility(View.GONE);
                svIndex = 0;
                break;
            case R.id.record_dialog_save2:
                sv1blur.setVisibility(View.GONE);
                sv2blur.setVisibility(View.VISIBLE);
                sv3blur.setVisibility(View.GONE);
                svIndex = 1;
                break;
            case R.id.record_dialog_save3:
                sv1blur.setVisibility(View.GONE);
                sv2blur.setVisibility(View.GONE);
                sv3blur.setVisibility(View.VISIBLE);
                svIndex = 2;
                break;
            case R.id.record_dialog_save:
                // if (records.get(svIndex) != null) {
                // Toast.makeText(context, "覆盖原有记录~",
                // Toast.LENGTH_SHORT).show();
                // }

                // save
                Date saveDate = new Date(System.currentTimeMillis());
                Record newrecord = new Record(svIndex + 1, FightDataInfoController.hero, saveDate);
                RecordFileController.save(newrecord, svIndex + 1);
                // refresh activity data
                switch (svIndex) {
                    case 0:
                        sv1heroname.setText(newrecord.getName());
                        sv1duration.setText(newrecord.getFormatedDate());
                        sv1none.setVisibility(View.GONE);
                        break;
                    case 1:
                        sv2heroname.setText(newrecord.getName());
                        sv2duration.setText(newrecord.getFormatedDate());
                        sv2none.setVisibility(View.GONE);
                        break;
                    case 2:
                        sv3heroname.setText(newrecord.getName());
                        sv3duration.setText(newrecord.getFormatedDate());
                        sv3none.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.record_dialog_load:
                startGameFromRecord(svIndex);
                break;
            case R.id.record_dialog_cancel:
                RecordDialog.this.dismiss();
                break;
            default:
                break;
        }
    }

    private void startGameFromRecord(int svIndex) {
        if(svIndex == -1) {
            Toast.makeText(context, "请选择一个需要读取的存档", Toast.LENGTH_SHORT).show();
            return;
        }
        Record record = records.get(svIndex);
        if (record == null) {
            Toast.makeText(context, "选择存档为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, MainGameActivity.class);
        intent.putExtra("record", record);
        context.startActivity(intent);
        activity.finish();
    }
}
