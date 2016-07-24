package bravostudio.nyeni.Custom;


import java.io.IOException;

import android.app.Activity;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;

import bravostudio.nyeni.R;


public class ArchitectActivity1 extends Activity {
    private static final String TAG = ArchitectActivity1.class.getSimpleName();
	protected boolean isLoading = false;

	ArchitectView architectView;
	StartupConfiguration startupConfiguration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_architect1);

		this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
		
//		To use the Wikitude Android SDK you need to provide a valid license key to the onCreate lifecycle-method of the ArchitectView. 
//		This can either be done directly by providing the key as a string and the call the onCreate(final String key) method or creating an 
//		StartupConfiguration object, passing it the license as a string and then call the onCreate(final StartupConfiguration config) method.
//		Please refer to the AbstractArchitectCamActivity of the SDK Examples project for a practical example of how to set the license key.
		final StartupConfiguration config = new StartupConfiguration(NyeniConstant.WIKITUDE_SDK_KEY);
	    this.architectView.onCreate( config );		
		
	}
	
	@Override
	protected void onPostCreate( final Bundle savedInstanceState ) {
	    super.onPostCreate( savedInstanceState );

	    if ( this.architectView != null ) {

	        // call mandatory live-cycle method of architectView
	        this.architectView.onPostCreate();

	        try {
	            // load content via url in architectView, ensure '<script src="architect://architect.js"></script>' is part of this HTML file,
	        	// have a look at wikitude.com's developer section for API references
	        	//
//	            String s= getAssets().open("samples/Test1/index.html").toString();
//Log.e(TAG, "LOCATION="+s);
//	            this.architectView.load(getAssets().open("samples/Test1/index.html").toString());

	        	//for some odd reason it doesn't always load the files from the assets
	        	//also, do we REALLY want to embed the stuff inside the app?
	        	//it makes more sense to host the files on a web-server outside of the app
	        	//allows for making changes outside of the app
	            this.architectView.load("http://192.168.1.107/nyeni/index.html");

	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }

	    }
	}

	public static final boolean isVideoDrawablesSupported() {
		String extensions = GLES20.glGetString( GLES20.GL_EXTENSIONS );
		return extensions != null && extensions.contains( "GL_OES_EGL_image_external" ) && android.os.Build.VERSION.SDK_INT >= 14 ;
	}
	
	@Override
	public void onResume() {
	    super.onResume();

	    this.architectView.onResume();
	}

	@Override
	protected void onPause() {
	    super.onPause();

	    // call mandatory live-cycle method of architectView
	    if ( this.architectView != null ) {
	        this.architectView.onPause();
	    }
	}

	@Override
	protected void onStop() {
	    super.onStop();
	}

	@Override
	protected void onDestroy() {
	    super.onDestroy();

	    // call mandatory live-cycle method of architectView
	    if ( this.architectView != null ) {
	        this.architectView.onDestroy();
	    }
	}

	@Override
	public void onLowMemory() {
	    super.onLowMemory();
	    if ( this.architectView != null ) {
	        this.architectView.onLowMemory();
	    }
	}	
	
}
