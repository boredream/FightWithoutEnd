
package com.boredream.fightwithoutend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.boredream.fightwithoutend.R;
import com.boredream.fightwithoutend.test.Test;

public class MainMenuActivity extends Activity implements OnClickListener {

    private Button menuStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuStart = (Button) findViewById(R.id.menu_start);
        menuStart.setOnClickListener(this);

        // Test.dropTreasuresTest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
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

            default:
                break;
        }
    }

}
