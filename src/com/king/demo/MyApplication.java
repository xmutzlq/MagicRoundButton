package com.king.demo;

import com.king.demo.contact.contacts.Contacts;

import android.app.Application;

public class MyApplication extends Application{
	private static MyApplication instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Contacts.initialize(this);
	}
	
	public static MyApplication getInstance() {
		return instance;
	}
}
