package com.nao20010128nao.Sky.Music;
import android.app.*;
import android.os.*;
import com.android.music.*;
import com.nao20010128nao.SpoofBrowser.classes.*;
import android.view.*;
import android.widget.*;
import android.util.*;
import android.content.*;
import java.lang.reflect.*;
import com.nao20010128nao.MusicAppAnother.*;

public class MultiScreenActivity extends ActivityGroup /*implements ActivityTools.ActivityChangeable*/{
	//This activity must call from Lollipop
	LocalActivityManager lam=getLocalActivityManager();
	Window localWindow;ViewGroup prevDecor;
	boolean foreverLoopProtection=false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buttonbar);
		int activeTab = Tools.getSettings("activetab",R.id.artisttab,this);
        if (activeTab != R.id.artisttab
			&& activeTab != R.id.albumtab
			&& activeTab != R.id.songtab
			&& activeTab != R.id.playlisttab) {
            activeTab = R.id.artisttab;
        }
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		onActivityChangeRequest(activeTab);
		//getWindow().setNavigationBarColor(0);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if(foreverLoopProtection)return super.onPrepareOptionsMenu(menu);
		foreverLoopProtection=true;
		try{
			return ((Activity)localWindow.getContext()).onPrepareOptionsMenu(menu);
		}finally{
			foreverLoopProtection=false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if(foreverLoopProtection)return super.onCreateOptionsMenu(menu);
		foreverLoopProtection=true;
		menu.clear();
		try{
			return ((Activity)localWindow.getContext()).onCreateOptionsMenu(menu);
		}finally{
			foreverLoopProtection=false;
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		// TODO: Implement this method
		if(foreverLoopProtection)return super.onMenuItemSelected(featureId,item);
		foreverLoopProtection=true;
		try{
			return ((Activity)localWindow.getContext()).onMenuItemSelected(featureId, item);
		}finally{
			foreverLoopProtection=false;
		}
	}
	
	@Override
	public void onActivityChangeRequest(int activeTab){
		// TODO: Implement this method
		localWindow=MusicUtils.activateTab((ActivityGroup)this, activeTab);
		ViewGroup vg=(ViewGroup)findViewById(R.id.mainFrameContent);
		removeFromParent(prevDecor);
		vg.addView(prevDecor=(ViewGroup)localWindow.getDecorView());
		if(localWindow.getContext()!=this){
			Log.d("Debug","localWindow.getContext()!=this");
			getIntent().putExtra("withtabs",true);
			MusicUtils.updateButtonBar(this, activeTab);
		}
	}
	public void removeFromParent(View v){
		if(v!=null)
			((ViewGroup)v.getParent()).removeView(v);
	}
}
