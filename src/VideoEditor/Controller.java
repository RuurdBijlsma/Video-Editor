package VideoEditor;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Controller {
    public GridPane videoGrid;
    public GridPane audioGrid;
    private Stage stage;

    void setStage(Stage stage) {
        this.stage = stage;
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 0);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 1);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 2);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 3);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 4);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 5);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 6);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 7);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 8);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 9);
//        importToGrid("Doei - Naffme", videoGrid, Color.DARKGREEN, 10);
    }

    public void importButtonClicked() {
        File startDirectory = new File(System.getProperty("user.home"));
        File[] files = startDirectory.listFiles();
        assert files != null;
        Optional<File> videoDir = Arrays.stream(files).filter(f -> f.toString().toLowerCase().contains("video")).findFirst();
        if (videoDir.isPresent())
            startDirectory = videoDir.get();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select video...");
        fileChooser.setInitialDirectory(startDirectory);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Videos", "*.mp4", "*.avi", "*.webm"),
                new FileChooser.ExtensionFilter("All Files", "*")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
    }

    private void importToGrid(String name, GridPane grid, Color color, int index) {
        int rows = grid.getColumnConstraints().size();
        int columns = grid.getRowConstraints().size();
        int x = index % rows;
        int y = index / rows;
        grid.add(createNode(name, color), x, y);
    }

    private VBox createNode(String name, Color color) {
        VBox videoContainer = new VBox();
        Pane thumbnail = new Pane();
        thumbnail.setPrefWidth(200);
        thumbnail.setPrefHeight(200);
        BackgroundFill backgroundFill = new BackgroundFill(color, new CornerRadii(5),
                new Insets(0, 0, 0, 0));
        thumbnail.setBackground(new Background(backgroundFill));

        Text text = new Text(name);
        text.setStyle("-fx-fill: white;");
        StackPane textContainer = new StackPane(text);
        videoContainer.getChildren().addAll(thumbnail, textContainer);
        return videoContainer;
    }
}
