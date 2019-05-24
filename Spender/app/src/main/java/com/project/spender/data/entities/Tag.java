package com.project.spender.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

/**
 * Таблица тегов
 */
@Entity(indices = {@Index(value = "name", unique = true ), @Index(value = "color")})
public class Tag {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private int color;

    @Ignore
    public Tag(String name) {
        this(0, name);
    }

    @Ignore
    public Tag(long id, String name) {
        this(id, name, 0);
    }


    /**
     * Spoiled your code a little
     * I think it ll be better to store colors in database too
     */
    public Tag(long id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    @Ignore
    public Tag(String newTagName, int newTagColor) {
        this(0, newTagName, newTagColor);
    }

    public int getColor() {
        return color;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name) && Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
