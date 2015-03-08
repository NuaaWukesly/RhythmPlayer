package org.nuaa161140225.rhythmplayer;

import org.nuaa161140225.rhythmplayer.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent myintent = new Intent(LaunchActivity.this,
						RhythmPlayer.class);
				myintent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				LaunchActivity.this.startActivityIfNeeded(myintent, 0);
				LaunchActivity.this.finish();
			}
		}, 2000);
	}
}
