package convertData;
import java.util.List;
import java.util.ArrayList;

public class DataSet  {
	public String naam;
	public List<Category> categories;
	
	public DataSet(String naam)
	{
		this.naam = naam;
		this.categories = new ArrayList<Category>();
	}
	
	public void addCategory(Category cat)
	{
		this.categories.add(cat);
	}
	
	public Category getCategory(String naam){
		for (Category cat : categories) {
			if ( cat.naam.equals(naam) ){
				return cat;
			}
		}
		Category returnCat = new Category(naam);
		categories.add(returnCat);
		return returnCat;
	}
}
