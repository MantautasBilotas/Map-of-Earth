package assignment;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.List;

public class Earth {
    //Earth variables
    private double[][] arrayOfEarth;
    private Map<Double, TreeMap<Double, Double>> mapOfEarth;
    public int count = 0;

    //Variables needed for PlotMap class
    public List<MapCoordinate> coordinatesOfMap = new ArrayList<>();
    public MapCoordinate last;

    //Reading the file into an Array
    public void readDataArray(String fileName) throws Exception {
        Scanner in = new Scanner(new File(fileName));
        while (in.hasNextLine()) {
            this.count++;
            in.nextLine();
        }

        arrayOfEarth = new double[count][3];
        in.close();
        in = new Scanner(new File(fileName));
        for (double[] each : arrayOfEarth) {
            String line = in.nextLine();
            String[] a = line.split("\t");
            for (int i = 0; i < 3; i++)
            {
                each[i] = Double.parseDouble(a[i]);
            }
        }}

     //Putting the coordinates above given altitude into a list
    public List<String> coordinatesAbove(double altitude) {
        List<String> aboveCor = new ArrayList<>();
        for (double[] every : arrayOfEarth) {
            if (altitude < every[2]) {  //or <= if you include equal altitudes
                aboveCor.add(Arrays.toString(every));
            }
        }
        return aboveCor;
    }

    //Putting the coordinates below given altitude into a list
    public List<String> coordinatesBelow(double altitude) {
        List<String> belowCor = new ArrayList<>();
        for (double[] every : arrayOfEarth) {
            if (altitude > every[2]) {  //or >= if you include equal values
                belowCor.add(Arrays.toString(every));
            }
        }
        return belowCor;
    }

    //Prints out the percentage of altitudes above the given one
    public void percentageAbove(double altitude) {
        double listSize = coordinatesAbove(altitude).size();
        double percent = (listSize / count) * 100;
        System.out.println("The percentage of coordinates above is " + String.format("%.1f", percent) + "%");
        //Leaves only one decimal spot
    }

    //Prints out the percentage of altitudes below the given one
    public void percentageBelow(double altitude) {
        double listSize = coordinatesBelow(altitude).size();
        double percent = (listSize / count) * 100;
        System.out.println("The percentage of coordinates below is " + String.format("%.1f", percent) + "%");
        //Leaves only one decimal spot
    }

    //A code that will prompt the user to enter an altitude in meters
    // on the command line and print the percentage of coordinates above this altitude.
    public void altitudesAbove() {
        Scanner input = new Scanner(System.in);
        String quit = "quit";
        System.out.println("Please enter an altitude in metres or \"quit\" to end program.");
        String altitude = input.nextLine();

        //Lasts until user puts in word "quit"
        while (!altitude.equalsIgnoreCase(quit)) {
            try {
                percentageAbove(Double.parseDouble(altitude));
                System.out.println("Please enter an altitude or \"quit\" to end program.");
                altitude = input.nextLine();}
            catch (Exception e) {  //If there are any exceptions the following message will be printed
                System.out.println("Invalid altitude. Please enter an altitude or \"quit\" to end program.\n");
                altitude = input.nextLine();
            }}

        if (altitude.equalsIgnoreCase(quit)) {System.out.println("Bye"); }input.close();
    }

    //Reading the file into a map
    public void readDataMap(String fileName) throws Exception{
        Scanner in = new Scanner(new File(fileName));
        mapOfEarth = new TreeMap<>();
        String line;
        double longitude, latitude, altitude;

        while(in.hasNextLine()){
            line = in.nextLine();
            String[] curData = line.split("\t");
            longitude = Double.parseDouble(curData[0]);
            latitude = Double.parseDouble(curData[1]);
            altitude = Double.parseDouble(curData[2]);
            if(mapOfEarth.get(longitude) == null){
                mapOfEarth.put(longitude, new TreeMap<>()); }

                mapOfEarth.get(longitude).put(latitude, altitude);
        }
        in.close();
    }

