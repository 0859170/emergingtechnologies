import java.util.List;
import java.util.ArrayList;

public class SubCategory  {
	public int id;
	public String naam;
	public int datatype;
	public float weight;
	public boolean selected;
	public int red;
	public int green;
	public int blue;
	public List<Period> periods;
	
	public SubCategory(int id, String naam, int datatype, float weight, int red, int green, int blue)
	{
		this.id = id;
		this.naam = naam;
		this.datatype = datatype;
		this.weight = weight;
		this.selected = true;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.periods = new ArrayList<Period>();
	}
	
	public void setRGB(int red, int green, int blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public void addPeriod(Period per)
	{
		this.periods.add(per);
	}
}
