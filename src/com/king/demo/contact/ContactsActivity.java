package com.king.demo.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.gson.GsonBuilder;
import com.king.demo.R;
import com.king.demo.contact.WaveSideBarView.OnTouchLetterChangeListener;
import com.king.demo.contact.contacts.Contact;
import com.king.demo.contact.contacts.Contacts;
import com.king.demo.contact.contacts.Query;
import com.king.demo.task.base.Continuation;
import com.king.demo.task.base.Task;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class ContactsActivity extends Activity implements OnTouchLetterChangeListener {
	private static final String TAG = ContactsActivity.class.getSimpleName();
	private static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 76;
	
	private WaveSideBarView mWaveSideBarView;
	
	private StickyListHeadersListView mStickyListHeadersListView;
	private ContactsAdapter mContactsAdapter;
	
	private ProgressDialog pd;
	
	private boolean isFirst;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_contact);
		mWaveSideBarView = (WaveSideBarView) findViewById(R.id.contact_sidebar);
		mStickyListHeadersListView = (StickyListHeadersListView) findViewById(R.id.contact_lay);
		mContactsAdapter = new ContactsAdapter(this, new ArrayList<Contact>());
		mStickyListHeadersListView.setAdapter(mContactsAdapter);
		
		mWaveSideBarView.setOnTouchLetterChangeListener(this);
		pd = new ProgressDialog(this);
		pd.setMessage("Loading...");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isFirst) {
			isFirst = true;
			checkPermission();
		}
	}
	
	private void queryContacts() {
		showProgress();
        Task.callInBackground(new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                Query q = Contacts.getQuery();
                q.include(Contact.Field.ContactId, Contact.Field.DisplayName, 
                		Contact.Field.PhoneNumber, Contact.Field.PhoneNormalizedNumber, Contact.Field.Email);
                List<Contact> contacts = q.find();
                return contacts;
            }
        }).continueWith(new Continuation<List<Contact>, Void>() {
            @Override
            public Void then(Task<List<Contact>> task) throws Exception {
            	dissmissProgress();
                if (task.isFaulted()) {
                	Toast.makeText(ContactsActivity.this, "找不到联系人", Toast.LENGTH_SHORT).show();
                } else if(task.isCompleted()) {
                	mContactsAdapter.appendToTopList(new ArrayList<Contact>(task.getResult()));
                }
                return null;
            }
        });
    }
	
	private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            queryContacts();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACT_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == READ_CONTACT_PERMISSION_REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            queryContacts();
        }
    }

	@Override
	public void onLetterChange(String letter) {
		int pos = mContactsAdapter.getLetterPosition(letter);
        if (pos != -1) {
        	mStickyListHeadersListView.setSelection(pos);
        }
	}
	
	private void showProgress() {
		if(pd != null && !pd.isShowing()) {
			pd.show();
		}
	}
	
	private void dissmissProgress() {
		if(pd != null) {
			pd.dismiss();
		}
	}
}
