# Basmo Paint App 

A JavaFX desktop drawing application that allows users to create, save, and load digital drawings with customizable stroke styles and colors.

## Features
- Freehand drawing on canvas
- Multiple stroke types (Thin, Thick, Broken)
- Color picker for custom drawing colors
- Save drawings as JPEG images
- Save and load drawings using custom `.basit` file format
- Clear canvas functionality
- Smooth drawing with mouse interaction

## Technologies Used
- Java
- JavaFX (Canvas, Scene, Stage, UI Controls)
- JavaFX GraphicsContext for drawing
- JavaFX FileChooser for file operations
- SwingFXUtils (for image conversion)
- Object-Oriented Programming (Stroke class, serialization)

## File Features
- `.jpg` → Export drawing as an image
- `.basit` → Custom file format to save and reload drawings

## How to Run
- Ensure Java JDK 8 or higher is installed
- Ensure JavaFX is properly configured (for Java 11+)
- Open the project in any Java IDE (NetBeans, IntelliJ, Eclipse)
- Run the `PaintApp.java` file

## Notes
- JPEG format does not support transparency, so drawings are saved with a white background
- `.basit` files store drawing data using Java serialization
