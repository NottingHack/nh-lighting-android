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
import android.widget.LinearLayout;
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
    int PATTERN_ID_OFF, PATTERN_ID_ON = 0;

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
    RecyclerView rv_dashboard_lights, rv_patterns;
    RecyclerView.Adapter rv_dashboard_lights_adaptor, rv_patterns_adaptor;
    RecyclerView.LayoutManager rv_dashboard_lights_layout_manager, rv_patterns_layout_manager;
    BottomNavigationView navigation;
    ArrayList<Light> lights;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vf = (ViewFlipper) findViewById(R.id.view_flipper);

        /* // for future implementation......
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
       */


        try {
            handler = new ProcessHandler(ROOM, rv_dashboard_lights_adaptor, this);
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
                        //   handler.getRoomData(ROOM); //TESTING DISABLED
                        rv_dashboard_lights.setLayoutManager(rv_dashboard_lights_layout_manager);
                        rv_dashboard_lights_adaptor = new RVAdaptorLights(handler.getLightData(ROOM), handler, ROOM);
                        // RVAdaptorLights adap = new RVAdaptorLights(lights, handler); // above code for release, testing only
                        rv_dashboard_lights.setAdapter(rv_dashboard_lights_adaptor);
                        rv_dashboard_lights_adaptor.notifyDataSetChanged();
                        return true;
                    case R.id.navigation_patterns:
                        vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.view_dashboard_patterns)));
                        rv_patterns = (RecyclerView) findViewById(R.id.rv_dasboard_patterns);
                        rv_patterns_layout_manager = new GridLayoutManager(getApplicationContext(), 4);
                        rv_patterns.setLayoutManager(rv_patterns_layout_manager);
                        rv_patterns_adaptor = new RVAdaptorPatterns(handler.getPatterns(), handler, ROOM);
                        rv_patterns.setAdapter(rv_patterns_adaptor);
                        rv_patterns_adaptor.notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        };

        tv_home_time = (TextView) findViewById(R.id.tv_home_time);
       // tv_home_date = (TextView) findViewById(R.id.tv_home_date);
        tv_room_temperature = (TextView) findViewById(R.id.tv_homeTemp);

        tv_home_time.setTextColor(Color.WHITE);
        //tv_home_date.setTextColor(Color.WHITE);
        tv_room_temperature.setTextColor(Color.WHITE);

        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tv_room_information = (TextView) findViewById(R.id.tv_RoomName);
        tv_room_information.setTextColor(Color.WHITE);
        tv_room_information.setText(ROOM);

        iv_lightHomeAll = (ImageView) findViewById(R.id.iv_homeLightStatus);
        iv_lightHomeAll.setImageResource(R.drawable.ic_light_off);

        tb_lightHomeAll = (ToggleButton) findViewById(R.id.tb_LightHomeAll);


        final Thread time = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_home_time.setText(Calendar.getInstance().getTime().toString());
                                // tv_home_date.setText(Calendar.getInstance().get);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        time.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        //testing
        handler.testing();
    }

    private void getPatternId()
    {
        for (int i=0; i< handler.getPatterns().size(); i++)
        {
            if (handler.getPatterns().get(i).getName().startsWith(ROOM.substring(0, 3)) && handler.getPatterns().get(i).getName().contains("Off"))
            {
                PATTERN_ID_OFF = handler.getPatterns().get(i).getPatternId();
            }

            if (handler.getPatterns().get(i).getName().startsWith(ROOM.substring(0, 3)) && handler.getPatterns().get(i).getName().contains("On"))
            {
                PATTERN_ID_ON = handler.getPatterns().get(i).getPatternId();
            }
        }
    }

    public void createControls(boolean setting)
    {
        getPatternId();
        tb_lightHomeAll.setOnCheckedChangeListener(null);

        if (setting == true)
        {
            iv_lightHomeAll.setImageResource(R.drawable.ic_light_on);
            tb_lightHomeAll.setChecked(true);
        }
        if (setting == false)
        {
            iv_lightHomeAll.setImageResource(R.drawable.ic_light_off);
            tb_lightHomeAll.setChecked(false);
        }

        tb_lightHomeAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // id set to 1 on home screen, need to implement patterns or custom light setup for overall main control.
                    //handler.handleChangeCommand("button", 1, true, ROOM);

                    for (int i=0; i< handler.getRooms().size(); i++)
                    {
                        if (handler.getRooms().get(i).getName().equals(ROOM))
                        {
                            handler.handleChangeCommand("pattern", PATTERN_ID_ON, true, ROOM);
                        }
                    }

                    //iv_lightHomeAll.setBackgroundColor(Color.YELLOW);
                    iv_lightHomeAll.setImageResource(R.drawable.ic_light_on);
                }
                else
                {
                    //handler.handleChangeCommand("button", 1, false, ROOM);
                    for (int i=0; i< handler.getRooms().size(); i++)
                    {
                        if (handler.getRooms().get(i).getName().equals(ROOM))
                        {
                            handler.handleChangeCommand("pattern", PATTERN_ID_OFF, true, ROOM);
                        }
                    }

                    //iv_lightHomeAll.setBackgroundColor(Color.GRAY);
                    iv_lightHomeAll.setImageResource(R.drawable.ic_light_off);
                }
            }
        });

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
            final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_room_choice);
            rg.removeAllViews();

            for (int i=0; i< handler.getRooms().size(); i++)
            {
                RadioButton rb = new RadioButton(this);
                rb.setId(View.generateViewId());
                rb.setText(handler.getRooms().get(i).getName());
                LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1f);
                rb.setLayoutParams(pm);
                rg.addView(rb);
            }

            btn_room_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
