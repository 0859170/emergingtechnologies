/***************************************************************************************************************************
File: Category.java
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
Deze file beschrijft de category class
****************************************************************************************************************************/

/* Imports */
import java.util.List;
import java.util.ArrayList;

/* De category class */
public class Category  {
 /* Interne variabelen */
	public int id;
	public String naam;
 public List<SubCategory> subcategories;
	
 /* Constructor */
	public Category(int id, String naam)
	{
		this.id = id;
		this.naam = naam;
		this.subcategories = new ArrayList<SubCategory>();
	}
	
 /* Voeg een sub-categorie toe */
	public void addSubCategory(SubCategory scat)
	{	
		this.subcategories.add(scat);
	}
}
