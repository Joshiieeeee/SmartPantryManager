package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class RecipeDetailActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        nav();

        Recipe recipe = new DatabaseHelper(this).getRecipe(getIntent().getLongExtra("id", -1));
        if (recipe == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.detailTitle)).setText(recipe.name);
        StringBuilder ingredients = new StringBuilder();
        for (Recipe.Requirement ingredient : recipe.ingredients) {
            ingredients.append("• ")
                    .append(ingredient.name)
                    .append(": ")
                    .append(String.format(Locale.getDefault(), "%s", ingredient.quantity))
                    .append(" ")
                    .append(ingredient.unit)
                    .append("\n");
        }
        ((TextView) findViewById(R.id.detailIngredients)).setText(ingredients.toString());
        ((TextView) findViewById(R.id.detailSteps)).setText(recipe.method);
    }
}
