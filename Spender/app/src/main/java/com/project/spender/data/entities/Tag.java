package com.project.spender.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
    private String substring;

    @Ignore
    public Tag(String name) {
        this(0, name);
    }

    @Ignore
    public Tag(long id, String name) {
        this(id, name, 0);
    }


    public Tag(long id, String name, int color, String substring) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.substring = "";
    }

    @Ignore
    public Tag(long id, String name, int color) {
        this(id, name, color, "");
    }

    @Ignore
    public Tag(String newTagName, int newTagColor) {
        this(0, newTagName, newTagColor);
    }

    public int getColor() {
        return color;
    }

    public String getSubString() {return substring;}

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
