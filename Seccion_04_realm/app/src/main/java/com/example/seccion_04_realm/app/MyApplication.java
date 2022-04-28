package com.example.seccion_04_realm.app;

import android.app.Application;
import android.util.Log;

import com.example.seccion_04_realm.models.Board;
import com.example.seccion_04_realm.models.Note;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

//Esta Clase se ejecuta antes del primer activity para el autoincrement
public class MyApplication extends Application {

    public static AtomicInteger BoardID = new AtomicInteger();
    public static AtomicInteger NoteID = new AtomicInteger();


    @Override
    public void onCreate() {
        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        BoardID = getIdByTable(realm, Board.class);
        NoteID = getIdByTable(realm, Note.class);
        realm.close();
        super.onCreate();
        // Run this on the device to find the path on the emulator
        Log.i("Realm file located at: ", realm.getPath());
    }

    private void setUpRealmConfig() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name("seccion_04db.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass) {
        RealmResults<T> results = realm.where(anyClass).findAll();
        //if
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }

}
