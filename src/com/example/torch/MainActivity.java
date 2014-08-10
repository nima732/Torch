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
		getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void turnOnLight(View view) {
		boolean hastFlash = false;
		ImageView imageView = (ImageView) view.findViewById(R.id.imageButton1);

		// To check the previous state of Flash and Image
		if (torchOn == false) {
			hastFlash = getApplicationContext().getPackageManager()
					.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
			System.out.println(">>>>>>>>>>>>");
			imageView.setImageResource(R.drawable.light_bulb);

			// Check Flash hardware.
			if (hastFlash) {
				torchOn = true;
				getCamera();
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
	
	protected void onDestroy() {
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
				camera = Camera.open();
				params = camera.getParameters();
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

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
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
		mediaPlayer = MediaPlayer.create(this, R.raw.beep_07);
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
