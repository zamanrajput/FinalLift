package com.portsip.sipsample.modification.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import com.portsip.R;
import com.portsip.databinding.ActivityConfirmSettingsBinding;
import com.portsip.sipsample.modification.src.BaseActivity;
import com.portsip.sipsample.modification.src.SharedPreUtil;
import com.portsip.sipsample.modification.src.models.AdminConfig;
import com.portsip.sipsample.modification.src.models.Utils;
import com.portsip.sipsample.ui.MainSIPActivity;


public class ConfirmSettingsActivity extends BaseActivity {
    ActivityConfirmSettingsBinding binding;
    private static AdminConfig adminConfig;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utils.FullScreen(this);
    }

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmSettingsBinding.inflate(getLayoutInflater());
        Utils.FullScreen(this);
        handler = new Handler();
        setContentView(binding.getRoot());
        binding.tv.setText("Do you want to Save Configuration?");
        binding.yes.setOnClickListener(v -> {
            new SharedPreUtil(ConfirmSettingsActivity.this).saveConfig(adminConfig);
            showToast("Saved");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    redirect(MainSIPActivity.class);
                    finish();
                }
            }, 500);
        });

        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.no.setOnClickListener(b -> finishAffinity());
    }


    public static void start(Activity activity, AdminConfig config) {
        adminConfig = config;
        activity.startActivity(new Intent(activity, ConfirmSettingsActivity.class));
        activity.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


}