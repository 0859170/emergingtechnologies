package convertData;
import java.util.List;
import java.util.ArrayList;

public class SubCategory  {
	public String naam;
	public int datatype;
	public float weight;
	public List<Period> periods;
	
	public SubCategory(String naam, int datatype, float weight)
	{
		this.naam = naam;
		this.datatype = datatype;
		this.weight = weight;
		this.periods = new ArrayList<Period>();
	}
	
	public void addLocation(int jaar, int maand, DataPoint location)
	{
		boolean found =false;
		
		for (Period per: this.periods)
		{
			if (per.jaar == jaar && per.maand == maand)
			{
				found =true;
				per.addLocation(location);
			}
		}
		
		if (!found)
		{
			Period per = new Period(jaar,maand);
			per.addLocation(location);
			this.addPeriod(per);
		}
	}
	
	public void addPeriod(Period per)
	{
		this.periods.add(per);
	}
}
