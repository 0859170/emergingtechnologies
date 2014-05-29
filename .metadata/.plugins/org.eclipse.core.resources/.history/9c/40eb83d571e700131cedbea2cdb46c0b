import java.net.URLEncoder;


public class GeoCoder {

	public GeoCoder() {
		
	}
	
	public static void GeoCode(String straatnaam, String plaats) {
		String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		url+= URLEncoder.encode(straatnaam+" "+plaats);
		url+= "&sensor=false&key=AIzaSyDL__Og_Cue1Jud5MEhuh0oZwPQ3ZzR038";
	}

}
