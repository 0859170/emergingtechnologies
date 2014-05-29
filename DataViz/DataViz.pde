import controlP5.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import de.fhpotsdam.unfolding.providers.*;

UnfoldingMap map;

ControlP5 controlP5;
MultiList l;

int time = 0;

JSONObject json;
int indexnummer = 0;
DataProvider provider = new DataProvider();

Random rand = new Random();

int minJaar = 0;
int minMaand = 0;
int maxJaar = 0;
int maxMaand = 0;

int currentJaar = 0;
int currentMaand = 0;

int intervalInMilliseconds = 3000; // 3 seconden

boolean isPlaying = false;

int lastUpdate = millis();
int toggleDebounceLatest = millis();
int toggleDebounceMilliseconds = 200;

List<SubCategory> currentSet;

java.io.FilenameFilter jsonFilter = new java.io.FilenameFilter() {
    boolean accept(File dir, String name) {
      return name.toLowerCase().endsWith(".json");
    }
  };

public void setup() {
  size(1024, 500, P2D);
  noStroke();
  
  map = new UnfoldingMap(this, 0, 0, width, height - 25, new Microsoft.HybridProvider());
  map.setTweening(false);
  MapUtils.createDefaultEventDispatcher(this, map);
  
  // we'll have a look in the data folder
  java.io.File folder = new java.io.File(dataPath(""));
   
  // list the files in the data folder, passing the filter as parameter
  String[] filenames = folder.list(jsonFilter);
  println(filenames.length + " json files in specified directory");
   
  // display and parse the filenames
  for (int i = 0; i < filenames.length; i++) {
    println(filenames[i]);
    parseJSONFile(filenames[i]);
  }
  
  // De provider is nu gevuld, dus kunnen we deze gebruiken in de draw
  // We moeten nu de minimale en de maximale jaar/maand combinatie gaan bepalen
  minJaar = provider.getMinJaar();
  minMaand = provider.getMinMaand(minJaar);
  maxJaar = provider.getMaxJaar();
  maxMaand = provider.getMaxMaand(maxJaar);
  
  println("minJaar : " + minJaar);
  println("minMaand: " + minMaand);
  
  println("maxJaar : " + maxJaar);
  println("maxMaand: " + maxMaand);
  
  currentJaar = minJaar;
  currentMaand = minMaand;
  
  currentSet = provider.getDataForMoment(currentJaar, currentMaand);
  
  //GUI control
  controlP5 = new ControlP5(this);
  
  l = controlP5.addMultiList("dataList",40,40,200,24);
  
  // De multicontrol opbouwen op basis van de datasets/categorieen/subcategorieeen
  
  for (DataSet set: provider.datasets)
  {
    MultiListButton btn;
    btn = l.add(set.naam, set.id);
    for (Category cat: set.categories)
    {
      MultiListButton btncat;
      btncat = btn.add(cat.naam, cat.id);
      for (SubCategory scat: cat.subcategories)
      {
        MultiListButton btnscat;
        btnscat = btncat.add(scat.naam, scat.id);
      }
    }
  }
  
  // Calculeer het aantal maanden in het bereik
  // 2010 2
  //  2011 1
  // (maxJaar - minJaar * 12) - (minMaand - maxMaand)
  // minMaand - maxMaand 
  
  int months = 0;
  if (minJaar == maxJaar)
  {
    months = maxMaand - minMaand;
  }
  else
  {
    months = (maxJaar - minJaar * 12) - (minMaand - maxMaand);
  }
  
  //time slider
  controlP5.addSlider("time")
     .setPosition(100,height - 20)
     .setWidth(width - 200)
     .setRange(float(minJaar) + (float(minMaand) / 100), float(maxJaar) + (float(maxMaand) / 100)) // values can range from big to small as well
     .setValue(float(minJaar) + (float(minMaand) / 100))
     .setNumberOfTickMarks(1 + months)
     .setSliderMode(Slider.FLEXIBLE)
     .setLabel("Tijd")
     ;
     
  PImage[] imgs = {loadImage("button_a.png"),loadImage("button_b.png"),loadImage("button_c.png")};
  controlP5.addButton("play")
     .setValue(-1)
     .setPosition(20,height - 20)
     .setImages(imgs)
     .updateSize()
     ;
     
  PImage[] imgs2 = {loadImage("pauze_a.png"),loadImage("pauze_b.png"),loadImage("pauze_c.png")};
  controlP5.addButton("pauze")
     .setValue(-1)
     .setPosition(40,height - 20)
     .setImages(imgs2)
     .updateSize()
     ;
  
  // Wordt automatisch aangezet?   
  isPlaying = false;
}

public void controlEvent(ControlEvent theEvent) {
  if (theEvent.getController().getName() == "time")
  {
    println(theEvent.value());  
    
    // Als iemand een andere maand selecteert in de slider dan moeten we terugrekenen naar jaar / maand
    // het integer component is het jaar
    int currentJaar = int(theEvent.value());
    int currentMaand = int((theEvent.value() - currentJaar) * 100);
    println(currentJaar);
    println(currentMaand);
    
    clearCurrentSet();
    currentSet = provider.getDataForMoment(currentJaar, currentMaand);
  }
  else
  {
    println("NON TIME VAL: " + theEvent.value());
    if (theEvent.value() > -1)
    {
      // We gaan uit van een togglebutton
      if ((millis() - toggleDebounceLatest) > toggleDebounceMilliseconds)
      {
        provider.toggleDataSetSelected(int(theEvent.value()));
        
        // verbreek de koppeling
        
        clearCurrentSet();
        currentSet = provider.getDataForMoment(currentJaar, currentMaand);
        toggleDebounceLatest = millis();
      }
    }
  }
}

