/***************************************************************************************************************************
 File: DataProvider.java
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
 Deze file implementeert de data-provider. Een helper class welke de gewenste datapunten voor een bepaalde periode teruggeeft
 ook als er voor die specifieke periode geen data beschibaar is, de laatste date voor het gevraagde punt wordt dan gepakt.
 ****************************************************************************************************************************/

/* Imports */
import java.util.List;
import java.util.ArrayList;

/* De DataProvider class*/
public class DataProvider {
  /* Interne variabelen */
  public List<DataSet> datasets;

  /* Constructor */
  public DataProvider()
  {
    this.datasets = new ArrayList<DataSet>();
  }

  /* Voeg een dataset toe */
  public void addDataSet(DataSet set)
  {
    this.datasets.add(set);
  }

  /* 'toggle' (uit als aan, aan als uit) de gekozen dataset en onderliggende data */
  public void toggleDataSetSelected(int index)
  {
    // Itereer door dataset/category/subcategory
    for (DataSet set: this.datasets)
    {
      if (set.id == index)
      {
        // toggle alles hieronder
        for (Category cat: set.categories)
        {
          for (SubCategory scat: cat.subcategories)
          {
            scat.selected = !scat.selected;
          }
        }
      }
      else
      {
        // Ga dieper kijken
        for (Category cat: set.categories)
        {
          if (cat.id == index)
          {
            for (SubCategory scat: cat.subcategories)
            {
              scat.selected = !scat.selected;
            }
          }
          else
          {
            // Dieper
            for (SubCategory scat: cat.subcategories)
            {
              if (scat.id == index)
              {
                scat.selected = !scat.selected;
              }
            }
          }
        }
      }
    }
  }

  /* Geeft het hoogste jaar uit de gecombineerde datasets */
  public int getMaxJaar()
  {
    int jaar = 0;

    // Itereer de datasets
    for (DataSet set: this.datasets)
    {
      for (Category cat: set.categories)
      {
        for (SubCategory scat: cat.subcategories)
        {
          // Als deze subcat selected is nemen we hem sowieso mee
          if (scat.selected)
          {
            SubCategory subcat = new SubCategory(scat.id, scat.naam, scat.datatype, scat.weight, scat.red, scat.green, scat.blue);

            for (Period per: scat.periods)
            {
              if (per.year > jaar)
              {
                jaar = per.year;
              }
            }
          }
        }
      }
    }

    return jaar;
  } 

  /* Geeft het kleinste jaar uit de gecombineerde datasets */
  public int getMinJaar()
  {
    int jaar = 99999;

    // Itereer de datasets
    for (DataSet set: this.datasets)
    {
      for (Category cat: set.categories)
      {
        for (SubCategory scat: cat.subcategories)
        {
          // Als deze subcat selected is nemen we hem sowieso mee
          if (scat.selected)
          {
            SubCategory subcat = new SubCategory(scat.id, scat.naam, scat.datatype, scat.weight, scat.red, scat.green, scat.blue);

            for (Period per: scat.periods)
            {
              if (per.year < jaar)
              {
                jaar = per.year;
              }
            }
          }
        }
      }
    }

    return jaar;
  }

  /* Geeft de hoogste maand uit de gecombineerde datasets, voor het meegegeven jaar */
  public int getMaxMaand(int jaar)
  {
    int maand = 1;

    // Itereer de datasets
    for (DataSet set: this.datasets)
    {
      for (Category cat: set.categories)
      {
        for (SubCategory scat: cat.subcategories)
        {
          // Als deze subcat selected is nemen we hem sowieso mee
          if (scat.selected)
          {
            SubCategory subcat = new SubCategory(scat.id, scat.naam, scat.datatype, scat.weight, scat.red, scat.green, scat.blue);

            for (Period per: scat.periods)
            {
              if (per.year == jaar)
              {
                if (per.month > maand)
                {
                  maand = per.month;
                }
              }
            }
          }
        }
      }
    }

    return maand;
  }

  /* Geeft de laagste maand uit de gecombineerde datasets, voor het meegegeven jaar */
  public int getMinMaand(int jaar)
  {
    int maand = 12;

    // Itereer de datasets
    for (DataSet set: this.datasets)
    {
      for (Category cat: set.categories)
      {
        for (SubCategory scat: cat.subcategories)
        {
          // Als deze subcat selected is nemen we hem sowieso mee
          if (scat.selected)
          {
            SubCategory subcat = new SubCategory(scat.id, scat.naam, scat.datatype, scat.weight, scat.red, scat.green, scat.blue);

            for (Period per: scat.periods)
            {
              if (per.year == jaar)
              {
                if (per.month < maand)
                {
                  maand = per.month;
                }
              }
            }
          }
        }
      }
    }

    return maand;
  }

  /* Geef de data van alle actieve datasets voor deze jaar/maand combinatie */
  /* Als er geen exacte match is voor een dataset, geef dan de vorige beschikbare periode */
  public List<SubCategory> getDataForMoment(int year, int month)
  {
    // Itereer door alle subsets heen en retourneer de dichtsbijzijnde selected subcat periods
    List<SubCategory> result = new ArrayList<SubCategory>();

    // Itereer de datasets
    for (DataSet set: this.datasets)
    {
      for (Category cat: set.categories)
      {
        for (SubCategory scat: cat.subcategories)
        {
          // Als deze subcat selected is nemen we hem sowieso mee
          if (scat.selected)
          {
            SubCategory subcat = new SubCategory(scat.id, scat.naam, scat.datatype, scat.weight, scat.red, scat.green, scat.blue);

            // Oke, nu bekijken welke period het beste past bij de gevraagde jaar/maand
            Period thePeriod = scat.periods.get(0); // De eerste altijd gelijk zetten

            for (Period per: scat.periods)
            {
              if (per.year > year || (per.year >= year && per.month > month))
              {
                // We zijn 'te ver' gebruik de vorige period
                break;
              }
              else
              {
                thePeriod = per;
              }
            }

            // Deze periode toevoegen aan de subcat
            subcat.addPeriod(thePeriod);

            // En de subcat aan de lijst
            result.add(subcat);
          }
        }
      }
    }

    return result;
  }
}

