package assignment;


public class MapCoordinate implements Comparable<MapCoordinate>{
    //Implementing Comparable so that it would be possible to compare coordinates
    //MapCoordinate variables
    public final double LONGITUDE;
    public final double LATITUDE;
    public final double ALTITUDE;

    //MapCoordinate constructor
    public MapCoordinate(double LONGITUDE,double LATITUDE, double ALTITUDE) {
        this.LONGITUDE = LONGITUDE;
        this.LATITUDE = LATITUDE;
        this.ALTITUDE = ALTITUDE;
    }

    //A method that counts the distance between two coordinates excluding altitude
    //Using Harvesine's formula
    public double distance(MapCoordinate m){
       double R = 6372.8;
       double latitude = Math.toRadians(LATITUDE-m.LATITUDE);
       double longitude = Math.toRadians(LONGITUDE-m.LONGITUDE);
       double d = Math.pow(Math.sin(latitude/2),2)+Math.pow(Math.sin(longitude/2),2)*Math.cos(Math.toRadians(LATITUDE))*Math.cos(Math.toRadians(m.LATITUDE));
       double c = Math.asin(Math.sqrt(d));
       return R*c;
    }

    //Overriding compareTo method
    public int compareTo(MapCoordinate m) {
        if(ALTITUDE==m.ALTITUDE && LATITUDE==m.LATITUDE && LONGITUDE==m.LONGITUDE)
            return 0;
        else if(ALTITUDE>m.ALTITUDE || (ALTITUDE==m.ALTITUDE && LATITUDE > m.LATITUDE) || ((ALTITUDE==m.ALTITUDE && LATITUDE == m.LATITUDE && LONGITUDE>m.LONGITUDE)))
            return 1;
        else
            return -1;
    }

    //Overriding equals method
    @Override
    public boolean equals(Object m) {
        if (this == m) return true;
        if (m == null) return false;
        if (getClass() != m.getClass()) return false;
        MapCoordinate c = (MapCoordinate) m;
        return this.compareTo(c) == 0;

    }

    //Overriding toString method
    @Override
    public String toString() {
        double[] l = new double[3];
        l[0]=ALTITUDE;
        l[1]=LATITUDE;
        l[2]=LONGITUDE;
        return "Altitude: "+l[0]+" Latitude: "+l[1]+" Longitude: "+l[2];
    }

}