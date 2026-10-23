package com.example.smartpantry;
import android.content.*;import android.os.Bundle;import android.view.*;import android.widget.*;import java.util.*;
public class MainActivity extends BaseActivity {
 private DatabaseHelper db;private ListView list;
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main_polished);db=new DatabaseHelper(this);list=findViewById(R.id.itemList);nav();
  findViewById(R.id.addButton).setOnClickListener(v->startActivity(new Intent(this,EditIngredientActivity.class)));
 }
 @Override protected void onResume(){super.onResume();if(list!=null)refresh();}
 private void refresh(){
  ArrayList<Ingredient> items=db.allIngredients();
  ((TextView)findViewById(R.id.emptyText)).setVisibility(items.isEmpty()?View.VISIBLE:View.GONE);
  PantryAdapter adapter=new PantryAdapter(this,items,ing->{Intent i=new Intent(this,EditIngredientActivity.class);i.putExtra("id",ing.id);startActivity(i);});
  list.setAdapter(adapter);
 }
}
