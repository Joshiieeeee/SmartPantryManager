package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "pantry.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,expiry TEXT)");
        db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,method TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_id INTEGER NOT NULL,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL)");

        addRecipe(db, "Egg Toast", "Toast bread. Fry eggs in a pan, then place them on the toast.", "bread|2|pieces,egg|2|pieces");
        addRecipe(db, "Tomato Sandwich", "Slice the tomato and put it between two pieces of bread.", "bread|2|pieces,tomato|1|pieces");
        addRecipe(db, "Cheese Toastie", "Put cheese between bread slices and toast in a pan.", "bread|2|pieces,cheese|40|g");
        addRecipe(db, "Scrambled Eggs", "Beat eggs with milk and cook slowly in a pan.", "egg|2|pieces,milk|30|ml");
        addRecipe(db, "Banana Oats", "Cook oats with milk and top with sliced banana.", "oats|50|g,milk|200|ml,banana|1|pieces");
        addRecipe(db, "Peanut Butter Toast", "Spread peanut butter on toasted bread.", "bread|2|pieces,peanut butter|30|g");
        addRecipe(db, "Tomato Pasta", "Cook pasta and mix with chopped tomato and oil.", "pasta|100|g,tomato|2|pieces,oil|15|ml");
        addRecipe(db, "Cheesy Pasta", "Cook pasta. Stir through milk and grated cheese.", "pasta|100|g,cheese|50|g,milk|50|ml");
        addRecipe(db, "Rice and Beans", "Cook rice and beans separately, then combine.", "rice|100|g,beans|150|g");
        addRecipe(db, "Garlic Rice", "Cook rice and stir in garlic and oil.", "rice|100|g,garlic|10|g,oil|15|ml");
        addRecipe(db, "Potato Mash", "Boil potatoes and mash with milk.", "potato|2|pieces,milk|50|ml");
        addRecipe(db, "Fried Potatoes", "Cut potatoes and fry gently in oil.", "potato|2|pieces,oil|30|ml");
        addRecipe(db, "Egg Fried Rice", "Cook rice. Fry egg and combine with rice and oil.", "rice|100|g,egg|1|pieces,oil|15|ml");
        addRecipe(db, "Banana Pancakes", "Mash banana with eggs and fry small pancakes.", "banana|1|pieces,egg|2|pieces,oil|10|ml");
        addRecipe(db, "Yoghurt Banana Bowl", "Slice banana into yoghurt.", "yoghurt|150|g,banana|1|pieces");
        addRecipe(db, "Bean Toast", "Heat cooked beans and spoon over toast.", "beans|150|g,bread|2|pieces");
        addRecipe(db, "Tomato Omelette", "Cook beaten eggs with chopped tomato.", "egg|2|pieces,tomato|1|pieces");
        addRecipe(db, "Cheese Omelette", "Cook eggs with grated cheese.", "egg|2|pieces,cheese|40|g");
        addRecipe(db, "Peanut Butter Oats", "Cook oats in milk, then stir in peanut butter.", "oats|50|g,milk|200|ml,peanut butter|20|g");
        addRecipe(db, "Simple Tomato Salad", "Mix diced tomatoes with oil.", "tomato|2|pieces,oil|15|ml");
    }

    private void addRecipe(SQLiteDatabase db, String name, String method, String required) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("method", method);
        long id = db.insert("recipes", null, values);

        for (String entry : required.split(",")) {
            String[] parts = entry.split("\\|");
            ContentValues ingredient = new ContentValues();
            ingredient.put("recipe_id", id);
            ingredient.put("name", parts[0]);
            ingredient.put("quantity", Double.parseDouble(parts[1]));
            ingredient.put("unit", parts[2]);
            db.insert("recipe_ingredients", null, ingredient);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No schema changes in version 1.
    }

    public long saveIngredient(long id, String name, double quantity, String unit, String expiry) {
        ContentValues values = new ContentValues();
        values.put("name", name.trim());
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry", expiry);

        if (id > 0) {
            getWritableDatabase().update("pantry", values, "id=?", new String[]{String.valueOf(id)});
            return id;
        }
        return getWritableDatabase().insert("pantry", null, values);
    }

    public void deleteIngredient(long id) {
        getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }

    public Ingredient getIngredient(long id) {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id,name,quantity,unit,expiry FROM pantry WHERE id=?",
                new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) return fromCursor(cursor);
        }
        return null;
    }

    private Ingredient fromCursor(Cursor cursor) {
        return new Ingredient(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2),
                cursor.getString(3), cursor.getString(4));
    }

    public ArrayList<Ingredient> allIngredients() {
        ArrayList<Ingredient> data = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id,name,quantity,unit,expiry FROM pantry ORDER BY name COLLATE NOCASE", null)) {
            while (cursor.moveToNext()) data.add(fromCursor(cursor));
        }
        return data;
    }

    public ArrayList<Recipe> allRecipes() {
        ArrayList<Recipe> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT id,name,method FROM recipes ORDER BY name", null)) {
            while (cursor.moveToNext()) {
                data.add(new Recipe(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            }
        }

        for (Recipe recipe : data) {
            loadRequirements(db, recipe);
        }
        return data;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Recipe recipe;
        try (Cursor cursor = db.rawQuery(
                "SELECT id,name,method FROM recipes WHERE id=?",
                new String[]{String.valueOf(id)})) {
            if (!cursor.moveToFirst()) return null;
            recipe = new Recipe(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
        }
        loadRequirements(db, recipe);
        return recipe;
    }

    private void loadRequirements(SQLiteDatabase db, Recipe recipe) {
        try (Cursor cursor = db.rawQuery(
                "SELECT name,quantity,unit FROM recipe_ingredients WHERE recipe_id=?",
                new String[]{String.valueOf(recipe.id)})) {
            while (cursor.moveToNext()) {
                recipe.ingredients.add(new Recipe.Requirement(
                        cursor.getString(0), cursor.getDouble(1), cursor.getString(2)));
            }
        }
    }
}
