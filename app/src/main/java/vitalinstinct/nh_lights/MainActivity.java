package vitalinstinct.nh_lights;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String ROOM = "Blue room";

    // cache expiration in seconds
    long cacheExpiration = 3600;

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAnalytics mFirebaseAnalytics;

    ProcessHandler handler;
    ViewFlipper vf;

    private TextView mTextMessage;

    ToggleButton tb_lightHomeAll;
    ImageView iv_lightHomeAll;
    TextView tv_room_information, tv_room_temperature, tv_home_time, tv_home_date;
    RecyclerView rv_dashboard_lights;
    RecyclerView.Adapter rv_dashboard_lights_adaptor;
    RecyclerView.LayoutManager rv_dashboard_lights_layout_manager;
    BottomNavigationView navigation2, navigation3;
    ArrayList<Light> lights;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vf = (ViewFlipper) findViewById(R.id.view_flipper);


        // for future implementation......
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        //expire the cache immediately for development mode.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
            mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            //task failed
                        }

                    }

                });
       // ROOM = mFirebaseRemoteConfig.getString("room");


        try {
            handler = new ProcessHandler(ROOM);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        lights = new ArrayList<>();


        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.view_home)));
                        return true;
                    case R.id.navigation_dashboard:
                        vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.view_dashboard_lights)));
                        rv_dashboard_lights = (RecyclerView) findViewById(R.id.rv_dasboard_lights);
                        rv_dashboard_lights_layout_manager = new GridLayoutManager(getApplicationContext(), 4);
                        handler.getRoomData(ROOM); //TESTING DISABLED
                        rv_dashboard_lights.setLayoutManager(rv_dashboard_lights_layout_manager);
                        RVAdaptorLights adap = new RVAdaptorLights(handler.getLightData(ROOM), handler);
                       // RVAdaptorLights adap = new RVAdaptorLights(lights, handler); // above code for release, testing only
                        rv_dashboard_lights.setAdapter(adap);
                        return true;
                    case R.id.navigation_notifications:
                        return true;
                }
                return false;
            }
        };

        tv_home_time = (TextView) findViewById(R.id.tv_home_time);
        tv_home_date = (TextView) findViewById(R.id.tv_home_date);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation2 = (BottomNavigationView) findViewById(R.id.navigation_dashboard2);
        navigation2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation3 = (BottomNavigationView) findViewById(R.id.navigation_dashboard3);
        navigation3.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tv_room_information = (TextView) findViewById(R.id.tv_RoomName);
        tv_room_information.setText(ROOM);

        iv_lightHomeAll = (ImageView) findViewById(R.id.iv_homeLightStatus);
        iv_lightHomeAll.setBackgroundColor(Color.GRAY);

        tb_lightHomeAll = (ToggleButton) findViewById(R.id.tb_LightHomeAll);
        tb_lightHomeAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // id set to 1 on home screen, need to implement patterns or custom light setup for overall main control.
                    handler.handleChangeCommand("button", 1, true, ROOM);
                    iv_lightHomeAll.setBackgroundColor(Color.YELLOW);
                }
                else
                {
                    handler.handleChangeCommand("button", 1, false, ROOM);
                    iv_lightHomeAll.setBackgroundColor(Color.GRAY);
                }
            }
        });

        final Thread time = new Thread() {
            @Override
            public void run() {
                try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_home_time.setText(Calendar.getInstance().getTime().toString());
                               // tv_home_date.setText(Calendar.getInstance().get);
                            }
                        });
                } catch (InterruptedException e) {
                }
            }
        };
        time.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_settings) {
           // go to settings
            vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.view_settings)));
            Button btn_room_choice = (Button) findViewById(R.id.btn_choose_room);
            btn_room_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioGroup rg = (RadioGroup) findViewById(R.id.rg_room_choice);
                    int selected = rg.getCheckedRadioButtonId();
                    RadioButton rgbtn = (RadioButton) findViewById(selected);
                    ROOM = rgbtn.getText().toString();
                    tv_room_information.setText(ROOM);
                }
            });
        }
        return true;
    }
}
