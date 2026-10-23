package com.example.smartpantry;
import android.app.*;import android.os.Bundle;import android.widget.*;import java.util.*;
public class EditIngredientActivity extends BaseActivity {
 private EditText name,quantity,expiry;private Spinner units;private long id;private DatabaseHelper db;
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_edit);nav();db=new DatabaseHelper(this);
  name=findViewById(R.id.inputName);quantity=findViewById(R.id.inputQuantity);expiry=findViewById(R.id.inputExpiry);units=findViewById(R.id.inputUnit);
  String[] opts={"pieces","g","kg","ml","l","tbsp","tsp"};units.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,opts));
  id=getIntent().getLongExtra("id",-1);if(id>0){Ingredient ing=db.getIngredient(id);if(ing!=null){name.setText(ing.name);quantity.setText(String.valueOf(ing.quantity));expiry.setText(ing.expiry);for(int i=0;i<opts.length;i++)if(opts[i].equals(ing.unit))units.setSelection(i);}}
  Button save=findViewById(R.id.saveButton);
  save.setBackgroundResource(R.drawable.button_primary);save.setTextColor(getResources().getColor(R.color.white));save.setAllCaps(false);
  Button delete=findViewById(R.id.deleteButton);delete.setVisibility(id>0?android.view.View.VISIBLE:android.view.View.GONE);
  delete.setBackgroundResource(R.drawable.button_secondary);delete.setTextColor(getResources().getColor(R.color.green));delete.setAllCaps(false);
  save.setOnClickListener(v->save());
  delete.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Delete ingredient?").setMessage("This ingredient will be removed from the pantry.").setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,x)->{db.deleteIngredient(id);finish();}).show());
 }
 private void save(){String n=name.getText().toString().trim(),q=quantity.getText().toString().trim();
  if(n.isEmpty()){name.setError("Enter an ingredient name");return;}
  double amount;try{amount=Double.parseDouble(q);}catch(NumberFormatException e){quantity.setError("Enter a number");return;}
  if(!Double.isFinite(amount)||amount<=0){quantity.setError("Quantity must be more than 0");return;}
  String date=expiry.getText().toString().trim();
  if(!date.isEmpty()&&!date.matches("\\d{4}-\\d{2}-\\d{2}")){expiry.setError("Use YYYY-MM-DD");return;}
  if(!date.isEmpty()){try{java.text.SimpleDateFormat f=new java.text.SimpleDateFormat("yyyy-MM-dd",Locale.US);f.setLenient(false);f.parse(date);}catch(Exception e){expiry.setError("Enter a valid date");return;}}
  db.saveIngredient(id,n,amount,units.getSelectedItem().toString(),date);finish();
 }
}
