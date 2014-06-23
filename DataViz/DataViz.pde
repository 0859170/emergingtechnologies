/***************************************************************************************************************************

  ______                          _               _______        _                 _                   
 |  ____|                        (_)             |__   __|      | |               | |                  
 | |__   _ __ ___   ___ _ __ __ _ _ _ __   __ _     | | ___  ___| |__  _ __   ___ | | ___   __ _ _   _ 
 |  __| | '_ ` _ \ / _ \ '__/ _` | | '_ \ / _` |    | |/ _ \/ __| '_ \| '_ \ / _ \| |/ _ \ / _` | | | |
 | |____| | | | | |  __/ | | (_| | | | | | (_| |    | |  __/ (__| | | | | | | (_) | | (_) | (_| | |_| |
 |______|_| |_| |_|\___|_|  \__, |_|_| |_|\__, |    |_|\___|\___|_| |_|_| |_|\___/|_|\___/ \__, |\__, |
                             __/ |         __/ |                                            __/ | __/ |
                            |___/         |___/                                            |___/ |___/  

 ****************************************************************************************************************************
 * Vak:      EMERGING TECHNOLOGY
 * Vakcode:  INFPR201A2
 * Docent:   W.N.F. Blijlevens
 ****************************************************************************************************************************
 * Auteurs:  W. Deur
 *           S. Mayer
 *           M. vd Werf
 *           J. Tigchelaar
 ****************************************************************************************************************************
 Deze applicatie is ontwikkeld voor het vak INFPR201A2, Emerging Technology. Doel van deze applicatie is het 'over tijd' vis-
 ualiseren van diverse geografische datasets. De belangrijkste dataset is de zogeheten LISA dataset waarin de creatieve
 industrie in de omgeving van Rotterdam is vastgelegd. Als voorbeeld databronnen zijn daar onder andere fietsendiefstallen
 aan toegevoegd.
 
 Plaats de geconverteerde JSON data in de data submap om deze terug te zien in de applicatie.
 ****************************************************************************************************************************/

/* Imports van libraries */
import controlP5.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import de.fhpotsdam.unfolding.providers.*;

/* Het map component */
UnfoldingMap map;

/* De UI control library componenten */
ControlP5 controlP5;
MultiList l;

/* De huidige tijd */
int time = 0;

/* JSON object om data in te lezen */
JSONObject json;

/* Huidige dataprovider */
int indexnummer = 0;

/* Onze dataprovider */
DataProvider provider = new DataProvider();

/* Random generator */
Random rand = new Random();

/* Weergave variabelen */
int minJaar = 0;        // kleinste jaar in de dataset
int minMaand = 0;       // kleinste maand in de dataset 
int maxJaar = 0;        // grootste jaar in de dataset
int maxMaand = 0;       // grootste maand in de dataset

int currentJaar = 0;    // huidige jaar
int currentMaand = 0;   // huidige maand

/* Interval tussen stappen */
int intervalInMilliseconds = 1000;   // 1 seconde

/* Afspeel variabelen */
boolean isPlaying = false;
int lastUpdate = millis();
int toggleDebounceLatest = millis();
int toggleDebounceMilliseconds = 200;

/* Subcategorieeen van de data */
List<SubCategory> currentSet;

/* Heatmap variabelen */
PImage overlay;
PImage originalHeatmapBrush;
PImage heatmapBrush;   // radiale gradient als brush. Alleen blauwe kanaal gebruikt
PImage heatmapColors;  // Enkele pixel bitmap met kleurcode van koud naar heet

PImage gradientMap;    // canvas voor de gradient map
PImage heatmap;        // canvas voor de heatmap

float maxValue = 0;    // maximale waarde in de gradientMap

boolean needRecalc = true;

float previousMapZoom = 0;
float previousLat = 0;
float previousLon = 0;

/* JSON bestanden filter */
java.io.FilenameFilter jsonFilter = new java.io.FilenameFilter() {
  boolean accept(File dir, String name) {
    return name.toLowerCase().endsWith(".json");
  }
};

