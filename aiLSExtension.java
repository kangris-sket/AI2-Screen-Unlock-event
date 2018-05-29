package com.zacharytipnis.aiLSExtension;

import android.content.Context;      
import android.util.Log;  

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.collect.Sets;
import com.google.appinventor.components.runtime.util.BoundingBox;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.PaintUtil;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.*;

@DesignerComponent(version = aiLSExtension.VERSION,
    description = "A component to take do complex image processing! " +
        "That cannot be done with normal AppInventor components or blocks " +
        "This is the guinea pig for CDK Experiments done secretly at MIT " +
        "The details regarding this component is classified top secret in a google docs " +
        "Pssst... Dont let the government know!!",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")

@SimpleObject(external = true)
public class aiLSExtension extends AndroidNonvisibleComponent implements Component {
	public static final int VERSION = 1;
	private ComponentContainer container;

	public aiLSExtension(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        Log.d("CDK", "aiLSExtension Created" );
    }
	public class LockScreenService extends Service {
	    BroadcastReceiver receiver;

	    @Override
	    public IBinder onBind(Intent intent) {
	        return null;
	    }

	    @Override
	    @SuppressWarnings("deprecation")
	    public void onCreate() {
	        KeyguardManager.KeyguardLock key;
	        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

	        //This is deprecated, but it is a simple way to disable the lockscreen in code
	        key = km.newKeyguardLock("IN");

	        key.disableKeyguard();

	        //Start listening for the Screen On, Screen Off, and Boot completed actions
	        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	        filter.addAction(Intent.ACTION_SCREEN_OFF);
	        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

	        //Set up a receiver to listen for the Intents in this Service
	        receiver = new LockScreenReceiver();
	        registerReceiver(receiver, filter);

	        super.onCreate();
	    }

	    @Override
	    public void onDestroy() {
	        unregisterReceiver(receiver);
	        super.onDestroy();
	    }
	}

	@SimpleEvent
    public void Unlock(){
        EventDispatcher.dispatchEvent(this, "Unlock");
    }

	public class LockScreenReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();

	        //If the screen was just turned on or it just booted up, start your Lock Activity
	        if(action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_BOOT_COMPLETED))
	        {
	        	aiLSExtension.this.Unlock();
	        }
	    }
	}
}