package com.portsip.sipsample.modification.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.serialport.SerialPortFinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.portsip.R;
import com.portsip.databinding.ActivitySelectDeviceInterfaceBinding;
import com.portsip.sipsample.modification.src.BaseActivity;
import com.portsip.sipsample.modification.src.SharedPreUtil;
import com.portsip.sipsample.modification.src.models.AdminConfig;
import com.portsip.sipsample.modification.src.models.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends BaseActivity {
    ActivitySelectDeviceInterfaceBinding binding;

    AdminConfig adminConfig;

    @Override
    protected void onResume() {
        super.onResume();
        Utils.FullScreen(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDeviceInterfaceBinding.inflate(getLayoutInflater());
        Utils.FullScreen(this);
        SharedPreUtil sharedPreUtil = new SharedPreUtil(this);
        adminConfig = sharedPreUtil.getConfig();
        setContentView(binding.getRoot());


        if (adminConfig == null) {
            adminConfig = new AdminConfig();

        }


        binding.backBtn.setOnClickListener(v -> onBackPressed());


        SerialPortFinder mSerialPortFinder = new SerialPortFinder();
        List<String> devices = new ArrayList<>();
        devices.add("Select Device Interface");

        devices.addAll(Arrays.asList(mSerialPortFinder.getAllDevicesPath()));
        if (devices.size() == 1) {
            //temp data
            devices.add("/dev/ttSy4");
        }
        ArrayAdapter<String> portsAdapter = new ArrayAdapter<>(this, R.layout.device_interface_item, devices);
        portsAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        binding.devicesSpinner.setAdapter(portsAdapter);

        List<String> rates = new ArrayList<String>();
        rates.add("Select Baud Rate");
        rates.addAll(Arrays.asList(getResources().getStringArray(R.array.baud_rates)));
        ArrayAdapter<String> ratesAdapter = new ArrayAdapter<>(this, R.layout.device_interface_item, rates);
        ratesAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        binding.ratesSpinner.setAdapter(ratesAdapter);


        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(SettingsActivity.this, R.array.transports, R.layout.device_interface_item);
        transportAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        binding.protocolSpinner.setAdapter(transportAdapter);

        try {
            binding.protocolSpinner.setSelection(adminConfig.transportProtocol);
        } catch (Exception e) {
            e.printStackTrace();
            adminConfig.transportProtocol = 0;
        }

        binding.protocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adminConfig.transportProtocol = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try {
            adminConfig.devicePortName = devices.get(1);
            adminConfig.baudRate = Integer.parseInt(rates.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < devices.size(); i++) {
            if (i != 0) {
                if (sharedPreUtil.getDevicePort().equals(devices.get(i))) {
                    binding.devicesSpinner.setSelection(i, true);
                }
            }

        }
        for (int i = 0; i < rates.size(); i++) {
            if (i != 0) {
                if (sharedPreUtil.getBaudRate() == Integer.parseInt(rates.get(i))) {
                    binding.ratesSpinner.setSelection(i, true);
                }
            }
            binding.ratesSpinner.setSelection(0, true);

        }

        binding.devicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) adminConfig.devicePortName = devices.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.ratesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    adminConfig.baudRate = Integer.parseInt(rates.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        binding.submit.setOnClickListener(v -> {

            if (binding.ratesSpinner.getSelectedItemPosition() == 0 || binding.devicesSpinner.getSelectedItemPosition() == 0 || binding.username.getText().toString().length() == 0 ||

                    binding.password.getText().toString().length() == 0 || binding.serverIP.getText().toString().length() == 0
                    || binding.port.getText().toString().length() == 0 || binding.helpline.getText().toString().length() == 0
            ) {
                Toast.makeText(getApplicationContext(), "Please Configure Accurately ", Toast.LENGTH_SHORT).show();
                return;
            }

            String usrName = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            String serverIP = binding.serverIP.getText().toString();
            String helpLine = binding.helpline.getText().toString();
            int port = Integer.parseInt(binding.port.getText().toString());
            adminConfig.username = usrName;
            adminConfig.password = password;
            adminConfig.server = serverIP;
            adminConfig.port = port;
            adminConfig.helpLine = helpLine;
            adminConfig.deviceID = binding.et.getText().toString();
            ConfirmSettingsActivity.start(SettingsActivity.this, adminConfig);
        });


    }


    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
        activity.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}