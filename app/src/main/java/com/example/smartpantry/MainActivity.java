package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private DatabaseHelper db;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_polished);
        db = new DatabaseHelper(this);
        list = findViewById(R.id.itemList);
        nav();

        findViewById(R.id.addButton).setOnClickListener(
                view -> startActivity(new Intent(this, EditIngredientActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (list != null) refresh();
    }

    private void refresh() {
        ArrayList<Ingredient> items = db.allIngredients();
        TextView emptyText = findViewById(R.id.emptyText);
        emptyText.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        PantryAdapter adapter = new PantryAdapter(this, items, ingredient -> {
            Intent intent = new Intent(this, EditIngredientActivity.class);
            intent.putExtra("id", ingredient.id);
            startActivity(intent);
        });
        list.setAdapter(adapter);
    }
}
