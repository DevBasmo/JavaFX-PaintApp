import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;


    
    public class Stroke implements Serializable 
    {
        private List<Double> xPoints = new ArrayList<>();
        private List<Double> yPoints = new ArrayList<>();
        private String colorHex;
        private double lineWidth;
        private String strokeType;
        
        
        public Stroke(String colorHex, double lineWidth, String strokeType )
        {
            this.colorHex = colorHex;
            this.lineWidth = lineWidth;
            this.strokeType = strokeType;
        }
        
        public void addPoint(double x, double y )
        {
            xPoints.add(x);
            yPoints.add(y);
            
        }
        
        
        public List<Double> getXPoints() {
            
            return xPoints;
        }
        
        public List<Double> getYPoints()
        {
            return yPoints;
        }
        
        public String getColorHex()
        {
            return colorHex;
        }
        
        
        public double getLineWidth()
        {
            return lineWidth;
        }
        
        public String getStrokeType()
        {
            return strokeType;
        }
        
        
        
    }
    

