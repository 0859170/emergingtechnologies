/***************************************************************************************************************************
 File: DataPoint.java
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
 Deze file beschrijft de DataPoint class, deze wordt gebruikt om de te tekenen punten in op te slaan
 ****************************************************************************************************************************/

/* De DataPoint class */
public class DataPoint {
  /* Interne variabelen */
  public float lat;
  public float lng;
  public float amount;

  /* Constructor */
  public DataPoint(float lat, float lng, float amount)
  {
    this.lat = lat;
    this.lng = lng;
    this.amount = amount;
  }
}

