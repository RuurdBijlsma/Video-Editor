package VideoEditor;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Controller {

    private enum WindowState {
        MAXIMIZED, MINIMIZED, REGULAR
    }

    private Stage stage;
    private WindowState windowState = WindowState.REGULAR;
    private Rectangle2D prevSize;

    void setStage(Stage stage) {
        this.stage = stage;
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
}
