import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.geo.*;
import processing.core.*;
import java.util.HashMap;

/**
 * A point marker which can show a label containing the marker's name.
 */
public class ClusteredMarker  {

  Location location;
  float size;

  private PFont font;
  private float fontSize = 12;

  public ClusteredMarker(Location location, float size) {
    this.location = location;
    this.size = size;
  }

  /**
   * Displays this marker's name in a box.
   */
  public void draw(PGraphics pg, float x, float y) {
  }
  
  public void grow(float size)
  {
    this.size += size;
  }
  
  public Location getLoc()
  {
    return this.location;
  }
  
  public float getSize()
  {
    return this.size;
  }
}
