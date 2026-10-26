package com.example.smartpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public class PantryAdapter extends BaseAdapter {
    public interface Click {
        void selected(Ingredient ingredient);
    }

    private Context context;
    private List<Ingredient> items;
    private Click click;
    private boolean showExpiryStatus;

    public PantryAdapter(Context context, List<Ingredient> items, Click click) {
        this.context = context;
        this.items = items;
        this.click = click;
        this.showExpiryStatus = context.getSharedPreferences("options", Context.MODE_PRIVATE)
                .getBoolean("expiry_alert", false);
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
        String spokenDescription = description;
        if (item.expiry != null && !item.expiry.trim().isEmpty()) {
            String status = showExpiryStatus
                    ? ExpiryStatus.label(item.expiry, Calendar.getInstance()) : "";
            description += " · " + (status.isEmpty() ? item.expiry : status);
            spokenDescription += ", expiry " + item.expiry;
            if (!status.isEmpty()) spokenDescription += ", " + status;
        }
        ((TextView) view.findViewById(R.id.rowSubtitle)).setText(description);
        view.setContentDescription(item.name + ", " + spokenDescription);
        view.setOnClickListener(clicked -> click.selected(item));
        return view;
    }
}
