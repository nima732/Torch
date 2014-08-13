package com.example.torch;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import android.media.MediaPlayer.OnCompletionListener;


public class MainActivity extends Activity {
	boolean torchOn = false;
	Camera camera;
	Parameters params;
	MediaPlayer mediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().																	// Retrieve current window for activity
		getDecorView().																	// Retrieve the top-level window decor view
		setBackgroundColor(Color.LTGRAY);
																				

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public void turnOnLight(View view) {												// This method be called in Activity XML
		boolean hastFlash = false;	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageButton1);

																						// To set imageview to transparent, it should be set via xml file.		
		
//		imageView.setScaleType(ScaleType.FIT_XY);										// To fit to screen
		
																					
		if (torchOn == false) {															// To check the previous state of Flash and Image
			hastFlash = getApplicationContext()											// Return the context of the single, global Application object 
																						// of the current process. This generally should only be used if 
																						// you need a Context whose lifecycle is separate from the current 
																						// context, 
																						// that is tied to the lifetime of the process rather than the 
																						// current component. 
					.getPackageManager()
					.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
			System.out.println(">>>>>>>>>>>>");
			imageView.setImageResource(R.drawable.light_bulb);

			
			if (hastFlash) {															// Check Flash hardware.
				torchOn = true;
				getCamera();															// To initiate camera object.
				turnOnFlash();	
			} else {
				AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
						.create();
				alert.setTitle("Error");
				alert.setMessage("Sorry, your device doesn't support flash light!");
				alert.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// closing the application
						finish();
					}
				});
				alert.show();

			}
		} else {
			System.out.println("@@@@@@@@");
			imageView.setImageResource(R.drawable.bulb_icon);
			getCamera();
			turnOffFlash();
			torchOn = false;

		}

	}
	
	protected void onDestroy() {														// This method is important, otherwise the app will not release camera 
																						// does not work.
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (camera != null){
        	camera.release();
        	camera = null;
        }
        
        if (params != null){
        	params =  null;
        }
    }


	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();														// Creates a new Camera object to access the first back-facing 
																							// camera on the device.
				params = camera.getParameters();											// Return current setting for this camera device.
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}

	private void turnOnFlash() {
		if (torchOn) {
			if (camera == null || params == null) {
				return;
			}
			try {
				playSound("mySound");														// play sound
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
//			catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);								// To make camera parameters take effect, 
																							// applications have to call setParameters(Camera.Parameters). 
			camera.setParameters(params);
			camera.startPreview();															// Starts capturing and drawing preview frames to the screen. 
																							// Preview will not actually start until a surface is supplied 
																							// with setPreviewDisplay(SurfaceHolder) or setPreviewTexture(SurfaceTexture). 
			torchOn = true;

			// changing button/switch image
			// toggleButtonImage();
		}

	}

	private void turnOffFlash() {
		if (torchOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			try {
				playSound("mySound");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
//			catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			System.out.println("###########");
			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			camera.stopPreview();
			// isFlashOn = false;

			// changing button/switch image
			// toggleButtonImage();
		}
	}

	public void playSound() throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {

		try {
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		    r.play();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	private void playSound(String mySound){
		mediaPlayer = MediaPlayer.create(this, R.raw.beep_07);								// Using MediaPlayer to play sound. I have download a beep file to 
																							// play. Then put the file in new created folder -raw-.
		mediaPlayer.setLooping(false);	
		Log.e("beep","started0");
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer paramMediaPlayer) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
}
