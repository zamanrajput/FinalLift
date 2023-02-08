package com.portsip.sipsample.modification.src;



import static com.portsip.sipsample.modification.src.LockApi.SUCCESS;
import static com.portsip.sipsample.modification.src.LockApi.closePower;
import static com.portsip.sipsample.modification.src.LockApi.connectWithBoard;
import static com.portsip.sipsample.modification.src.LockApi.getAllState;
import static com.portsip.sipsample.modification.src.LockApi.getState;
import static com.portsip.sipsample.modification.src.LockApi.openAllLock;
import static com.portsip.sipsample.modification.src.LockApi.openLock;
import static com.portsip.sipsample.modification.src.LockApi.openPower;
import static com.portsip.sipsample.modification.src.LockApi.sendData;
import static com.portsip.sipsample.modification.src.LockApi.setCommLock;
import static com.portsip.sipsample.modification.src.LockApi.toByteArray;
import static com.portsip.sipsample.modification.src.models.Utils.launcher;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.portsip.R;
import com.portsip.sipsample.modification.src.models.AdminConfig;

import java.util.regex.Pattern;

public class MainBoardActivity extends BaseActivity {

    EditText boardNumEt, lockNumEt, sendCommandEt;
    TextView tvLog;

    private void showMessage(String text) {
        runOnUiThread(() -> Toast.makeText(MainBoardActivity.this, text, Toast.LENGTH_SHORT).show());
    }

    public void writeLog(String text) {
        runOnUiThread(() -> tvLog.append(text));
    }

    private void initView() {
        boardNumEt = findViewById(R.id.boardNumberEt);
        lockNumEt = findViewById(R.id.lockNumberEt);
        sendCommandEt = findViewById(R.id.sendCommandEt);
        tvLog = findViewById(R.id.tvLog);
        tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        SharedPreUtil sharedPreUtil = new SharedPreUtil(MainBoardActivity.this);
        AdminConfig adminConfig = sharedPreUtil.getConfig();

        if (adminConfig == null) {

        } else {
            try {
                connectWithBoard(adminConfig, s -> launcher(MainBoardActivity.this, () -> {
                    if (s.equals(SUCCESS)) {
                        Toast.makeText(getApplicationContext(), "Successfully Connected With Board", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                }));

            } catch (Exception e) {
                e.printStackTrace();
                launcher(MainBoardActivity.this, () -> Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show());
            }

        }


        //Initialize the serial port, incoming processing serial port reception
        MApp.mApp.initCOM(new KCallBack.CallBackInterface() {
            @Override
            public void sendToData(String str) {
                //do something
                writeLog("Serial port receiving：" + str + "\n");
            }
        });

        //Set serial port
        findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Connect to the serial port
        findViewById(R.id.btnConn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreUtil sp = new SharedPreUtil(MainBoardActivity.this);
                        String[] rsMsg = {""};
//                        setCommLock(sp.getString("DEVICE"), Integer.parseInt( sp.getString("BAUDRATE")), rsMsg);
                        setCommLock("/dev/ttyS4", 9600, rsMsg);
                        showMessage("Connect the control board," + rsMsg[0]);

                    }
                }).start();
            }
        });


        //unlock
        findViewById(R.id.btnUnlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    if (!isInteger(boardNumEt.getText().toString()) || !isInteger(lockNumEt.getText().toString())) {
                        showMessage("Please enter a valid board number and lock number");
                        return;
                    }
                    String[] rsMsg = {""};
                    openLock(Byte.parseByte(boardNumEt.getText().toString()), Byte.parseByte(lockNumEt.getText().toString()), rsMsg);
                    showMessage("unlock," + rsMsg[0]);
                }).start();


            }
        });

        //all locks
        findViewById(R.id.btnUnlockAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isInteger(boardNumEt.getText().toString()) == false) {
                            showMessage("Please enter a valid board number");
                            return;
                        }
                        String[] rsMsg = {""};
                        openAllLock(Byte.valueOf(boardNumEt.getText().toString()), rsMsg);
                        showMessage("all unlocked, complete");
                    }
                }).start();


            }
        });

        //state
        findViewById(R.id.btnState).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isInteger(boardNumEt.getText().toString()) == false || isInteger(lockNumEt.getText().toString()) == false) {
                            showMessage("Please enter a valid board number and lock number");
                            return;
                        }
                        String[] rsMsg = {""};
                        getState(Byte.valueOf(boardNumEt.getText().toString()), Byte.valueOf(lockNumEt.getText().toString()), rsMsg);
                        showMessage("mode," + rsMsg[0]);
                    }
                }).start();

            }
        });

        //所有状态
        findViewById(R.id.btnAllStates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isInteger(boardNumEt.getText().toString()) == false) {
                            showMessage("Please enter a valid board number");
                            return;
                        }
                        String[] rsMsg = {""};
                        getAllState(Byte.valueOf(boardNumEt.getText().toString()), rsMsg);
                        showMessage("All States," + rsMsg[0]);
                    }
                }).start();

            }
        });

        //通电
        findViewById(R.id.btnPowerUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isInteger(boardNumEt.getText().toString()) == false || isInteger(lockNumEt.getText().toString()) == false) {
                            showMessage("Please enter a valid board number and lock number");
                            return;
                        }
                        String[] rsMsg = {""};
                        openPower(Byte.valueOf(boardNumEt.getText().toString()), Byte.valueOf(lockNumEt.getText().toString()), rsMsg);
                        showMessage("Energize," + rsMsg[0]);
                    }
                }).start();


            }
        });

        //断电
        findViewById(R.id.btnPowerOutage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isInteger(boardNumEt.getText().toString()) == false || isInteger(lockNumEt.getText().toString()) == false) {
                            showMessage("Please enter a valid board number and lock number");
                            return;
                        }
                        String[] rsMsg = {""};
                        closePower(Byte.valueOf(boardNumEt.getText().toString()), Byte.valueOf(lockNumEt.getText().toString()), rsMsg);
                        showMessage("power outage," + rsMsg[0]);
                    }
                }).start();
            }
        });

        //发送
        findViewById(R.id.btnSent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (sendCommandEt.getText().toString().trim().equals("")) {
                            showMessage("Please enter the sending hexadecimal content");
                            return;
                        }
                        String[] rsMsg = {""};
                        byte[] to_send = toByteArray(sendCommandEt.getText().toString());
                        if (sendData(to_send, rsMsg)) {
                            writeLog("take over:" + rsMsg[0] + "\n");
                        } else {
                            writeLog("Sending failed, or the serial port is not connected!" + "\n");
                        }
                    }
                }).start();
            }
        });

        //清空
        findViewById(R.id.btnCls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLog.setText("");
            }
        });


    }

    private void showToast(String[] rsMsg) {
        Toast.makeText(getApplicationContext(), rsMsg[0], Toast.LENGTH_SHORT).show();
    }


}
