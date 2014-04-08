import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import java.util.List;
import java.util.Random;
import de.fhpotsdam.unfolding.providers.*;

UnfoldingMap map;

List<Location> locations1;
List<Location> locations2;

List<ClusteredMarker> clustered1;
List<ClusteredMarker> clustered2;

// Zet deze waarden van 1-3 om verschillende typen maps te krijgen
int mapMethod = 3;

// Zet deze waarden van 1-3 om verschillende kleurensets te gebruiken
int visColors = 1;

// Zet deze waarde op true om clustering toe te passen
Boolean applyClustering = true;

public void setup() {
  size(800, 600, P2D);
  noStroke();
  
  locations1 = new ArrayList<Location>();
  locations2 = new ArrayList<Location>();

  setVisMap();
  fillRandomData();

  map.zoomAndPanTo(locations2.get(0), 13);
  
  if (applyClustering)
  {
    clustered1 = ClusterLocations(locations1);
    clustered2 = ClusterLocations(locations2);
  }
}

public List<ClusteredMarker> ClusterLocations(List<Location> source)
{
  // Loop door beide sets en maak er geclusterde data van
  // Pak het eerste punt en kijk of er genoeg 'buren' zijn binnen de radius
  // Zo ja, maak van dit punt een cluster punt en voeg het punt binnen de radius toe
  // verwijder beide punten uit de bron lijst. 
  // bepaal het nieuwe middelpunt
  // kijk opnieuw of er punten in de bronlijst binnen de radius passen
  // de radius groeit per punt wat toegevoegd wordt met growSize
  
  // Maak de resultaat lijst
  List<ClusteredMarker> clustered = new ArrayList<ClusteredMarker>();
  
  float radius = 0.03f; // Speel met deze waarde om de 'reikwijdte' van de clustering aan te passen
  
  float growSize = 0.008f;
  Boolean stopClustering = false;
  ClusteredMarker currentCluster = null;
  
  while (!stopClustering)
  {
    if (currentCluster == null)
    {
      if (source.size() > 0)
      {
        // Zet de nieuwe cluster op dit punt
        currentCluster = new ClusteredMarker(source.get(0), growSize);
        
        // Verwijder het punt uit de bron
        source.remove(0);
      }
      else
      {
        stopClustering = true;
      }

      // Lus door alle items in de source      
      for (int index = source.size()-1; index >= 0; index--)
      {
         Location loc = source.get(index);
         // Bepaal of de plaats van dit item binnen de radius van de huidige cluster ligt
         double distance = Math.sqrt((currentCluster.getLoc().getLat() - loc.getLat()) * (currentCluster.getLoc().getLat() - loc.getLat()) + (currentCluster.getLoc().getLon() - loc.getLon()) * (currentCluster.getLoc().getLon() - loc.getLon()));
         
         distance = Math.abs(distance);
         
         //if (distance <= currentCluster.size)
         if (distance <= radius)
         {
           // Valt binnen onze radius, dus opnemen in deze cluster en verwijderen uit lijst
           // Bepaal nieuw locatie van de cluster als gemiddelde van dit punt en het huidige punt
           currentCluster.getLoc().setLat(currentCluster.getLoc().getLat() + ((loc.getLat() - currentCluster.getLoc().getLat())/2));
           currentCluster.getLoc().setLon(currentCluster.getLoc().getLon() + ((loc.getLon() - currentCluster.getLoc().getLon())/2));
           
           source.remove(index);
           currentCluster.grow(growSize);
         } 
      } 
      
      // Add het cluster aan de lijst
      if (currentCluster != null)
      {
        clustered.add(currentCluster);
        currentCluster = null;
      }

      if (source.size() == 0)
      {
        stopClustering = true;
      }
    }
  }
  
  return clustered;
}

public void fillRandomData()
{
  for (int i = 0; i<12550; i++)
  {
      Random rand = new Random();
      float xdev = rand.nextInt(500) - 250;
      float ydev = rand.nextInt(1000) - 500;
      
      Location pseudoplek = new Location(51.9433333f + (xdev / 10000), 4.4425f + (ydev / 10000));
      locations1.add(pseudoplek);
  }
  
  for (int i = 0; i<7055; i++)
  {
      Random rand = new Random();
      float xdev = rand.nextInt(500) - 250;
      float ydev = rand.nextInt(1000) - 500;
      
      Location pseudoplek = new Location(51.9433333f + (xdev / 10000), 4.4425f + (ydev / 10000));
      locations2.add(pseudoplek);
  }  
}

public void setVisMap()
{
  switch (mapMethod)
  {
    case 1:
      map = new UnfoldingMap(this, new OpenStreetMap.OSMGrayProvider());
      map.setTweening(false);
      MapUtils.createDefaultEventDispatcher(this, map);
      break;
    case 2:
      map = new UnfoldingMap(this, new Microsoft.HybridProvider());
      map.setTweening(false);
      MapUtils.createDefaultEventDispatcher(this, map);
      break;      
    case 3:
      map = new UnfoldingMap(this, new StamenMapProvider.TonerLite());
      map.setTweening(false);
      MapUtils.createDefaultEventDispatcher(this, map);
      break;   
    }
}

public void draw() {
  background(0);
  map.draw();

  if (applyClustering)
  {
    // Teken de clusters en niet de locations
    for (ClusteredMarker cluster: clustered1)
    {
      ScreenPosition position = map.getScreenPosition(cluster.getLoc());
      fill(0, 200, 0, 100);
      float s = map.getZoom() / 5000;
      ellipse(position.x, position.y, 5 + (cluster.getSize() * s), 5 + (cluster.getSize() * s));
    }
    
    for (ClusteredMarker cluster: clustered2)
    {
      ScreenPosition position = map.getScreenPosition(cluster.getLoc());
      fill(200, 0, 0, 100);
      float s = map.getZoom() / 5000;
      ellipse(position.x, position.y, 5 + (cluster.getSize() * s), 5 + (cluster.getSize() * s));
    }
  }
  else
  {
    // Draws locations on screen positions according to their geo-locations.
    for (Location loc: locations1)
    {
      ScreenPosition posBerlin = map.getScreenPosition(loc);
      fill(0, 200, 0, 100);
      float s = map.getZoom();
      //ellipse(posBerlin.x, posBerlin.y, s / 1000, s / 1000);
      ellipse(posBerlin.x, posBerlin.y, 10, 10);
    }
    
    for (Location loc: locations2)
    {
      ScreenPosition posBerlin = map.getScreenPosition(loc);
      fill(200, 0, 0, 100);
      ellipse(posBerlin.x, posBerlin.y, 10, 10);
    }
  }



  
  // Fixed-size marker
  //ScreenPosition posBerlin = map.getScreenPosition(locationBerlin);
  //fill(0, 200, 0, 100);
  //ellipse(posBerlin.x, posBerlin.y, 20, 20);

  // Zoom dependent marker size
  //ScreenPosition posLondon = map.getScreenPosition(locationLondon);
  //fill(200, 0, 0, 100);
  //float s = map.getZoom();
  //ellipse(posLondon.x, posLondon.y, s, s);
}

