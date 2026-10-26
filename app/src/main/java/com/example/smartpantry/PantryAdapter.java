package com.example.smartpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

public class PantryAdapter extends BaseAdapter {
    public interface Click {
        void selected(Ingredient ingredient);
    }

    private Context context;
    private List<Ingredient> items;
    private Click click;

    public PantryAdapter(Context context, List<Ingredient> items, Click click) {
        this.context = context;
        this.items = items;
        this.click = click;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int index) {
        return items.get(index);
    }

    @Override
    public long getItemId(int index) {
        return items.get(index).id;
    }

    @Override
    public View getView(int index, View oldView, ViewGroup parent) {
        View view = oldView == null
                ? LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
                : oldView;
        Ingredient item = items.get(index);
        ((TextView) view.findViewById(R.id.rowTitle)).setText(item.name);

        String description = BigDecimal.valueOf(item.quantity).stripTrailingZeros().toPlainString()
                + " " + item.unit;
        if (item.expiry != null && !item.expiry.trim().isEmpty()) {
            description += " · " + item.expiry;
        }
        ((TextView) view.findViewById(R.id.rowSubtitle)).setText(description);
        view.setContentDescription(item.name + ", " + description);
        view.setOnClickListener(clicked -> click.selected(item));
        return view;
    }
}
