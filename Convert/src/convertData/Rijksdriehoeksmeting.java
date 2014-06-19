package convertData;


public class Rijksdriehoeksmeting {
	private static final int X0 = 155000;
	private static final int Y0 = 463000;
	private static final double lat0 = 52.1551744;
	private static final double lng0 = 5.38720621;
	
	private static LatlngpqK[] latpqK = new LatlngpqK[12];
	private static LatlngpqK[] lngpqK = new LatlngpqK[13];
	
	private double lat = 0;
	private double lng = 0;
	private double x = 0;
	private double y = 0;
	
	//public Rijksdriehoeksmeting(double inX, double inY){
	public Rijksdriehoeksmeting(){
		//if (!(x>10000 && x<290000 && y>300000 && y<630000)) return;//XY are out of range
//		x = inX;
//		y = inY;
		//LatlngpqK[] latpqK = new LatlngpqK[12];
		for(int i=1;12>i;i++)latpqK[i]=new LatlngpqK();
		latpqK[1].p=0;latpqK[1].q=1;latpqK[1].K=3235.65389;
		latpqK[2].p=2;latpqK[2].q=0;latpqK[2].K=-32.58297;
		latpqK[3].p=0;latpqK[3].q=2;latpqK[3].K=-0.2475;
		latpqK[4].p=2;latpqK[4].q=1;latpqK[4].K=-0.84978;
		latpqK[5].p=0;latpqK[5].q=3;latpqK[5].K=-0.0665;
		latpqK[6].p=2;latpqK[6].q=2;latpqK[6].K=-0.01709;
		latpqK[7].p=1;latpqK[7].q=0;latpqK[7].K=-0.00738;
		latpqK[8].p=4;latpqK[8].q=0;latpqK[8].K=0.0053;
		latpqK[9].p=2;latpqK[9].q=3;latpqK[9].K=-3.9E-4;
		latpqK[10].p=4;latpqK[10].q=1;latpqK[10].K=3.3E-4;
		latpqK[11].p=1;latpqK[11].q=1;latpqK[11].K=-1.2E-4;
		//LatlngpqK[] lngpqK = new LatlngpqK[13];
		for(int i=1;13>i;i++)lngpqK[i]=new LatlngpqK();
		lngpqK[1].p=1;lngpqK[1].q=0;lngpqK[1].K=5260.52916;
		lngpqK[2].p=1;lngpqK[2].q=1;lngpqK[2].K=105.94684;
		lngpqK[3].p=1;lngpqK[3].q=2;lngpqK[3].K=2.45656;
		lngpqK[4].p=3;lngpqK[4].q=0;lngpqK[4].K=-0.81885;
		lngpqK[5].p=1;lngpqK[5].q=3;lngpqK[5].K=0.05594;
		lngpqK[6].p=3;lngpqK[6].q=1;lngpqK[6].K=-0.05607;
		lngpqK[7].p=0;lngpqK[7].q=1;lngpqK[7].K=0.01199;
		lngpqK[8].p=3;lngpqK[8].q=2;lngpqK[8].K=-0.00256;
		lngpqK[9].p=1;lngpqK[9].q=4;lngpqK[9].K=0.00128;
		lngpqK[10].p=0;lngpqK[10].q=2;lngpqK[10].K=2.2E-4;
		lngpqK[11].p=2;lngpqK[11].q=0;lngpqK[11].K=-2.2E-4;
		lngpqK[12].p=5;lngpqK[12].q=0;lngpqK[12].K=2.6E-4;
		
//		lat=RD2lat(x,y);
//		lng=RD2lng(x,y);
		
		
	}
	
//	public double getLat() {
//		return lat;
//	}
//
//	public double getLng() {
//		return lng;
//	}
//	public float getNiceLat() {
//		return Float.parseFloat(printGPS(lat).replace(",", "."));	
//	}
//	public float getNiceLng() {
//		return Float.parseFloat(printGPS(lng).replace(",", "."));
//	}
	
	public float calcualteNiceLat(double inX, double inY) {
		x = inX;
		y = inY;
		lng=RD2lng(x,y);
		return Float.parseFloat(printGPS(lat).replace(",", "."));	
	}
	public float calcualteNiceLng(double inX, double inY) {
		x = inX;
		y = inY;
		lat=RD2lat(x,y);
		return Float.parseFloat(printGPS(lng).replace(",", "."));
	}
	
//	public String toString(){
//		return printGPS(lat) + " " +  printGPS(lng);
//	}
//	
	private String printGPS(double a){
		return String.format("%.5f", a);
	}

	private double RD2lng(double x2, double y2) {
		// TODO Auto-generated method stub
		double a=0;
		double dX=1E-5*(x2-X0);
		double dY=1E-5*(y2-Y0);
		for(int i = 1; 13 > i; i++)
			a+=lngpqK[i].K*Math.pow(dX,lngpqK[i].p)*Math.pow(dY,lngpqK[i].q);
		return lng0 + a / 3600;
	}

	private double RD2lat(double x2, double y2) {
		// TODO Auto-generated method stub
		double a = 0;
		double dX = 1E-5*(x2-X0);		
		double dY = 1E-5*(y2-Y0);
		for(int i = 1; 12 > i; i++)
			a+=latpqK[i].K*Math.pow(dX,latpqK[i].p)*Math.pow(dY,latpqK[i].q);
		return lat0 + a/3600;
	}

	private static class LatlngpqK{
		public int p;
		public int q;
		public double K;
	}
}
