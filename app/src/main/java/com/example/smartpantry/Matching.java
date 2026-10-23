package com.example.smartpantry;
import java.util.*;
public class Matching {
 private static String normal(String name) {
  String s=name.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+"," ");
  if(s.equals("tomatoes"))return "tomato";
  if(s.equals("potatoes"))return "potato";
  if(s.endsWith("ies") && s.length()>4)return s.substring(0,s.length()-3)+"y";
  if(s.endsWith("s") && !s.endsWith("ss") && s.length()>3)return s.substring(0,s.length()-1);
  return s;
 }
 private static double base(double qty,String unit){
  String u=unit.toLowerCase(Locale.ROOT).trim();
  if(u.equals("kg"))return qty*1000;
  if(u.equals("l"))return qty*1000;
  if(u.equals("tbsp"))return qty*15;
  if(u.equals("tsp"))return qty*5;
  return qty;
 }
 private static String category(String unit) {
  String u=unit.toLowerCase(Locale.ROOT).trim();
  if(u.equals("g")||u.equals("kg"))return "mass";
  if(u.equals("ml")||u.equals("l")||u.equals("tbsp")||u.equals("tsp"))return "volume";
  if(u.equals("piece")||u.equals("pieces"))return "pieces";
  return u;
 }
 public static boolean canMake(Recipe recipe,List<Ingredient> pantry){
  for(Recipe.Requirement needed:recipe.ingredients){
   double available=0;
   for(Ingredient item:pantry){
    if(normal(item.name).equals(normal(needed.name)) && category(item.unit).equals(category(needed.unit)))
     available+=base(item.quantity,item.unit);
   }
   if(available+0.000001<base(needed.quantity,needed.unit))return false;
  }
  return true;
 }
}
