/***************************************************************************************************************************
 File: DataSet.java
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
 Deze file beschrijft de DataSet class
 ****************************************************************************************************************************/

/* Imports */
import java.util.List;
import java.util.ArrayList;

/* DataSet class */
public class DataSet {
  /* Interne variabelen */
  public int id;
  public String naam;
  public List<Category> categories;

  /* Constructor */
  public DataSet(int id, String naam)
  {
    this.id = id;
    this.naam = naam;
    this.categories = new ArrayList<Category>();
  }

  /* Voeg een categorie toe */
  public void addCategory(Category cat)
  {
    this.categories.add(cat);
  }
}