/***************************************************************************************************************************
 *                                                     De setup methode                                                     *
 ****************************************************************************************************************************
 Deze methode initialiseert de data en de interface
 ****************************************************************************************************************************/
public void setup() {
  size(1024, 768, P2D);
  noStroke();

  /* Heatmap code */
  heatmapColors = loadImage("images/heatmapColors.png");
  originalHeatmapBrush = loadImage("images/heatmapBrush.png");

  overlay = createImage(width, height, ARGB);
  image(overlay, 0, 0);

  gradientMap = createImage(width, height, ARGB);
  heatmap = createImage(width, height, ARGB);
  heatmapBrush = createImage(width, height, ARGB);

  heatmapBrush.copy(originalHeatmapBrush, 0, 0, width, height, 0, 0, width, height);

  gradientMap.loadPixels();
  heatmap.loadPixels();
  heatmapBrush.loadPixels();
  heatmapColors.loadPixels();  

  map = new UnfoldingMap(this, 0, 0, width, height - 25, new Microsoft.HybridProvider());
  map.setTweening(false);
  MapUtils.createDefaultEventDispatcher(this, map);

  java.io.File folder = new java.io.File(dataPath(""));

  /* Alleen JSON files uit de data folder inlezen */
  String[] filenames = folder.list(jsonFilter);

  /* Parsen van de files */
  for (int i = 0; i < filenames.length; i++) {
    parseJSONFile(filenames[i]);
  }

  /* De provider is nu gevuld, dus kunnen we deze gebruiken in de draw
   We moeten nu de minimale en de maximale jaar/maand combinatie gaan bepalen */
  minJaar = provider.getMinJaar();
  minMaand = provider.getMinMaand(minJaar);
  maxJaar = provider.getMaxJaar();
  maxMaand = provider.getMaxMaand(maxJaar);

  currentJaar = minJaar;
  currentMaand = minMaand;

  currentSet = provider.getDataForMoment(currentJaar, currentMaand);

  /* GUI control */
  controlP5 = new ControlP5(this);
  l = controlP5.addMultiList("dataList", 40, 40, 200, 24);

  /* De multicontrol opbouwen op basis van de datasets/categorieen/subcategorieeen */
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

  /* Calculeer het aantal maanden in het bereik */
  int months = 0;
  if (minJaar == maxJaar)
  {
    months = maxMaand - minMaand;
  }
  else
  {
    months = (maxJaar - minJaar * 12) - (minMaand - maxMaand);
  }

  /* time slider */
  controlP5.addSlider("time")
    .setPosition(100, height - 20)
      .setWidth(width - 200)
        .setRange(float(minJaar) + (float(minMaand) / 100), float(maxJaar) + (float(maxMaand) / 100)) // values can range from big to small as well
          .setValue(float(minJaar) + (float(minMaand) / 100))
            .setNumberOfTickMarks(1 + months)
              .setSliderMode(Slider.FLEXIBLE)
                .setLabel("Tijd")
                  ;

  PImage[] imgs = {
    loadImage("button_a.png"), loadImage("button_b.png"), loadImage("button_c.png")
  };
  
  controlP5.addButton("play")
    .setValue(-1)
      .setPosition(20, height - 20)
        .setImages(imgs)
          .updateSize()
            ;

  PImage[] imgs2 = {
    loadImage("pauze_a.png"), loadImage("pauze_b.png"), loadImage("pauze_c.png")
  };
  
  controlP5.addButton("pauze")
    .setValue(-1)
      .setPosition(40, height - 20)
        .setImages(imgs2)
          .updateSize()
            ;

  isPlaying = false;

  /* Zoom in op Rotterdam -  Rijnmonnd */
  map.zoomAndPanTo(new Location(51.9f, 4.4f), 11); 
}

/***************************************************************************************************************************
 *                                        Event voor P5 controls                                                            *
 ****************************************************************************************************************************
 Event wat afgaat voor de P5 controls
 ****************************************************************************************************************************/
