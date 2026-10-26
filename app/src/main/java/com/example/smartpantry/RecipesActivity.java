package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RecipesActivity extends BaseActivity {
    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        nav();
        db = new DatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRecipes();
    }

    private void showRecipes() {
        LinearLayout list = findViewById(R.id.recipeList);
        list.removeAllViews();
        List<Ingredient> pantry = db.allIngredients();
        int count = 0;

        for (Recipe recipe : db.allRecipes()) {
            if (!Matching.canMake(recipe, pantry)) continue;

            count++;
            View row = getLayoutInflater().inflate(R.layout.row_item, list, false);
            ((TextView) row.findViewById(R.id.rowTitle)).setText(recipe.name);
            row.findViewById(R.id.rowSubtitle).setVisibility(View.GONE);
            row.setContentDescription(recipe.name);
            row.setOnClickListener(view -> {
                Intent intent = new Intent(this, RecipeDetailActivity.class);
                intent.putExtra("id", recipe.id);
                startActivity(intent);
            });
            list.addView(row);
        }

        TextView noRecipes = findViewById(R.id.noRecipes);
        noRecipes.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (db != null) db.close();
        super.onDestroy();
    }
}
