package fietsenDiefstalConvert;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import convertData.*;


public class FietsenDiefstalConvert {

	public static void main(String[] args) {
		DataSet data = new DataSet("Misdaad");
		Category cat = new Category("Diefstal");
		SubCategory subcat = new SubCategory("Fietsen",0,1);
		

		int i;
		String line = "";
		String fileToParse = "fietsendiefstal.csv";
		boolean initial = true;
		ArrayList<String> header = new ArrayList<String>();
		
		GeoCoder geo = new GeoCoder();
		Scanner inputStream = new Scanner(FietsenDiefstalConvert.class.getResourceAsStream(fileToParse));
		while (inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			i = 0;
			String[] tokens = line.split(",");
			if (initial) {

				for (String token : tokens) {
					if (token.startsWith("\"")) {
						token = token.substring(1);
					}
					if (token.endsWith("\"")) {
						token = token.substring(0, token.length() - 1);
					}
					header.add(token);
				}
				initial = false;
				continue;

			}
			
			int beginMaand = 0;
			int beginJaar = 0;
			String straat = "";
			String plaats = "";
			
			// Get all tokens available in line
			for (String token : tokens) {
				if (token.startsWith("\"")) {
					token = token.substring(1);
				}
				if (token.endsWith("\"")) {
					token = token.substring(0, token.length() - 1);
				}
				
				if (header.get(i).equals("Begindatum")) {
					beginMaand = Integer.parseInt(token.substring(3, 5));
					beginJaar = Integer.parseInt(token.substring(6, 10));
				}
				
				if (header.get(i).equals("Straat")) {
					straat = token;
				}
				
				if (header.get(i).equals("Plaats")) {
					plaats = token;
				}
				
				i++;
				if (i == header.size())
					break;

			}
			
			Location loc = geo.GeoCodeAddress(straat, plaats);
			if (loc.straatnaam != null) {
				subcat.addLocation(beginJaar,beginMaand,new DataPoint(loc.lat,loc.lng,1));
			}
		}
		inputStream.close();
		cat.addSubCategory(subcat);
		data.addCategory(cat);
		
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("fietsendiefstal.json"), data);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}