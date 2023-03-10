package com.app.activeparks.data.model.dictionaries;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class ExerciseCategory {

    @PrimaryKey(autoGenerate = true)
    public int keyid;

    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    public String id = "";

    @SerializedName("title")
    @ColumnInfo(name = "title")
    @Expose
    public String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


//    @SerializedName("subcategories")
//    @Expose
//    private List<Subcategory> subcategories = null;
//
//    public List<Subcategory> getSubcategories() {
//        return subcategories;
//    }
//
//    public void setSubcategories(List<Subcategory> subcategories) {
//        this.subcategories = subcategories;
//    }

}