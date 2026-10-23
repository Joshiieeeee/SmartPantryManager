package com.example.smartpantry;
import android.os.Bundle;import android.widget.*;import java.util.*;
public class RecipeDetailActivity extends BaseActivity {
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_detail);nav();
  Recipe r=new DatabaseHelper(this).getRecipe(getIntent().getLongExtra("id",-1));if(r==null){finish();return;}
  ((TextView)findViewById(R.id.detailTitle)).setText(r.name);StringBuilder ingredients=new StringBuilder();
  for(Recipe.Requirement ing:r.ingredients)ingredients.append("• ").append(ing.name).append(": ").append(String.format(Locale.getDefault(),"%s",ing.quantity)).append(" ").append(ing.unit).append("\n");
  ((TextView)findViewById(R.id.detailIngredients)).setText(ingredients.toString());((TextView)findViewById(R.id.detailSteps)).setText(r.method);
 }
}