public void controlEvent(ControlEvent theEvent) {
  if (theEvent.getController().getName() == "time")
  {
    // Als iemand een andere maand selecteert in de slider dan moeten we terugrekenen naar jaar / maand
    // het integer component is het jaar
    int currentJaar = int(theEvent.value());
    int currentMaand = int((theEvent.value() - currentJaar) * 100);

    clearCurrentSet();
    currentSet = provider.getDataForMoment(currentJaar, currentMaand);
  }
  else
  {
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

/***************************************************************************************************************************
 *                                                clearCurrentSet                                                           *
 ****************************************************************************************************************************
 Maakt de dataset leeg
 ****************************************************************************************************************************/
public void clearCurrentSet()
{
  for (SubCategory cat: currentSet)
  {
    cat.periods = null;
  }

  currentSet = null;
}

/***************************************************************************************************************************
 *                                                    play                                                                  *
 ****************************************************************************************************************************
 Begin met afspelen
 ****************************************************************************************************************************/
public void play(int theValue) {
  isPlaying = true;
}

/***************************************************************************************************************************
 *                                                    pauze                                                                 *
 ****************************************************************************************************************************
 Pauzeer afspelen
 ****************************************************************************************************************************/
public void pauze(int theValue) {
  isPlaying = false;
}

/***************************************************************************************************************************
 *                                                    drawLegend                                                            *
 ****************************************************************************************************************************
 Teken de legenda
 ****************************************************************************************************************************/
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

/***************************************************************************************************************************
 *                                                        draw                                                              *
 ****************************************************************************************************************************
 Teken alles
 ****************************************************************************************************************************/
public void draw() {
  background(0, 10, 50);
  map.draw();

  /* Bepaal of de heatmap opnieuw gecalculeerd moet worden */
  if (map.getZoom() != previousMapZoom) {
    previousMapZoom = map.getZoom();
    needRecalc = true;
  }
  if (map.getLocation(0, 0).getLat() != previousLat) {
    previousLat = map.getLocation(0, 0).getLat();
    needRecalc = true;
  }
  if (map.getLocation(0, 0).getLon() != previousLon) {
    previousLon = map.getLocation(0, 0).getLon();
    needRecalc = true;
  }

  /* Verzet de verlopen tijd */
  int elapsed = millis() - lastUpdate;

  /* Zijn we aan het afspelen en is het tijd voor de volgende stap? */
  if ((elapsed > intervalInMilliseconds) && isPlaying)
  {
    needRecalc = true;

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

    float newval = float(currentJaar) + (float(currentMaand) / 100);

    // We zetten de slider op de huidige jaar/maand combo
    controlP5.getController("time").setValue(newval);
  }

  /* Is hercalculatie van de heatmap nodig? */
  if (needRecalc) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int loc = x + y*width;
        heatmap.pixels[loc] = 0;
        gradientMap.pixels[loc] = 0;
      }
    }
    heatmap.updatePixels();
    gradientMap.updatePixels();
    maxValue = 0;
  }

  /* Elke drawcycle halen we de tekenen locaties op */
  for (SubCategory scat: currentSet)
  {
    /* Zet de fill color */
    fill(scat.red, scat.green, scat.blue, 255);

    for (Period per: scat.periods)
    {
      for (DataPoint loc: per.locations)
      {
        ScreenPosition position = map.getScreenPosition(new Location(loc.lat, loc.lng));

        /* Afhankelijk van het datatype tekenen we een vaste grote of een calculatie */
        if (scat.datatype == 0)
        {
          // ellipse(position.x, position.y, 10, 10); 
          if (needRecalc) {
            createHeatmapPoint(position.x, position.y);
          }
        }

        if (scat.datatype == 1)
        {
          /* TODO: WEIGHT * amount factor calculatie */
          float s = (map.getZoom() / 100) * (scat.weight * loc.amount); 
          ellipse(position.x, position.y, s, s);
        }
      }
    }
  }

  /* Als we de heatmap hercalculeerd hebben dan willen we hier eenmalig de heatmap bijwerken */
  /* met de nieuwe gradient data */
  if (needRecalc)
  {
    updateHeatmap();
  }

  /* Klaar met hercalculeren */
  needRecalc = false;

  /* Teken de heatmap over de kaart heen */
  tint(255, 126);
  image(heatmap, 0, 0);
  noTint();

  /* Teken de legenda */
  drawLegend();
}

