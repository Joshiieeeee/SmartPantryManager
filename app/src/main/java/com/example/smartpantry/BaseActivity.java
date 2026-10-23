package com.example.smartpantry;
import android.content.*;import android.widget.*;import androidx.appcompat.app.AppCompatActivity;
public class BaseActivity extends AppCompatActivity {
 protected void nav(){
  Button pantry=findViewById(R.id.navPantry),recipes=findViewById(R.id.navRecipes),settings=findViewById(R.id.navSettings);
  styleNav(pantry, this instanceof MainActivity);
  styleNav(recipes, this instanceof RecipesActivity || this instanceof RecipeDetailActivity);
  styleNav(settings, this instanceof SettingsActivity);
  if(pantry!=null)pantry.setOnClickListener(v->go(MainActivity.class));
  if(recipes!=null)recipes.setOnClickListener(v->go(RecipesActivity.class));
  if(settings!=null)settings.setOnClickListener(v->go(SettingsActivity.class));
 }
 private void styleNav(Button button, boolean current){
  if(button==null)return;
  button.setBackgroundResource(R.drawable.nav_background);
  button.setSelected(current);
  button.setTextColor(getResources().getColor(current?R.color.green:R.color.white));
  button.setAllCaps(false);
  button.setTextSize(14);
  button.setElevation(0);
 }
 protected void go(Class<?> cls){Intent i=new Intent(this,cls);i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);startActivity(i);}
}
