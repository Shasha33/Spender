package com.project.spender.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Таблица тегов
 */
@Entity(indices = {@Index(value = "name", unique = true )})
public class Tag {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    @Ignore
    public Tag(String name) {
        this(0, name);
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