/***************************************************************************************************************************
 *                                                        parseJSONFile                                                     *
 ****************************************************************************************************************************
 Verwerk de inhoud van een JSON bestand
 ****************************************************************************************************************************/
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

      SubCategory subcategory = new SubCategory(indexnummer, catnaam, datatype, weight, r, g, b);
      indexnummer++;

      JSONArray periods = categorie.getJSONArray("periods");
      for (int per=0; per<periods.size(); per++)
      {
        JSONObject jperiod = periods.getJSONObject(per);
        int year = jperiod.getInt("jaar");
        int month = jperiod.getInt("maand");

        Period period = new Period(year, month);

        JSONArray locations = jperiod.getJSONArray("locations");
        for (int loc=0; loc<locations.size(); loc++)
        {
          JSONObject jlocation = locations.getJSONObject(loc);
          float lat = jlocation.getFloat("lat");
          float lng = jlocation.getFloat("lng");
          float amount = jlocation.getFloat("amount");

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

/***************************************************************************************************************************
 *                                                        createHeatmapPoint                                                *
 ****************************************************************************************************************************
 Maak een extra heatmap punt aan, de meer punten op een bepaalde plek, de 'heter' het punt
 ****************************************************************************************************************************/
void createHeatmapPoint(float x, float y)
{
  // render the heatmapBrush into the gradientMap:
  drawToGradient((int)x, (int)y);
}

/***************************************************************************************************************************
 *                                                        drawToGradient                                                    *
 ****************************************************************************************************************************
 Render code die de heatmapBrush (stencil) blit op de gradientmap, gecentreerd rond het gekozen punt, maakt gebruik van 
 additieve blending
 ****************************************************************************************************************************/
void drawToGradient(int x, int y)
{
  /* Zorg dat de heatmap meeschaalt met het zoomnivo van de map */
  heatmapBrush = loadImage("images/heatmapBrush.png");
  heatmapBrush.resize(0, 1 + (int)(map.getZoom() / 75)); // 100

  // find the top left corner coordinates on the target image
  int startX = x-heatmapBrush.width/2;
  int startY = y-heatmapBrush.height/2;

  for (int py = 0; py < heatmapBrush.height; py++)
  {
    for (int px = 0; px < heatmapBrush.width; px++) 
    {
      // voor elkey pixel in de heatmapBrush:
      // vind het corresponderende coordinaat in de gradient map:
      int hmX = startX+px;
      int hmY = startY+py;
      /*
        Out of bounds?
       */
      if (hmX < 0 || hmY < 0 || hmX >= gradientMap.width || hmY >= gradientMap.height)
      {
        continue;
      }

      // kleur
      int col = heatmapBrush.pixels[py*heatmapBrush.width+px]; 
      col = col & 0xff; 

      // pixels in gradient map
      int gmIndex = hmY*gradientMap.width+hmX;

      if (gradientMap.pixels[gmIndex] < 0xffffff-col) 
      {
        gradientMap.pixels[gmIndex] += col; 
        if (gradientMap.pixels[gmIndex] > maxValue) 
        {
          // update maxValue zodat de scaling goed blijft
          maxValue = gradientMap.pixels[gmIndex];
        }
      }
    }
  }
  gradientMap.updatePixels();
}


/***************************************************************************************************************************
 *                                                        updateHeatmap                                                    *
 ****************************************************************************************************************************
 Update de heatmap vanuit de gradientmap
 ****************************************************************************************************************************/
void updateHeatmap()
{
  // voor alle pixels in de gradient:
  for (int i=0; i<gradientMap.pixels.length; i++)
  {
    // haal pixel waarde
    float gmValue = gradientMap.pixels[i];

    // map naar heatMapColors
    int colIndex = (int) ((gmValue/maxValue)*(heatmapColors.pixels.length-1));
    int col = heatmapColors.pixels[colIndex];

    // update de heatmap 
    heatmap.pixels[i] = col;
  }

  // laad de pixel data in de heatmap.
  heatmap.updatePixels();
}

