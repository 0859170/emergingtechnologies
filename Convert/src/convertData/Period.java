package convertData;
import java.util.List;
import java.util.ArrayList;

public class Period  {
	public int jaar;
	public int maand;
	public List<DataPoint> locations;
	
	public Period(int year, int month)
	{
		this.jaar = year;
		this.maand = month;
		this.locations = new ArrayList<DataPoint>();
	}
	
	public void addLocation(DataPoint loc)
	{
		this.locations.add(loc);
	}
}
