package com.example.moviesearcher.model.data;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Category extends ExpandableGroup<Subcategory> {
    private int id;
    private String name;

    public Category(String name, List<Subcategory> items) {
        super(name, items);
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
