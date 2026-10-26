package com.example.smartpantry;

import java.util.ArrayList;

public class Recipe {
    public long id;
    public String name;
    public String method;
    public ArrayList<Requirement> ingredients = new ArrayList<>();

    public Recipe(long id, String name, String method) {
        this.id = id;
        this.name = name;
        this.method = method;
    }

    public static class Requirement {
        public String name;
        public String unit;
        public double quantity;

        public Requirement(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }
}
