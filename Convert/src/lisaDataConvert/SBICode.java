package lisaDataConvert;

import java.util.HashMap;
import java.util.Scanner;



public class SBICode {

	
	private HashMap<String,String> SBICode = new HashMap<>();

	public SBICode() {

		String line = "";
		String fileToParse = "SBICodes.csv";

			
		Scanner inputStream = new Scanner(SBICode.class.getResourceAsStream(fileToParse));
		while (inputStream.hasNextLine() ) {
			line = inputStream.nextLine(); 
			
			String[] tokens = line.split(";");
			if (tokens[0].equals("SBI"))
				continue; //this is the header
			
			SBICode.put(tokens[0], tokens[1]);
		}
		inputStream.close();

	}
	
	public String getSBIName(String inSBICode){

		if (SBICode.containsKey(inSBICode))
			return SBICode.get(inSBICode);
		System.out.println("key not found:" + inSBICode);
		//Changes in SBI http://www.kvk.nl/over-de-kvk/over-het-handelsregister/wat-staat-er-in-het-handelsregister/overzicht-sbi-codes/
		switch (inSBICode) {
		case "6321":
			return "persagentschappen";
		case "6329":
			return "overige dienstverlenende activiteiten op het gebied van informatie";
		} 
		
		return inSBICode;
	}
	
}
