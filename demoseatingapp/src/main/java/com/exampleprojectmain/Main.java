package com.exampleprojectmain;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    // Declare UI components
    private GridPane seatingGrid;
    private TextField nameField;
    private ColorPicker colorPicker;
    private Button addButton;

    private Set<Color> usedColors = new HashSet<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize UI components
        seatingGrid = new GridPane();
        nameField = new TextField();
        colorPicker = new ColorPicker();
        addButton = new Button("Add Student");

        // Set up the seating grid
        setUpSeatingGrid();

        // Set up the form
        VBox form = new VBox(10, new Label("Student Name:"), nameField, new Label("Color:"), colorPicker, addButton);

        // Set up the main layout
        BorderPane root = new BorderPane();
        root.setCenter(seatingGrid);
        root.setBottom(form);

        // Create the scene
        Scene scene = new Scene(root, 600, 400);

        // Set up the stage
        primaryStage.setTitle("JavaFX Seating App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set up the "Add Student" button event handler
        addButton.setOnAction(event -> {
            String studentName = nameField.getText();
            Color studentColor = colorPicker.getValue();

            if (isNameValid(studentName) && isColorAvailable(studentColor)) {
                randomlyChooseSeat(studentName, studentColor);
            } else {
                // Display error message(s)
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);

                if (!isNameValid(studentName)) {
                    alert.setContentText("Please enter a valid student name.");
                } else if (!isColorAvailable(studentColor)) {
                    alert.setContentText("Color has already been chosen.");
                }

                alert.showAndWait();
            }
        });
    }

    private boolean isNameValid(String name) {
        return !name.trim().isEmpty();
    }

    private boolean isColorAvailable(Color color) {
        return !usedColors.contains(color);
    }

    private void randomlyChooseSeat(String studentName, Color studentColor) {
        int numRows = seatingGrid.getRowCount();
        int numCols = seatingGrid.getColumnCount();

        // Create a random number generator
        Random random = new Random();

        // Find an empty seat position
        int row, col;
        do {
            row = random.nextInt(numRows);
            col = random.nextInt(numCols);
        } while (seatingGrid.getChildren().get(row * numCols + col) instanceof Rectangle);

        // Update UI with student name and color
        Rectangle seat = createSeatWithStudent(studentName, studentColor);
        seatingGrid.add(seat, col, row);

        // Mark color as used
        usedColors.add(studentColor);

        // Check if all seats are occupied
        if (usedColors.size() == numRows * numCols) {
            displayCompletionMessage();
            addButton.setDisable(true); // Disable the button when seating is complete
        }
    }

    private Rectangle createSeatWithStudent(String studentName, Color studentColor) {
        Rectangle seat = new Rectangle(50, 50);
        seat.setStroke(Color.BLACK);
        seat.setFill(studentColor);

        // Set a tooltip to display student name
        Tooltip tooltip = new Tooltip(studentName);
        Tooltip.install(seat, tooltip);

        return seat;
    }

    private void setUpSeatingGrid() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                // Create a placeholder for each seat
                seatingGrid.add(new Rectangle(50, 50), col, row);
            }
        }
    }

    private void displayCompletionMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Seating Complete");
        alert.setHeaderText(null);
        alert.setContentText("All seats are occupied. Seating is complete!");
        alert.showAndWait();
    }
}
