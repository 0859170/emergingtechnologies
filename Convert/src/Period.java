import java.util.List;
import java.util.ArrayList;

public class Period  {
	public int year;
	public int month;
	public List<DataPoint> locations;
	
	public Period(int year, int month)
	{
		this.year = year;
		this.month = month;
		this.locations = new ArrayList<DataPoint>();
	}
	
	public void addLocation(DataPoint loc)
	{
		this.locations.add(loc);
	}
}
