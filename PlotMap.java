package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class PlotMap extends Plot { //extends Plot from CE152 course and Plot extends JComponent
    //PlotMap variables
    Earth e;
    int point = 1;

    //PlotMap constructor
    public PlotMap(String filename, double metres) throws Exception{
        this.e = new Earth();
        e.readDataMap(filename); //automatically reads the map
        e.seaLevels(metres);     //automatically rises the sea levels by the given metres

        //Setting the minimum and maximum values of x and y for JFrame
        setScaleX(0, 360);
        setScaleY(-90, 90);
    }

    //A method to save exact coordinates of MapOfEarth according to scaled values
    public Map<Integer,Map<Integer,MapCoordinate>> savingScaled(){
        Map<Integer,Map<Integer,MapCoordinate>> scaled = new TreeMap<>();
        Map<Double, TreeMap<Double,Double>> map = e.getMapOfEarth();
        for(Map.Entry<Double, TreeMap<Double,Double>> entry : map.entrySet())
        {
            double curLong = entry.getKey();
            Map<Double,Double> map2 = entry.getValue();
            for(Map.Entry<Double,Double> entry1 : map2.entrySet()){
                double curLat = entry1.getKey();
                if(curLong>180){
                    //Subtracting 180 from every longitude above 180 because of the centering in paintComponent
                    if(scaled.get(scaleX(curLong-180)) == null){
                    scaled.put(scaleX(curLong-180), new TreeMap<>()); }
                    else{
                    MapCoordinate coordinate = new MapCoordinate(curLong,curLat,e.getAltitude(curLong,curLat));
                    scaled.get(scaleX(curLong-180)).put(scaleY(curLat), coordinate);}}
                else {
                    //Adding 180 to every longitude below 180 because of the centering in paintComponent
                    if(scaled.get(scaleX(curLong+180)) == null){
                    scaled.put(scaleX(curLong+180), new TreeMap<>()); }
                    else{
                    MapCoordinate coordinate = new MapCoordinate(curLong,curLat,e.getAltitude(curLong,curLat));
                    scaled.get(scaleX(curLong+180)).put(scaleY(curLat), coordinate);}}

            }}return scaled;
    }

    //Overriding paintComponent to produce a map in a JFrame
    @Override
    public void paintComponent(Graphics g){
        Graphics2D graphics2D = (Graphics2D)g;

        this.width = getWidth();
        this.height = getHeight();

        Map<Double, TreeMap<Double,Double>> map = e.getMapOfEarth();

        for(Map.Entry<Double, TreeMap<Double,Double>> entry : map.entrySet())
        {   double curLong = entry.getKey(); //setting current longitude
            double minAlt = -11034; //depth of the lowest natural point on earth (Mariana Trench)
            double maxAlt = 8848; //height of the highest natural point on earth (Mount Everest)


          Map<Double,Double> map2 = entry.getValue();
          for(Map.Entry<Double,Double> entry1 : map2.entrySet()){
              double curLat = entry1.getKey(); //setting current latitude
              double curAlt = map.get(curLong).get(curLat); //getting altitude

                    //Setting colour gradients:
                    //From 0 to 1000 is green, gets greener every 250 metres
                    if (curAlt>0 && curAlt<=250)
                    {graphics2D.setColor(new Color(70,(((int)((curAlt)*(255/250)))),10));}
                    else if (curAlt>250 && curAlt<=500)
                    {graphics2D.setColor(new Color(90,(((int)((curAlt-250)*(255/250)))),20));}
                    else if (curAlt>500 && curAlt<=750)
                    {graphics2D.setColor(new Color(110,(((int)((curAlt-500)*(255/250)))),30));}
                    else if (curAlt>750 && curAlt<=1000)
                    {graphics2D.setColor(new Color(130,(((int)((curAlt-750)*(255/250)))),40));}

                    //From 1000 to 6000 is red, gets darker till 6000 metres
                    else if (curAlt>1000 && curAlt<=2000)
                    {graphics2D.setColor(new Color((255-((int)((curAlt-1000)*(255/1000)))),200,40));}
                    else if (curAlt>2000 && curAlt<=3000)
                    {graphics2D.setColor(new Color((255-((int)((curAlt-2000)*(255/1000)))),150,40));}
                    else if (curAlt>3000 && curAlt<=6000)
                    {graphics2D.setColor(new Color(((255-((int)((curAlt-3000)*(255/(maxAlt-3000)))))),100,40));}

                    //Everything above 6000 is white, thus protecting from errors if sea levels drastically fall
                    else if (curAlt>6000)
                    {graphics2D.setColor(new Color(255,255,255));}
                    //Everything bellow -11034 is black, thus protecting from errors if sea levels drastically rise
                    else if (curAlt<minAlt)
                    { {graphics2D.setColor(new Color(0,0,0));}}
                    //Everything else is blue
                    else
                    {graphics2D.setColor(new Color(25,50,((255-(int)(curAlt*(255/minAlt))))));}

                    //Centering the map and filling JFrame with scaled values
                    if(curLong<180)
                    {graphics2D.fillRect(scaleX(curLong+180), scaleY(curLat), point, point);}
                    else
                    {graphics2D.fillRect(scaleX(curLong-180), scaleY(curLat), point, point);} }
        }
    }

    //Main method that creates a JFrame and asks the user to provide the risen sea level in metres
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the the height of risen sea levels in metres");
        try {
            String metres = in.nextLine();
            in.close();

            //Setting up the JFrame
            JFrame jf = new JFrame("Map of Earth");
            jf.getContentPane().setPreferredSize(new Dimension(1440, 720));
            PlotMap pm = new PlotMap("C:\\Users\\Admin\\IdeaProjects\\CE152home\\src\\earth.xyz", Double.parseDouble(metres));
            jf.setResizable(false);
            jf.add(pm);
            jf.pack();
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf.setVisible(true);
            //Introducing Mouse Listener
            jf.addMouseListener(new MouseAdapter() {
                //Overriding mouseClicked method to behave accordingly
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                        //For some reason x value of JFrame on my computer begins from 8
                        // and y value from 30, that is why subtraction of those values are needed
                        // I believe that it would not be necessary on other devices
                        MapCoordinate x = pm.savingScaled().get(e.getX()-8).get(e.getY()-30);
                        pm.e.coordinatesOfMap.add(x);
                        System.out.println("Clicked coordinates: ");
                        System.out.println(x.toString());

                        if(pm.e.last != null){
                            System.out.println("The distance between previous clicked point and the current one is: ");
                            System.out.println(String.format("%.2f", x.distance(pm.e.last))+" km");}

                        pm.e.last = x;
                        Collections.sort(pm.e.coordinatesOfMap); }

                    else if(e.getModifiers()==MouseEvent.BUTTON3_MASK) {
                        if(pm.e.last != null && pm.e.coordinatesOfMap.contains(pm.e.last)) {
                        pm.e.coordinatesOfMap.remove(pm.e.last);
                        System.out.println("Previous point deleted: ");
                        System.out.println(pm.e.last.toString()); }

                    else { System.out.println("Coordinate cannot be deleted");}
                    }}});

            //If I understood the task correctly it says that we may overwrite the existing file only
            //when we start the program over but not every time when we add a new value to the list
            //of clicked coordinates. I found that only possible to do with window listener
            //Introducing WindowListener
            jf.addWindowListener(new WindowAdapter() {
                //Overriding windowClosing method to print the coordinates to the file when it is closing
                @Override
                public void windowClosing(WindowEvent e) {
                    pm.e.printingToTheFile(pm.e.coordinatesOfMap,"coordinates.txt");
                    jf.dispose(); }});
        }
        catch (Exception e) //Checking if the input of the user was right
        {
            System.out.println("Invalid height. Please enter height in metres");}
    }}
