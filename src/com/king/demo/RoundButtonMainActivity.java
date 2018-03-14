package com.king.demo;

import com.king.demo.widget.RoundButton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class RoundButtonMainActivity extends Activity implements OnSeekBarChangeListener {
	
	private RoundButton mR1, mR2, mR3;
	private SeekBar mSeekBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_round_button);
		
		mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
		mSeekBar.setOnSeekBarChangeListener(this);
		
		mR1 = (RoundButton) findViewById(R.id.btn_first);
		mR2 = (RoundButton) findViewById(R.id.btn_second);
		mR3 = (RoundButton) findViewById(R.id.btn_thrid);
		
		change();
	}
	
	private void change() {
		mR1.reSetBg(R.color.color_6293f9, R.color.color_61abfd, true);
		
		mR2.reSetBg(R.color.color_6293f9, R.color.color_61abfd, false);
		
		mR3.setEnabled(false);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		mR1.setCornerRadius(seekBar.getMax() - progress);
		mR2.setCornerRadius(seekBar.getMax() - progress);
		mR3.setCornerRadius(seekBar.getMax() - progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	
}
