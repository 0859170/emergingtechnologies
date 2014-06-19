package lisaDataConvert;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import convertData.*;

public class LisaConvert {

	/**
	 * @param args
	 */
	public static String inFile = "lisa_data_combined_Cleaned.csv";
	
	
	public static void main(String[] args) {
		DataSet data = new DataSet("Lisa");
		Category cat = new Category("Creatieve industrie");
		GeoCoder geo = new GeoCoder();
		SBICode  sbiCode = new SBICode();
		Rijksdriehoeksmeting rdCalc = new Rijksdriehoeksmeting();
		
		int teller = 0;
		
		Scanner inputStream = new Scanner(LisaConvert.class.getResourceAsStream(inFile));
		while(inputStream.hasNextLine()){
	        //read single line, put in string
	        String line = inputStream.nextLine();
	        //System.out.println(line);
	        String[] tokens = line.split(";");
	        Location loc;
	        if(tokens[3].equals("0,000")){
	        	 loc = geo.GeoCodeAddressByPpostcode(tokens[5]);
	        } else {
	        	loc = new Location();
	        	double x = Double.parseDouble(tokens[3].replace(",", "."));
	        	double y =  Double.parseDouble(tokens[4].replace(",", "."));
	        	loc.lat = rdCalc.calcualteNiceLat(x,y);
	        	loc.lng = rdCalc.calcualteNiceLng(x,y);
	        }
	        if ( loc.lat == 0.0f || loc.lng == 0.0f ){
	        	System.out.println("Skipping: " + tokens[5] + ", Cannot find a suitable location"); 
	        	continue;
	        }
	        SubCategory subCat = cat.getSubCategory(sbiCode.getSBIName(tokens[2]), 1, 1);
	        //System.out.println("printing in cat"+ sbiCode.getSBIName(tokens[2]) + ":" + tokens[0] + loc.lat +","+ loc.lng);
	        subCat.addLocation(Integer.parseInt(tokens[0]), 0, new DataPoint(loc.lat,loc.lng,1));
	        teller++;
	        
	    }
		inputStream.close();
		
		data.addCategory(cat);
		
		System.out.println("I've read "+ teller + " lines" ) ;
		System.out.println("Found "+ cat.subcategories.size() + " subcatagories" ) ;
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			File outFile = new File("lisa.json"); 
			System.out.println("Writing to: " + outFile.getAbsolutePath());
			mapper.writeValue(new File("lisa.json"), data);
			System.out.println("Done Writing!");
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
		
		
	}

}
