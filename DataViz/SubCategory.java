/***************************************************************************************************************************
 File: SubCategory.java
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
 Deze file beschrijft de SubCategory class
 ****************************************************************************************************************************/

/* Imports */
import java.util.List;
import java.util.ArrayList;

/* SubCategory class */
public class SubCategory {
  /* Interne variabelen */
  public int id;
  public String naam;
  public int datatype;
  public float weight;
  public boolean selected;
  public int red;
  public int green;
  public int blue;
  public List<Period> periods;

  /* Constructor */
  public SubCategory(int id, String naam, int datatype, float weight, int red, int green, int blue, boolean selected)
  {
    this.id = id;
    this.naam = naam;
    this.datatype = datatype;
    this.weight = weight;
    this.selected = selected;
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.periods = new ArrayList<Period>();
  }

  /* Geef deze sub category een kleurcode */
  public void setRGB(int red, int green, int blue)
  {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /* Voeg een periode toe */
  public void addPeriod(Period per)
  {
    this.periods.add(per);
  }
}

