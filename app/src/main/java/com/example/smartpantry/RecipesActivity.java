package com.example.smartpantry;
import android.content.*;import android.os.Bundle;import android.view.*;import android.widget.*;import java.util.*;
public class RecipesActivity extends BaseActivity {
 private DatabaseHelper db;
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_recipes);nav();db=new DatabaseHelper(this);}
 @Override protected void onResume(){super.onResume();showRecipes();}
 private void showRecipes(){LinearLayout list=findViewById(R.id.recipeList);list.removeAllViews();
  List<Ingredient> pantry=db.allIngredients();int count=0;
  for(Recipe recipe:db.allRecipes())if(Matching.canMake(recipe,pantry)){
   count++;View row=getLayoutInflater().inflate(R.layout.row_item,list,false);
   ((TextView)row.findViewById(R.id.rowTitle)).setText(recipe.name);
   ((TextView)row.findViewById(R.id.rowSubtitle)).setText(recipe.ingredients.size()+" ingredients • Ready to make");
   row.setOnClickListener(v->{Intent i=new Intent(this,RecipeDetailActivity.class);i.putExtra("id",recipe.id);startActivity(i);});list.addView(row);
  }
  ((TextView)findViewById(R.id.noRecipes)).setVisibility(count==0?View.VISIBLE:View.GONE);
 }
}
