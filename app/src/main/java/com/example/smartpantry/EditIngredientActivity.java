package com.example.smartpantry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditIngredientActivity extends BaseActivity {
    private EditText name;
    private EditText quantity;
    private EditText expiry;
    private Spinner units;
    private long id;
    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        nav();
        db = new DatabaseHelper(this);

        name = findViewById(R.id.inputName);
        quantity = findViewById(R.id.inputQuantity);
        expiry = findViewById(R.id.inputExpiry);
        units = findViewById(R.id.inputUnit);

        String[] options = {"pieces", "g", "kg", "ml", "l", "tbsp", "tsp"};
        units.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, options));

        id = getIntent().getLongExtra("id", -1);
        if (id > 0) {
            Ingredient ingredient = db.getIngredient(id);
            if (ingredient != null) {
                name.setText(ingredient.name);
                quantity.setText(String.valueOf(ingredient.quantity));
                expiry.setText(ingredient.expiry);
                for (int index = 0; index < options.length; index++) {
                    if (options[index].equals(ingredient.unit)) units.setSelection(index);
                }
            }
        }

        Button save = findViewById(R.id.saveButton);
        save.setBackgroundResource(R.drawable.button_primary);
        save.setTextColor(getResources().getColor(R.color.white));
        save.setAllCaps(false);

        Button delete = findViewById(R.id.deleteButton);
        delete.setVisibility(id > 0 ? View.VISIBLE : View.GONE);
        delete.setBackgroundResource(R.drawable.button_secondary);
        delete.setTextColor(getResources().getColor(R.color.green));
        delete.setAllCaps(false);

        save.setOnClickListener(view -> save());
        delete.setOnClickListener(view -> new AlertDialog.Builder(this)
                .setTitle("Delete ingredient?")
                .setMessage("This ingredient will be removed from the pantry.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteIngredient(id);
                    finish();
                })
                .show());
    }

    private void save() {
        String ingredientName = name.getText().toString().trim();
        String quantityText = quantity.getText().toString().trim();
        if (ingredientName.isEmpty()) {
            name.setError("Enter an ingredient name");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(quantityText);
        } catch (NumberFormatException exception) {
            quantity.setError("Enter a number");
            return;
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            quantity.setError("Quantity must be more than 0");
            return;
        }

        String date = expiry.getText().toString().trim();
        if (!date.isEmpty() && !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            expiry.setError("Use YYYY-MM-DD");
            return;
        }
        if (!date.isEmpty()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                format.setLenient(false);
                format.parse(date);
            } catch (Exception exception) {
                expiry.setError("Enter a valid date");
                return;
            }
        }

        db.saveIngredient(id, ingredientName, amount, units.getSelectedItem().toString(), date);
        finish();
    }
}
