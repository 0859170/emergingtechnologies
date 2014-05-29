import java.util.List;
import java.util.ArrayList;

public class DataProvider  {
	public List<DataSet> datasets;
	
	public DataProvider()
	{
		this.datasets = new ArrayList<DataSet>();
	}
	
	public void addDataSet(DataSet set)
	{
		this.datasets.add(set);
	}

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
