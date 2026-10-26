package com.example.smartpantry;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Matching {
    private static String normal(String name) {
        String result = name.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
        if (result.equals("tomatoes")) return "tomato";
        if (result.equals("potatoes")) return "potato";
        if (result.endsWith("ies") && result.length() > 4) {
            return result.substring(0, result.length() - 3) + "y";
        }
        if (result.endsWith("s") && !result.endsWith("ss") && result.length() > 3) {
            return result.substring(0, result.length() - 1);
        }
        return result;
    }

    private static double base(double quantity, String unit) {
        String value = unit.toLowerCase(Locale.ROOT).trim();
        if (value.equals("kg")) return quantity * 1000;
        if (value.equals("l")) return quantity * 1000;
        if (value.equals("tbsp")) return quantity * 15;
        if (value.equals("tsp")) return quantity * 5;
        return quantity;
    }

    private static String category(String unit) {
        String value = unit.toLowerCase(Locale.ROOT).trim();
        if (value.equals("g") || value.equals("kg")) return "mass";
        if (value.equals("ml") || value.equals("l") || value.equals("tbsp") || value.equals("tsp")) {
            return "volume";
        }
        if (value.equals("piece") || value.equals("pieces")) return "pieces";
        return value;
    }

    private static void addQuantity(Map<String, Double> quantities, String key, double amount) {
        Double current = quantities.get(key);
        quantities.put(key, (current == null ? 0 : current) + amount);
    }

    public static boolean canMake(Recipe recipe, List<Ingredient> pantry) {
        Map<String, Double> available = new HashMap<>();
        for (Ingredient item : pantry) {
            String key = normal(item.name) + "|" + category(item.unit);
            addQuantity(available, key, base(item.quantity, item.unit));
        }

        Map<String, Double> required = new HashMap<>();
        for (Recipe.Requirement needed : recipe.ingredients) {
            String key = normal(needed.name) + "|" + category(needed.unit);
            addQuantity(required, key, base(needed.quantity, needed.unit));
        }

        for (Map.Entry<String, Double> entry : required.entrySet()) {
            Double amount = available.get(entry.getKey());
            if ((amount == null ? 0 : amount) + 0.000001 < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
}
