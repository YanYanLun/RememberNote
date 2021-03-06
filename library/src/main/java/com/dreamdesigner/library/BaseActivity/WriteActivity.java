package com.dreamdesigner.library.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.dreamdesigner.floatingactionbutton.FloatingActionButtonPlus;
import com.dreamdesigner.library.R;
import com.dreamdesigner.library.StartActivity.ActivityStart;
import com.dreamdesigner.library.Utils.AppOnForegroundUtils;
import com.dreamdesigner.floatingactionbutton.FabTagLayout;

public abstract class WriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * 打开写新消息窗口
     *
     * @param view
     */
    protected abstract void WriteNewNote(View view);

    /**
     * 打开写新录音消息窗口
     *
     * @param view
     */
    protected abstract void AudioRecorderNote(View view);

    /**
     * UI加载完毕进行调用
     */
    protected abstract void onFocusChanged();

    /**
     * 标记UI是否加载完毕
     */
    private boolean isChange = false;
    protected Toolbar toolbar;
    private boolean isActive = false;
    public FloatingActionButtonPlus FabPlus;
    public DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_write);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                WriteNewNote(view);
            }
        });
        FabPlus = (FloatingActionButtonPlus) findViewById(R.id.FabPlus);
        FabPlus.setOnItemClickListener(new FloatingActionButtonPlus.OnItemClickListener() {
            @Override
            public void onItemClick(FabTagLayout tagView, int position) {
                switch (position) {
                    case 0:
                        WriteNewNote(tagView);
                        break;
                    case 1:
                        AudioRecorderNote(tagView);
                        break;
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isChange) {
            onFocusChanged();
            isChange = true;
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        View content = LayoutInflater.from(this).inflate(layoutResID, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        content.setLayoutParams(params);
        ((LinearLayout) this.findViewById(R.id.content_frame)).removeAllViews();
        ((LinearLayout) this.findViewById(R.id.content_frame)).addView(content);
    }

    public void doActivity(Class<? extends Activity> clz) {
        if (clz != null) {
            ActivityStart.Start(this, clz);
        }
    }

    public void doActivity(Class<? extends Activity> clz, Bundle extras) {
        if (clz != null) {
            ActivityStart.Start(this, clz, extras);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer == null)
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        if (drawer == null)
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!AppOnForegroundUtils.isAppOnForeground(this)) {
            //app 进入后台
            //全局变量isActive = false 记录当前已经进入后台
            Intent intent = new Intent();
            intent.setAction("activity.isAppOnForeground");
            sendBroadcast(intent);
            isActive = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("activity.isAppUnForeground");
        sendBroadcast(intent);
        if (!isActive) {
            //app 从后台唤醒，进入前台

            isActive = true;
        }
    }
}
