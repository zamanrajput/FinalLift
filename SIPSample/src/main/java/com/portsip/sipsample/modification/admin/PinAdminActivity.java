package com.portsip.sipsample.modification.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.portsip.R;
import com.portsip.databinding.ActivityPinAdminBinding;
import com.portsip.sipsample.modification.src.BaseActivity;
import com.portsip.sipsample.modification.src.models.Utils;

public class PinAdminActivity extends BaseActivity {
    ActivityPinAdminBinding binding;
    String editTextString = "";

    @Override
    protected void onResume() {
        super.onResume();
        Utils.FullScreen(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinAdminBinding.inflate(getLayoutInflater());
        Utils.FullScreen(this);
        setContentView(binding.getRoot());


        binding.submit.setOnClickListener(v -> {
            String pin = binding.et.getText().toString();
            if (pin.equals("")) {
                Toast.makeText(getApplicationContext(), "Please fill All fields First", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pin.equals("1234")) {
                SettingsActivity.start(PinAdminActivity.this);
                return;
            }
            showToast("Invalid Password");

            /*
            JsonObjectRequest request = new JsonObjectRequest(Constants.URL_ADMIN_LOGIN + pin, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utils.dismissProgressDialog();
                    if (response.toString().contains(Constants.WRONG_RESPONSE_MESSAGE_ADMIN)) {
                        Toast.makeText(getApplicationContext(), Constants.WRONG_RESPONSE_MESSAGE_ADMIN, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SelectDeviceInterfaceActivity.start(PinAdminActivity.this);
                    Toast.makeText(getApplicationContext(), "Authenticated Successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                }
            });

            Utils.showProgressDialog(PinAdminActivity.this, "Authenticating");
            VolleySingleton.getInstance(PinAdminActivity.this).addToRequestQueue(request);
            */
        });
        binding.backBtn.setOnClickListener(b -> onBackPressed());


    }






    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, PinAdminActivity.class));
        activity.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

    }


}