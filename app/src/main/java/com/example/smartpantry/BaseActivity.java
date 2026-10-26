package com.example.smartpantry;

import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected void nav() {
        Button pantry = findViewById(R.id.navPantry);
        Button recipes = findViewById(R.id.navRecipes);
        Button settings = findViewById(R.id.navSettings);

        styleNav(pantry, this instanceof MainActivity);
        styleNav(recipes, this instanceof RecipesActivity || this instanceof RecipeDetailActivity);
        styleNav(settings, this instanceof SettingsActivity);

        if (pantry != null) pantry.setOnClickListener(view -> go(MainActivity.class));
        if (recipes != null) recipes.setOnClickListener(view -> go(RecipesActivity.class));
        if (settings != null) settings.setOnClickListener(view -> go(SettingsActivity.class));
    }

    private void styleNav(Button button, boolean current) {
        if (button == null) return;
        button.setBackgroundResource(R.drawable.nav_background);
        button.setSelected(current);
        button.setTextColor(getResources().getColor(current ? R.color.green : R.color.white));
        button.setAllCaps(false);
        button.setTextSize(14);
        button.setElevation(0);
    }

    protected void go(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