    //A method to return a map, so that it can be used since it is private
    public Map<Double, TreeMap<Double, Double>> getMapOfEarth(){ return mapOfEarth; }

    //A method to generate a random map with random altitudes
    public void generateMap(double resolution){
        mapOfEarth = new TreeMap<>();
        double longitude, latitude, altitude;
        double minAlt = -11034; //depth of the lowest natural point on earth (Mariana Trench)
        double maxAlt = 8848; //height of the highest natural point on earth (Mount Everest)

        double maxLong = 360;
        double maxLat = 90;

        for(double i=0; i<=maxLong; i+=resolution){
            longitude = i;
            for (double j=-maxLat; j<=maxLat; j+=resolution){
                latitude = j;
                altitude = Math.round((Math.random() * ((maxAlt - minAlt) + 1)) + minAlt);
                if(mapOfEarth.get(longitude) == null){
                    mapOfEarth.put(longitude, new TreeMap<>()); }

                    mapOfEarth.get(longitude).put(latitude, altitude);}
    }}

    //A method to get an altitude given concrete coordinates
    public double getAltitude(double longitude, double latitude){ return mapOfEarth.get(longitude).get(latitude); }

    //A method that iterates over map of earth and changes altitudes accordingly to given metres
    public void seaLevels(double metres){
        Map<Double, TreeMap<Double,Double>> map = mapOfEarth;
        for(Map.Entry<Double, TreeMap<Double,Double>> each : map.entrySet())
        {
            Map<Double,Double> map2 = each.getValue();
            for(Map.Entry<Double,Double> each1 : map2.entrySet()){
                double curAlt = each1.getValue();
                each1.setValue(curAlt-metres);
            }}
    }

    //This method creates a new file every time a program is run. If the file of given filename exists already
    //it deletes it and creates a new one of the same name
    //It also prints a list of coordinates to that file
    public void printingToTheFile(List<MapCoordinate> list,String filename){
        try {
               File create = new File(filename);
               if(create.exists())
               {create.delete();}
               File newOne = new File(filename);
               FileWriter in = new FileWriter(newOne);
                for(MapCoordinate each: list) {
                    in.write(each.toString() + "\n");
                }
            System.out.println("Coordinates printed to the file");
                in.close();
        }
         catch (Exception e) { //If there are any exceptions the following message will be printed
            System.out.println("An error occurred.");
        }}

    //Main method that asks user to provide longitude and latitude
    // and then prints out the altitude at that point
    public static void main(String[] args) throws Exception {
        Earth ert = new Earth();
        ert.readDataMap("C:\\Users\\Admin\\IdeaProjects\\CE152home\\src\\earth.xyz");
        Scanner in = new Scanner(System.in);
        String quit = "quit";
        System.out.println("Please enter longitude and latitude or \"quit\" to end program.");
        String coordinate1=in.next();
        if(!coordinate1.equalsIgnoreCase(quit))
        {String coordinate2=in.next(); //Only takes the second coordinate if the user does not quit

        //Lasts until user writes "quit"
        while ((!coordinate1.equalsIgnoreCase(quit))) {
            try {
                double altitude;
                altitude = ert.getAltitude(Double.parseDouble(coordinate1),Double.parseDouble(coordinate2));
                System.out.println("The altitude at longitude " + Double.valueOf(coordinate1)
                        + " and latitude " + Double.valueOf(coordinate2) + " is: " + altitude + " metres.");
                System.out.println("Please enter an altitude or \"quit\" to end program.");
                coordinate1 = in.next();
                if(!coordinate1.equalsIgnoreCase(quit))
                {coordinate2=in.next();}}
            catch (Exception e) {
                System.out.println("Invalid coordinates. Please enter longitude and latitude or \"quit\" to end program.\n");
                coordinate1 = in.next();
                if(!coordinate1.equalsIgnoreCase(quit))
                {coordinate2=in.next();}
            }}}
        if (coordinate1.equalsIgnoreCase(quit)) {System.out.println("Bye"); in.close();}
    }
}
