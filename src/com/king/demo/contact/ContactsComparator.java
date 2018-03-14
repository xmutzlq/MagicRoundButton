package com.king.demo.contact;

import java.util.Comparator;

import com.king.demo.contact.contacts.Contact;

public class ContactsComparator implements Comparator<Contact> {

    @Override
    public int compare(Contact l, Contact r) {
    	//FIX 需要做空判断
        String lhsSortLetters = String.valueOf(HanziToPinyin.getPinYin(l.getDisplayName()).toLowerCase().charAt(0));
        String rhsSortLetters = String.valueOf(HanziToPinyin.getPinYin(r.getDisplayName()).toLowerCase().charAt(0));
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