public void clearCurrentSet()
{
  for (SubCategory cat: currentSet)
  {
    cat.periods = null;
  }
  
  currentSet = null;
}

public void play(int theValue) {
  println("playbutton aangeroepen: "+theValue);
  isPlaying = true;
}

public void pauze(int theValue) {
  println("pauzebutton aangeroepen: "+theValue);
  isPlaying = false;
}

public void drawLegend()
{
  // Teken in de rechterbovenhoek de legenda
  noStroke();
  fill(0, 10, 50, 220);
  rect(width - 150, 10, 140, height - 100);
    
  PFont myFont = new BitFont(CP.decodeBase64(BitFont.standard56base64));
  textFont(myFont);
  
  fill(255, 255, 255, 255);
  int yOffset = 20;
  for (SubCategory scat: currentSet)
  {
    text(scat.naam.toUpperCase(), width - 145, yOffset);
    
    // Kleurvoorbeeld
    fill(scat.red, scat.green, scat.blue, 200);
    rect(width - 30, yOffset - 5, 10, 10);
    
    yOffset+=10;
  }
}

public void draw() {
  background(time);
  map.draw();
  
  int elapsed = millis() - lastUpdate;
  
  if ((elapsed > intervalInMilliseconds) && isPlaying)
  {
    println("update want: " + elapsed);
    
    println(currentJaar);
    println(currentMaand);
    lastUpdate = millis();
    if (currentMaand == 12)
    {
      currentMaand = 1;
      currentJaar++;
    }
    else
    {
      currentMaand++;
    }
    
    if (currentJaar > maxJaar)
    {
      currentJaar = minJaar;
      currentMaand = minMaand;
    }
    else
    {
      if (currentJaar == maxJaar && currentMaand > maxMaand)
      {
        currentJaar = minJaar;
        currentMaand = minMaand;
      }
    }
    
    println("na update");
    println(currentJaar);
    println(currentMaand);
    
    float newval = float(currentJaar) + (float(currentMaand) / 100);
    
    println("calced " + newval);
    // We zetten de slider op de huidige jaar/maand combo
    controlP5.getController("time").setValue(newval);
  }
  
  drawLegend();
  
  // Elke drawcycle halen we de tekenen locaties op
  for (SubCategory scat: currentSet)
  {
    // Zet de fill color
    fill(scat.red, scat.green, scat.blue, 255);
      
    for (Period per: scat.periods)
    {
      for (DataPoint loc: per.locations)
      {
        ScreenPosition position = map.getScreenPosition(new Location(loc.lat, loc.lng));
        
        // Afhankelijk van het datatype tekenen we een vaste grote of een calculatie
        if (scat.datatype == 0)
        {
          ellipse(position.x, position.y, 10, 10);
        }
        
        if (scat.datatype == 1)
        {
          float s = map.getZoom() / 100; // TODO WEIGTH * AMOUNT
          ellipse(position.x, position.y, s, s);
        }
      }
    }
  }
}

void parseJSONFile(String file)
{
  json = loadJSONObject(file);

  String hoofdnaam = json.getString("naam");
  JSONArray categories = json.getJSONArray("categories");
  println(hoofdnaam);
  
  DataSet dataset = new DataSet(indexnummer, hoofdnaam);
  indexnummer++;
  
  for (int hcat=0; hcat<categories.size(); hcat++)
  {
    JSONObject hcategorie = categories.getJSONObject(hcat);
    String naam = hcategorie.getString("naam");
    JSONArray subcategories = hcategorie.getJSONArray("subcategories");
    println(" +   " + naam);
    
    Category category = new Category(indexnummer, naam);
    indexnummer++;
    
    for (int cat=0; cat<subcategories.size(); cat++)
    {
      JSONObject categorie = subcategories.getJSONObject(cat);
      String catnaam = categorie.getString("naam");
      int datatype = categorie.getInt("datatype");
      float weight = categorie.getFloat("weight");
      int r = 100 + rand.nextInt(155);
      int g = 100 + rand.nextInt(155); 
      int b = 100 + rand.nextInt(155);
      println("     +   " + catnaam);
      
      SubCategory subcategory = new SubCategory(indexnummer, catnaam, datatype, weight, r, g ,b);
      indexnummer++;
      
      JSONArray periods = categorie.getJSONArray("periods");
      for (int per=0; per<periods.size(); per++)
      {
        JSONObject jperiod = periods.getJSONObject(per);
        int year = jperiod.getInt("jaar");
        int month = jperiod.getInt("maand");
        println("         +   " + year + ", " + month);
        
        Period period = new Period(year, month);
        
        JSONArray locations = jperiod.getJSONArray("locations");
        for (int loc=0; loc<locations.size(); loc++)
        {
          JSONObject jlocation = locations.getJSONObject(loc);
          float lat = jlocation.getFloat("lat");
          float lng = jlocation.getFloat("lng");
          float amount = jlocation.getFloat("amount");
          println("             +   " + lat + ", " + lng + ", " + amount);
          
          DataPoint location = new DataPoint(lat, lng, amount);
          period.addLocation(location);
        }
        
        // In de lijst
        subcategory.addPeriod(period);
      }
      
      // Toevoegen aan de lijst
      category.addSubCategory(subcategory);
    }
    
    // Add de category
    dataset.addCategory(category);
  }
  
  // Add de dataset
  provider.addDataSet(dataset);
}

