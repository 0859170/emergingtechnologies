import java.util.List;
import java.util.ArrayList;

public class DataSet  {
	public int id;
	public String naam;
	public List<Category> categories;
	
	public DataSet(int id, String naam)
	{
		this.id = id;
		this.naam = naam;
		this.categories = new ArrayList<Category>();
	}
	
	public void addCategory(Category cat)
	{
		this.categories.add(cat);
	}
}
