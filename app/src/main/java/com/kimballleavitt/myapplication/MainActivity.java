package com.kimballleavitt.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.CORRECT;
import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add custom sound to swipe mapping", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        patternLockView = findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(patternLockViewListener);
    }

    private PatternLockView patternLockView;
    private PatternLockViewListener patternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " + PatternLockUtils.patternToString(patternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " + PatternLockUtils.patternToString(patternLockView, pattern));
            if (mp != null && mp.isPlaying()) {
                mp.stop();
            }
            if (pattern.size() == 2) {
                PatternLockView.Dot first = pattern.get(0);
                PatternLockView.Dot second = pattern.get(1);
                if (first.getRow() == 0 && first.getColumn() == 0 &&
                        second.getRow() == 0 && second.getColumn() == 1) {
                    patternLockView.setViewMode(CORRECT);
                    mp = MediaPlayer.create(MainActivity.this, R.raw.terran_three);
                    mp.start();
                    return;
                }
            } else if (pattern.size() == 3) {
                PatternLockView.Dot first = pattern.get(0);
                PatternLockView.Dot second = pattern.get(1);
                PatternLockView.Dot third = pattern.get(2);
                if (first.getRow() == 0 && first.getColumn() == 0 &&
                        second.getRow() == 0 && second.getColumn() == 1 &&
                    third.getRow() == 2 && third.getColumn() == 2) {
                    patternLockView.setViewMode(CORRECT);
                    mp = MediaPlayer.create(MainActivity.this, R.raw.maracas);
                    mp.start();
                    return;
                }
            }
            patternLockView.setViewMode(WRONG);
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.release();
        mp = null;
    }
}
