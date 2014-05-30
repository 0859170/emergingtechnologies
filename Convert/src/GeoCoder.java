import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class GeoCoder {
	
	private ArrayList<Location> locations;

	public GeoCoder(String projectroot) {
		this.locations = new ArrayList<Location>();
		//read zipcode geolocation file
		BufferedReader fileReader = null;
		String line = "";
		String fileToParse = projectroot+"postcode_NL.csv";
		try {
			fileReader = new BufferedReader(new FileReader(fileToParse));
			while ((line = fileReader.readLine()) != null) {
				
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
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}
