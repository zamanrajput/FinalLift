package com.portsip.sipsample.modification.src;

public class KCallBack {
    public interface CallBackInterface
    {
        void sendToData(String str);

    }
    public CallBackInterface CBI;
    public KCallBack(CallBackInterface aCBI)
    {
        CBI=aCBI;
    }
}
