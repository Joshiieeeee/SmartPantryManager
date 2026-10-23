package com.example.smartpantry;
import android.content.*;import android.view.*;import android.widget.*;import java.util.*;
public class PantryAdapter extends BaseAdapter {
 public interface Click{void selected(Ingredient ingredient);}
 private Context context;private List<Ingredient> items;private Click click;
 public PantryAdapter(Context context,List<Ingredient> items,Click click){this.context=context;this.items=items;this.click=click;}
 public int getCount(){return items.size();}public Object getItem(int i){return items.get(i);}public long getItemId(int i){return items.get(i).id;}
 public View getView(int index,View old,android.view.ViewGroup parent){
  View v=old==null?LayoutInflater.from(context).inflate(R.layout.row_item,parent,false):old;
  Ingredient item=items.get(index);((TextView)v.findViewById(R.id.rowTitle)).setText(item.name);
  String desc=String.format(Locale.getDefault(),"%s %s",item.quantity,item.unit);
  if(item.expiry!=null&&!item.expiry.trim().isEmpty())desc+=" • expires "+item.expiry;
  ((TextView)v.findViewById(R.id.rowSubtitle)).setText(desc);v.setOnClickListener(w->click.selected(item));return v;
 }
}
