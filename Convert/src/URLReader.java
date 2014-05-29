import java.net.*;
import java.io.*;

public class URLReader {
    public static void main(String[] args) throws Exception {
    	
    }
    
    public String readUrl() {
    	try {
	    	URL oracle = new URL("http://www.oracle.com/");
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(oracle.openStream()));
	
	        String inputLine;
	        String outputLine = "";
	        while ((inputLine = in.readLine()) != null)
	        	outputLine+= inputLine;
	        in.close();
	        
	        return outputLine;
    	} 
    	catch (MalformedURLException e) { 
    	    return "";
    	} 
    	catch (IOException e) {   
    		return "";
    	}
    }
}