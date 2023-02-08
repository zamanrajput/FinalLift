package com.portsip.sipsample.ui;

import com.portsip.PortSipErrorcode;
import com.portsip.PortSipSdk;
import com.portsip.R;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.portsip.PortSIPVideoRenderer;
import com.portsip.sipsample.receiver.PortMessageReceiver;
import com.portsip.sipsample.service.PortSipService;
import com.portsip.sipsample.util.CallManager;
import com.portsip.sipsample.util.Session;

public class VideoFragment extends BaseFragment implements View.OnClickListener ,PortMessageReceiver.BroadcastListener{
	MyApplication application;
	MainSIPActivity activity;

	private PortSIPVideoRenderer remoteRenderScreen = null;
	private PortSIPVideoRenderer localRenderScreen = null;
	private PortSIPVideoRenderer remoteRenderSamllScreen = null;
	private PortSIPVideoRenderer.ScalingType scalingType = PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_BALANCED;// SCALE_ASPECT_FIT or SCALE_ASPECT_FILL;
	private ImageButton imgSwitchCamera = null;
	private ImageButton imgScaleType = null;
	private boolean shareInSmall = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = (MainSIPActivity)getActivity();
        application = (MyApplication)activity.getApplication();

        return inflater.inflate(R.layout.video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		imgSwitchCamera = (ImageButton)view.findViewById(R.id.ibcamera);
		imgScaleType = (ImageButton)view.findViewById(R.id.ibscale);

		imgScaleType.setOnClickListener(this);
		imgSwitchCamera.setOnClickListener(this);

		localRenderScreen = (PortSIPVideoRenderer)view.findViewById(R.id.local_video_view);
		remoteRenderScreen = (PortSIPVideoRenderer)view.findViewById(R.id.remote_video_view);
		remoteRenderSamllScreen = (PortSIPVideoRenderer)view.findViewById(R.id.share_video_view);
		remoteRenderSamllScreen.setOnClickListener(this);
        scalingType = PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_FIT;//
        remoteRenderScreen.setScalingType(scalingType);
		activity.receiver.broadcastReceiver =this ;

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		PortSipSdk portSipLib = application.mEngine;
		if(localRenderScreen!=null){
			if(portSipLib!=null) {
				portSipLib.displayLocalVideo(false,false,null);
			}
			localRenderScreen.release();
		}

		CallManager.Instance().setRemoteVideoWindow(application.mEngine,-1,null);//set
		if(remoteRenderScreen!=null){
			remoteRenderScreen.release();
		}

		CallManager.Instance().setShareVideoWindow(application.mEngine,-1,null);//set
		if(remoteRenderSamllScreen!=null){
			remoteRenderSamllScreen.release();
		}
	}

