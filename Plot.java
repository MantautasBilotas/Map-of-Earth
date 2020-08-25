package assignment;

import javax.swing.*;

public class Plot extends JComponent {
    //Plot variables
    int width = 600, height = 600;
    double xmin=0,  xmax=1, ymin=0, ymax=1;

    //A method to scale given X coordinate between given xmin and xmax
    public  int scaleX(double x) {
        return (int) (width * (x - xmin) / (xmax - xmin));
    }

    //A method to scale given Y coordinate between given xmin and xmax
    public  int scaleY(double y) {
        return (int) (height * (ymin - y)/(ymax - ymin)+height);
    }

    //A method to set minimum x and maximum x for scaling
    public  void setScaleX(double min, double max) {
        xmin = min;   xmax = max;
    }

    //A method to set minimum y and maximum y for scaling
    public  void setScaleY(double min, double max) {
        ymin = min;   ymax = max;
    }
}
