package com.example.smartpantry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class MatchingTest {
    @Test
    public void requiresEveryIngredient() {
        Recipe recipe = new Recipe(1, "Egg Toast", "");
        recipe.ingredients.add(new Recipe.Requirement("bread", 2, "pieces"));
        recipe.ingredients.add(new Recipe.Requirement("egg", 2, "pieces"));

        assertFalse(Matching.canMake(recipe, Collections.singletonList(
                new Ingredient(1, "bread", 2, "pieces", ""))));
        assertTrue(Matching.canMake(recipe, Arrays.asList(
                new Ingredient(1, "bread", 2, "pieces", ""),
                new Ingredient(2, "eggs", 2, "pieces", ""))));
    }

    @Test
    public void convertsCompatibleUnits() {
        Recipe recipe = new Recipe(1, "Pasta", "");
        recipe.ingredients.add(new Recipe.Requirement("pasta", 1000, "g"));

        assertTrue(Matching.canMake(recipe, Collections.singletonList(
                new Ingredient(1, "pasta", 1, "kg", ""))));
        assertFalse(Matching.canMake(recipe, Collections.singletonList(
                new Ingredient(1, "pasta", 1000, "ml", ""))));
    }

    @Test
    public void sumsRepeatedRequirements() {
        Recipe recipe = new Recipe(1, "Egg dish", "");
        recipe.ingredients.add(new Recipe.Requirement("egg", 1, "pieces"));
        recipe.ingredients.add(new Recipe.Requirement("eggs", 2, "pieces"));

        assertFalse(Matching.canMake(recipe, Collections.singletonList(
                new Ingredient(1, "egg", 2, "pieces", ""))));
        assertTrue(Matching.canMake(recipe, Collections.singletonList(
                new Ingredient(1, "egg", 3, "pieces", ""))));
    }
}
