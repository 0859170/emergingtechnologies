package convertData;
import java.util.ArrayList;
import java.util.Scanner;


public class GeoCoder {
	
	private ArrayList<Location> locations;

	public GeoCoder() {
		this.locations = new ArrayList<Location>();
		//read zipcode geolocation file
		String line = "";
		String fileToParse = "postcode_NL.csv";

			
		Scanner inputStream = new Scanner(GeoCoder.class.getResourceAsStream(fileToParse));
		while (inputStream.hasNextLine() ) {
			line = inputStream.nextLine(); 
			int i = 0;
			Location loc = new Location();
			
			String[] tokens = line.split(";");
			for (String token : tokens) {
				if (token.startsWith("\"")) {
					token = token.substring(1);
				}
				if (token.endsWith("\"")) {
					token = token.substring(0, token.length() - 1);
				}
				
				switch(i) {
				case 1:
					loc.postcode = token;
					break;
				case 8:
					loc.straatnaam = token.toUpperCase();
					break;
				case 9:
					loc.plaats = token.toUpperCase();
					break;
				case 15:
					loc.lat = Float.parseFloat(token);
					break;
				case 16:
					loc.lng = Float.parseFloat(token);
					break;
				}
				i++;
			}
			
			this.locations.add(loc);
				
		}
		inputStream.close();

	}
	
	public Location GeoCodeAddress(String straatnaam, String plaats) {
		
		for(Location loc : this.locations) {
			//System.out.println(loc.straatnaam+" == "+straatnaam);
			if (loc.straatnaam.equals(straatnaam) && loc.plaats.equals(plaats)) {
				return loc;
			}
		}
		
		return new Location();
	}
	public Location GeoCodeAddressByPpostcode(String postcode) {
		//System.out.println("looking for " + postcode ); 
		for(Location loc : this.locations) {
			//System.out.println(loc.straatnaam+" == "+straatnaam);
			if ( loc.postcode.equals(postcode) ) {
			//	System.out.println("Found " +  loc.lat +","+ loc.lng ); 
				return loc;
			}
		}
		
		return new Location();
	}

}
