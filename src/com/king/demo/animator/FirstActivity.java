package com.king.demo.animator;

import com.king.demo.R;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FirstActivity extends Activity implements SwichLayoutInterFace {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_first);
		findViewById(R.id.go_).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SwitchLayout.get3DRotateFromLeft(FirstActivity.this, false, null);
//				Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//				startActivity(intent);
			}
		});
	}

	@Override
	public void setEnterSwichLayout() {
		 SwitchLayout.get3DRotateFromLeft(this, false, null);
	}

	@Override
	public void setExitSwichLayout() {
		SwitchLayout.get3DRotateFromRight(this, false, null);
	}
	
}
