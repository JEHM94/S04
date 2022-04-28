package com.example.seccion_04_realm.models;

import com.example.seccion_04_realm.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Board extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String tittle;
    @Required
    private Date createdAt;

    //Relación uno>muchos
    private RealmList<Note> notes;

    //Realm solicita un constructor vacío
    public Board() {

    }

    public Board(String tittle) {
        this.id = MyApplication.BoardID.incrementAndGet();
        this.tittle = tittle;
        this.createdAt = new Date();
        this.notes = new RealmList<Note>();
    }

    public int getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public Date getCreatedAt() {
        return createdAt;
    }


    public RealmList<Note> getNotes() {
        return notes;
    }
}