	@Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            localRenderScreen.setVisibility(View.INVISIBLE);
			remoteRenderSamllScreen.setVisibility(View.INVISIBLE);
			stopVideo(application.mEngine);
        }
        else
        {
			updateVideo(application.mEngine);
            activity.receiver.broadcastReceiver = this;
            localRenderScreen.setVisibility(View.VISIBLE);

        }
    }

    @Override
	public void onClick(View v)
	{
		PortSipSdk portSipLib = application.mEngine;
		switch (v.getId())
		{
			case R.id.ibcamera:
				application.mUseFrontCamera = !application.mUseFrontCamera;
				SetCamera(portSipLib, application.mUseFrontCamera);
				break;

			case R.id.share_video_view:

				shareInSmall=!shareInSmall;
				updateVideo(portSipLib);
				break;
			case R.id.ibscale:
				if (scalingType == PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_FIT)
				{
					imgScaleType.setImageResource(R.drawable.aspect_fill);
					scalingType = PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_FILL;
				}
				else if (scalingType == PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_FILL)
				{
					imgScaleType.setImageResource(R.drawable.aspect_balanced);
					scalingType = PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_BALANCED;
				}
				else
				{

					imgScaleType.setImageResource(R.drawable.aspect_fit);
					scalingType = PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_FIT;
				}
				
				localRenderScreen.setScalingType(scalingType);
				remoteRenderScreen.setScalingType(scalingType);
				updateVideo(portSipLib);
				break;
		}
	}

	private void SetCamera(PortSipSdk portSipLib,boolean userFront)
	{
		if (userFront)
		{
			portSipLib.setVideoDeviceId(1);
		}
		else
		{
			portSipLib.setVideoDeviceId(0);
		}
	}

	private void stopVideo(PortSipSdk portSipLib)
	{
		Session cur = CallManager.Instance().getCurrentSession();
		if(portSipLib!=null) {
			portSipLib.displayLocalVideo(false,false,null);
			CallManager.Instance().setRemoteVideoWindow(portSipLib,cur.sessionID,null);
			CallManager.Instance().setConferenceVideoWindow(portSipLib,null);
		}
	}

	public void updateVideo(PortSipSdk portSipLib)
	{
		CallManager callManager = CallManager.Instance();
		Session cur = CallManager.Instance().getCurrentSession();
		if (application.mConference)
		{
			remoteRenderScreen.setVisibility(View.VISIBLE);
			callManager.setConferenceVideoWindow(portSipLib,remoteRenderScreen);
		}else {
			if (cur != null && !cur.IsIdle()
					&& cur.sessionID != PortSipErrorcode.INVALID_SESSION_ID){

				if(cur.hasVideo) {
					localRenderScreen.setVisibility(View.VISIBLE);
					remoteRenderScreen.setVisibility(View.VISIBLE);
					if(cur.bScreenShare){
						remoteRenderSamllScreen.setVisibility(View.VISIBLE);
						callManager.setRemoteVideoWindow(portSipLib,cur.sessionID, null);
						callManager.setShareVideoWindow(portSipLib,cur.sessionID, null);
						if(shareInSmall) {
							callManager.setRemoteVideoWindow(portSipLib,cur.sessionID, remoteRenderScreen);
							callManager.setShareVideoWindow(portSipLib,cur.sessionID, remoteRenderSamllScreen);
							//callManager.se(portSipLib,cur.sessionID, remoteRenderScreen);
						}else{
							callManager.setRemoteVideoWindow(portSipLib,cur.sessionID, remoteRenderSamllScreen);
							callManager.setShareVideoWindow(portSipLib,cur.sessionID, remoteRenderScreen);
						}
					}else{
						remoteRenderSamllScreen.setVisibility(View.GONE);
						callManager.setShareVideoWindow(portSipLib,cur.sessionID, null);
						callManager.setRemoteVideoWindow(portSipLib,cur.sessionID, remoteRenderScreen);
					}
					portSipLib.displayLocalVideo(true, true, localRenderScreen); // display Local video
					portSipLib.sendVideo(cur.sessionID, true);
				}else{
					remoteRenderSamllScreen.setVisibility(View.GONE);
					localRenderScreen.setVisibility(View.GONE);
					remoteRenderScreen.setVisibility(View.VISIBLE);

					portSipLib.displayLocalVideo(false,false, null);
					callManager.setRemoteVideoWindow(portSipLib,cur.sessionID, null);
					if(cur.bScreenShare){
						callManager.setShareVideoWindow(portSipLib,cur.sessionID, remoteRenderScreen);
					}
				}
			}
			else {
				remoteRenderSamllScreen.setVisibility(View.GONE);
				portSipLib.displayLocalVideo(false,false, null);
				callManager.setRemoteVideoWindow(portSipLib,cur.sessionID, null);
			}
		}
	}

    public void onBroadcastReceiver(Intent intent)
	{
		String action = intent == null ? "" : intent.getAction();
		if (PortSipService.CALL_CHANGE_ACTION.equals(action))
		{
			long sessionId = intent.getLongExtra(PortSipService.EXTRA_CALL_SEESIONID, Session.INVALID_SESSION_ID);
			String status = intent.getStringExtra(PortSipService.EXTRA_CALL_DESCRIPTION);
			Session session = CallManager.Instance().findSessionBySessionID(sessionId);
			if (session != null)
			{
				switch (session.state)
				{
					case INCOMING:
						break;
					case TRYING:
						break;
					case CONNECTED:
					case FAILED:
					case CLOSED:
						updateVideo(application.mEngine);
						break;

				}
			}
		}
	}
}
