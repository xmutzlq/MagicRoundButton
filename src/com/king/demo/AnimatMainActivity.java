package com.king.demo;

import com.king.demo.animator.FirstActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AnimatMainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, FirstActivity.class));
	}
}
