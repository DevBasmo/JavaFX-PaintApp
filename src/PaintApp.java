
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class PaintApp extends Application {
    
    private GraphicsContext gc;
    private List<Stroke> strokes = new ArrayList<>();
    private Stroke currentStroke;
    private Button clearButtonn;
    private Color currentColor = Color.BLACK;
    private double currentLineWidth = 2;
    private String currentStrokeType = "THIN";

   
    public static void main(String[] args) {
        launch ();
    }

    @Override
    public void start(Stage stage)
    {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            
            gc.setStroke(currentColor);
            gc.setLineWidth(currentLineWidth);
            
            if (currentStrokeType.equals("BROKEN"))
            {
                gc.setLineDashes(10);
            }
            else
            {
                gc.setLineDashes(null);
            }
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
            
            
            String hex = currentColor.toString();
            currentStroke = new Stroke(hex, currentLineWidth, currentStrokeType);
            currentStroke.addPoint(e.getX(), e.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();

            currentStroke.addPoint(e.getX(), e.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            strokes.add(currentStroke);
        });

        // Save to JPEG
        Button saveJpegBtn = new Button("Save as JPEG");
        saveJpegBtn.setOnAction(e -> saveAsJPEG(canvas, stage));

        // Save to .basit
        Button saveBasitBtn = new Button("Save as .basit");
        saveBasitBtn.setOnAction(e -> saveAsBasit(stage));

        // Load from .basit
        Button loadBasitBtn = new Button("Load .basit");
        loadBasitBtn.setOnAction(e -> {
            loadBasit(stage);
            redrawCanvas();
          
});
         clearButtonn = new Button("Clear");
         clearButtonn.setOnAction(e -> 
         {
             gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
             strokes.clear();
             currentStroke = null;
             
         
        });
         
         //Color Picker for color
         ColorPicker colorPicker = new ColorPicker(currentColor);
         colorPicker.setOnAction(e -> currentColor = colorPicker.getValue());
         
         //Combo Box for Stroke Type
         ComboBox <String> strokeTypeCombo = new ComboBox <>();
         strokeTypeCombo.getItems().addAll("THIN", "THICK", "BROKEN");
         strokeTypeCombo.setValue("THIN");
         strokeTypeCombo.setOnAction(e -> {
             currentStrokeType =strokeTypeCombo.getValue();
             
             switch(currentStrokeType)
             {
                 case "THIN": currentLineWidth = 2; 
                 break;
                 
                 case "THICK": currentLineWidth = 4;
                 break;
                 
                 case "BROKEN": currentLineWidth = 3;
                 break;
             }
         });
         
         
         //Button Sizes
         double buttonEidth = 100;
         clearButtonn.setPrefWidth(buttonEidth);
         loadBasitBtn.setPrefWidth(buttonEidth);
         saveBasitBtn.setPrefWidth(buttonEidth);
         
         HBox buttonBox = new HBox(10,saveJpegBtn, saveBasitBtn, loadBasitBtn, clearButtonn, new Label("Color:"), colorPicker, new Label("Line:"), strokeTypeCombo);
         buttonBox.setAlignment(Pos.CENTER);

        ToolBar toolBar = new ToolBar(buttonBox);
        
        
        root.setTop(buttonBox);
        root.setCenter(canvas);
        Platform.runLater(() -> canvas.requestFocus());

        Scene scene = new Scene(root);
        stage.setTitle("Basmo Paint App");
        stage.setScene(scene);
        stage.show();
        
        
        
    }
    
    private void clearCanvas()
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 800, 600);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
    }
    
    private void redrawCanvas()
    {
        clearCanvas();
        for ( Stroke stroke : strokes )
        {
            List<Double> xPoints = stroke.getXPoints();
            List<Double> yPoints = stroke.getYPoints();
            
            if ( xPoints.size() < 2) continue;
            
            gc.setStroke(Color.web(stroke.getColorHex()));
            gc.setLineWidth(stroke.getLineWidth());
            if ("BROKEN".equals(stroke.getStrokeType())) gc.setLineDashes(10);
            else gc.setLineDashes(null);
            gc.beginPath();
            gc.moveTo(xPoints.get(0), yPoints.get(0));
            
            for (int i = 1; i < xPoints.size(); i++)
            {
                gc.lineTo(xPoints.get(i), yPoints.get(i));
            }
            gc.stroke();
        }
    }
    
   private void saveAsJPEG(Canvas canvas, Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save as JPEG");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Image (.jpg)", ".jpg"));
    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
        // Ensure the file ends with .jpg
        if (!file.getName().toLowerCase().endsWith(".jpg")) {
            file = new File(file.getAbsolutePath() + ".jpg");
        }

        try {
            // Snapshot of the canvas
            WritableImage fxImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, fxImage);

            // Convert to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

            // JPEG does not support transparency -> convert to RGB
            BufferedImage rgbImage = new BufferedImage(
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            
            Graphics2D g2d = rgbImage.createGraphics();
            g2d.setColor(java.awt.Color.WHITE); // White background
            g2d.fillRect(0, 0, rgbImage.getWidth(), rgbImage.getHeight());
            g2d.drawImage(bufferedImage, 0, 0, null);
            g2d.dispose();

            ImageIO.write(rgbImage, "jpg", file);
            System.out.println("✅ Saved JPEG to: " + file.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
       private void saveAsBasit(Stage stage) {
           
           FileChooser fileChooser = new FileChooser();
           fileChooser.setTitle("Save Drawing");
           fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Basmo Files", "*.basit"));
           File file = fileChooser.showSaveDialog(stage);
           
           if(file != null)
           {
               try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
               {
                   out.writeObject(strokes);
               }
               catch(IOException ex)
               {
                   ex.printStackTrace();
               }
           }
       
    }
     private void loadBasit(Stage stage) {
      FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Drawing");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Basmo Files", "*.basit"));
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null)
        {
            if(!file.getName().toLowerCase().endsWith(".basit"))
            {
                System.out.println("Invalid fie selected. Please choose a basit file");
                return;
            }
        

      
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = in.readObject();
                if (obj instanceof List<?>) {
                    strokes = (List<Stroke>) obj;
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Error reading the .basit file. Make sure its a valid saved drawing."
                       );
            }
        }
        
        }
}
