package com.portsip.sipsample.modification.src;


import static com.portsip.sipsample.modification.src.LockApi.DOOR_CLOSE_START;
import static com.portsip.sipsample.modification.src.LockApi.SUCCESS;
import static com.portsip.sipsample.modification.src.models.Utils.FullScreen;
import static com.portsip.sipsample.modification.src.models.Utils.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.portsip.R;
import com.portsip.databinding.ActivityCoreBinding;
import com.portsip.sipsample.modification.admin.PinAdminActivity;
import com.portsip.sipsample.modification.src.models.AdminConfig;
import com.portsip.sipsample.ui.LoginFragment;
import com.portsip.sipsample.ui.MainSIPActivity;
import com.portsip.sipsample.ui.NumpadFragment;
import com.portsip.sipsample.util.Ring;

import java.util.Date;
import java.util.List;

public class HomeActivity extends BaseActivity implements TextToSpeech.OnInitListener {
    public static ActivityCoreBinding binding;

    Handler handler = new Handler();


    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, HomeActivity.class));
        activity.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    void runLocker() {
        AdminConfig adminConfig = new SharedPreUtil(HomeActivity.this).getConfig();

        /*
         * for test purpose only
         *
         *
         *
         * */
//


        if (adminConfig == null) {
            Toast.makeText(getApplicationContext(), "Configure First", Toast.LENGTH_LONG).show();
            PinAdminActivity.start(HomeActivity.this);
            finishAffinity();
            return;
        }


        LoginFragment.switchOnline();


        try {
            LockApi.connectWithBoard(adminConfig, s -> launcher(HomeActivity.this, () -> {
                if (s.equals(SUCCESS)) {
                    Toast.makeText(getApplicationContext(), "Successfully Connected With Board", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Connection Failed:" + s, Toast.LENGTH_LONG).show();
            }));


        } catch (Exception e) {
            e.printStackTrace();
            launcher(HomeActivity.this, () -> Toast.makeText(getApplicationContext(), "Configuration Error", Toast.LENGTH_LONG).show());
            PinAdminActivity.start(HomeActivity.this);
        }
        MApp.initCOM(str -> {
            Log.i("Locker Response:", " " + str);
            String result = str;
            result = result.replace(" ", "");
            Log.i("Locker Response:", "Simplified " + result);
            if (result.startsWith(DOOR_CLOSE_START)) {
                Log.i("Locker Response:", "Door:State Closed");
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Locker Response:", "Door:Exception:" + e.getLocalizedMessage());
                }
            }
        });

    }

    List<Integer> imageList;
    int currentApiVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoreBinding.inflate(getLayoutInflater());
        FullScreen(HomeActivity.this);
        setContentView(binding.getRoot());
        runLocker();

        binding.settingIV.setOnClickListener(v -> {
            PinAdminActivity.start(HomeActivity.this);
        });

        binding.CallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumpadFragment.makeCall();
                startCall();
            }
        });

        binding.callEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumpadFragment.hangUp();
                endCall();
            }
        });


    }

    public static void endCall() {
        Ring.getInstance(MainSIPActivity.mActivitySip).stop();
        Ring.getInstance(MainSIPActivity.mActivitySip).stopRingBackTone();
        binding.callingInfoLayout.setVisibility(View.GONE);
        binding.callLayout.setVisibility(View.VISIBLE);
        binding.callInProgress.setVisibility(View.GONE);

    }

    public static void startCall() {
        binding.callingInfoLayout.setVisibility(View.VISIBLE);
        binding.callLayout.setVisibility(View.GONE);
        binding.callInProgress.setVisibility(View.GONE);

    }

    public static void callInProgress() {
        binding.callingInfoLayout.setVisibility(View.GONE);
        binding.callLayout.setVisibility(View.GONE);
        binding.callInProgress.setVisibility(View.VISIBLE);
    }


    static double lastUsedTime = new Date().getTime();

    public static void used() {
        Log.i("Used", "keepStateUpdated: Updated");
        lastUsedTime = new Date().getTime();
    }

    boolean isIdle = false;
    boolean isFocus = true;


    void keepStateUpdated() {
        if (isFocus) {
            new Handler().postDelayed(() -> {
                double gap = (new Date().getTime() - lastUsedTime);
                Log.i("Used", "keepStateUpdated: " + gap);
                isIdle = gap > 20 * 1000;
                keepStateUpdated();
            }, 1000);
        }

    }


    void keepCheckAndRun() {
        if (isFocus) {
            new Handler().postDelayed(() -> {

                keepCheckAndRun();
            }, 1000);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        FullScreen(HomeActivity.this);
        isFocus = true;
        used();
        keepStateUpdated();
        keepCheckAndRun();
    }

    @Override
    protected void onPause() {
        super.onPause();
        used();
        isFocus = false;
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onBackPressed() {
        MainSIPActivity.mActivitySip.finish();
        finish();
    }

    private static boolean isTTSOkay = false;

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isTTSOkay = true;
        }
    }


}