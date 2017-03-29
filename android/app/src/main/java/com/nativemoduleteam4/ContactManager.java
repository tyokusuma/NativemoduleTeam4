package com.nativemoduleteam4;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;

import java.util.ArrayList;

public class ContactManager extends ReactContextBaseJavaModule {

    public ContactManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /*
     * Returns all contactable records on phone
     * queries CommonDataKinds.Contactables to get phones and emails
     */
    @ReactMethod
    public void getAll(final Callback callback) {
        getAllContacts(callback);
    }

    /**
     * Introduced for iOS compatibility.  Same as getAll
     *
     * @param callback callback
     */
    @ReactMethod
    public void getAllContacts(final Callback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Context context = getReactApplicationContext();
                ContentResolver cr = context.getContentResolver();

                ContactProvider contactsProvider = new ContactProvider(cr);
                WritableArray contacts = contactsProvider.getContacts();

                callback.invoke(null, contacts);
            }
        });
    }

    @ReactMethod
    public void requestPermission(Callback callback) {
        callback.invoke(null, isPermissionGranted());
    }
    private String isPermissionGranted() {
        String permission = "android.permission.READ_CONTACTS";

        int res = getReactApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED) ? "authorized" : "denied";
    }
    @Override
    public String getName() {
        return "Contacts";
    }

}
