package convertData;
import java.util.List;
import java.util.ArrayList;

public class Category  {
	public String naam;
	public  List<SubCategory> subcategories;
	
	public Category(String naam)
	{
		this.naam = naam;
		this.subcategories = new ArrayList<SubCategory>();
	}
	
	public void addSubCategory(SubCategory scat)
	{	
		this.subcategories.add(scat);
	}
	public SubCategory getSubCategory(String naam, int datatype, float weight){
		for (SubCategory sc : subcategories) {
			if ( sc.naam.equals(naam) ){
				return sc;
			}
		}
		SubCategory returnScat = new SubCategory(naam, datatype, weight);
		subcategories.add(returnScat);
		return returnScat;
	}
}
