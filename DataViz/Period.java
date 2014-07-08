/***************************************************************************************************************************
 File: Period.java
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
 Deze file beschrijft de Period class
 ****************************************************************************************************************************/

/* Imports */
import java.util.List;
import java.util.ArrayList;

/* Period class */
public class Period {
  public int year;
  public int month;
  public List<DataPoint> locations;

  /* Constructor */
  public Period(int year, int month)
  {
    this.year = year;
    this.month = month;
    this.locations = new ArrayList<DataPoint>();
  }

  /* Voeg een locatie toe */
  public void addLocation(DataPoint loc)
  {
    this.locations.add(loc);
  }
}

