import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

public class Convert {

	public static void main(String[] args) {
		DataSet data = new DataSet("Misdaad");
		Category cat = new Category("Diefstal");
		SubCategory subcat = new SubCategory("Fietsen",1,1);
		
		BufferedReader fileReader = null;
		int i;
		String line = "";
		String fileToParse = "/Users/Mark/Documents/workspaces/HRO/INFPR201A2/fietsendiefstal.csv";
		boolean initial = true;
		ArrayList<String> header = new ArrayList<String>();

		try {
			fileReader = new BufferedReader(new FileReader(fileToParse));
			while ((line = fileReader.readLine()) != null) {
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
				
				subcat.addLocation(beginJaar,beginMaand,new DataPoint(51.9197405f,4.487553f,1));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		cat.addSubCategory(subcat);
		data.addCategory(cat);
		
		URLReader response = new URLReader();
		System.out.println(response.readUrl());
	}

}