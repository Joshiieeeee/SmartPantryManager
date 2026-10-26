package com.example.smartpantry;

import java.util.List;
import java.util.Locale;

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

    public static boolean canMake(Recipe recipe, List<Ingredient> pantry) {
        for (Recipe.Requirement needed : recipe.ingredients) {
            double available = 0;
            for (Ingredient item : pantry) {
                if (normal(item.name).equals(normal(needed.name))
                        && category(item.unit).equals(category(needed.unit))) {
                    available += base(item.quantity, item.unit);
                }
            }
            if (available + 0.000001 < base(needed.quantity, needed.unit)) return false;
        }
        return true;
    }
}
