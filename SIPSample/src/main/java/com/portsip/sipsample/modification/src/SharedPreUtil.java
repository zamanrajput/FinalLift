package com.portsip.sipsample.modification.src;

import android.content.Context;
import android.content.SharedPreferences;

import com.portsip.sipsample.modification.src.models.AdminConfig;

import java.util.Map;

/**
 * usingSharedPreferencesToStoringData
 */
public class SharedPreUtil {

    Context context;

    public SharedPreUtil(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("Lift_Project1GAZ", 0);
    }

    private SharedPreferences.Editor getEdit() {
        return getSharedPreferences().edit();
    }


    //fields Names
    String DEVICE_ID = "device_id", DEVICE_PORT_NAME = "device_port", DEVICE_BAUD_RATE = "device_baud_rate", SERVER_IP = "server_ip", SERVER_PORT = "process_port", USERNAME = "s_username", SERVER_PASSWORD = "s_password", TRANSFER_PROTOCOL = "t_protocol";


    public void saveConfig(AdminConfig config) {
        put("isConfigured", true);
        setBaudRate(config.baudRate);
        setDeviceID(config.deviceID);
        setDevicePort(config.devicePortName);

        setSERVER_PORT(config.port);
        setSERVER_PASSWORD(config.password);
        setTRANSFER_PROTOCOL(config.transportProtocol);
        setSERVER_IP(config.server);
        setUSERNAME(config.username);
        setHelpLine(config.helpLine);
    }

    public AdminConfig getConfig() {
        boolean isConfigured = getBoolean("isConfigured");
        if (isConfigured) {
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.baudRate = getBaudRate();
            adminConfig.deviceID = getDeviceID();
            adminConfig.devicePortName = getDevicePort();
            adminConfig.server = getServer();
            adminConfig.username = getUserName();
            adminConfig.password = getPassword();
            adminConfig.port = getServerPort();
            adminConfig.transportProtocol = getProtocol();
            adminConfig.helpLine = getHelpLine();
            return adminConfig;
        }
        return null;
    }

    String HELP_LINE = "help_line";

    private String getHelpLine() {
        return getString(HELP_LINE);
    }

    private void setHelpLine(String h) {
        put(HELP_LINE, h);
    }

    public int getServerPort() {
        return getInt(SERVER_PORT);
    }

    public void setSERVER_IP(String s) {
        put(SERVER_IP, s);
    }

    public void setSERVER_PORT(int s) {
        put(SERVER_PORT, s);
    }

    public void setUSERNAME(String s) {
        put(USERNAME, s);
    }

    public void setSERVER_PASSWORD(String s) {
        put(SERVER_PASSWORD, s);
    }

    public void setTRANSFER_PROTOCOL(int s) {
        put(TRANSFER_PROTOCOL, s);
    }

    public int getProtocol() {
        return getInt(TRANSFER_PROTOCOL);
    }

    public String getServer() {
        return getString(SERVER_IP);
    }

    public String getUserName() {
        return getString(USERNAME);
    }

    public String getPassword() {
        return getString(SERVER_PASSWORD);
    }

    public int getBaudRate() {
        return getInt(DEVICE_BAUD_RATE);
    }

    public String getDeviceID() {
        return getString(DEVICE_ID);
    }

    public String getDevicePort() {
        return getString(DEVICE_PORT_NAME);
    }

    public void setDeviceID(String string) {
        put(DEVICE_ID, string);
    }

    public void setDevicePort(String s) {
        put(DEVICE_PORT_NAME, s);
    }

    public void setBaudRate(int s) {
        put(DEVICE_BAUD_RATE, s);
    }

    public void put(String key, Object object) {
        SharedPreferences.Editor editor = getEdit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof String[]) {
            StringBuilder datas = new StringBuilder();
            String[] data = (String[]) object;

            for (int i = 0; i < data.length; ++i) {
                if (i != 0) {
                    datas.append(":");
                }

                datas.append(data[i]);
            }
            editor.putString(key, datas.toString());
        }

        editor.commit();
    }

    public String getString(String key, String defaultObject) {
        return getSharedPreferences().getString(key, defaultObject);
    }

    public String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public float getFloat(String key) {
        return getSharedPreferences().getFloat(key, 0f);
    }

    public long getLong(String key) {
        return getSharedPreferences().getLong(key, 0l);
    }

    public String[] getStringArray(String key) {
        return getString(key).split(":");
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = getEdit();
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        SharedPreferences.Editor editor = getEdit();
        editor.clear();
        editor.commit();
    }

    public boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    public Map<String, ?> getAll() {
        return getSharedPreferences().getAll();
    }
}
