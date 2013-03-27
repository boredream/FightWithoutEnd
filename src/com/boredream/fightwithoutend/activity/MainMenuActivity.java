
package com.boredream.fightwithoutend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.boredream.fightwithoutend.R;
import com.boredream.fightwithoutend.view.RecordDialog;

public class MainMenuActivity extends Activity implements OnClickListener {

    private Button menuStart;
    private Button menuLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);

        menuStart = (Button) findViewById(R.id.menu_start);
        menuStart.setOnClickListener(this);

        menuLoad = (Button) findViewById(R.id.menu_load);
        menuLoad.setOnClickListener(this);

        // Test.dropTreasuresTest();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_start:
                intent = new Intent(MainMenuActivity.this, MainGameActivity.class);
                startActivity(intent);
                MainMenuActivity.this.finish();
                break;
            case R.id.menu_load:
                RecordDialog dialog = new RecordDialog(
                        MainMenuActivity.this,
                        MainMenuActivity.this);
                dialog.show();
                break;

            default:
                break;
        }
    }

}
