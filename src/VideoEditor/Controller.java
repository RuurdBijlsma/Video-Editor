package VideoEditor;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Controller {
    public TilePane videoPane;
    public TilePane audioPane;
    private Stage stage;

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
        for (File file : selectedFiles) {
            importToGrid(file, videoPane);
        }
    }

    private String getPrettyName(File file) {
        String[] splitName = file.getName().split("\\.");
        return file.getName().substring(0, file.getName().length() - splitName[splitName.length - 1].length() - 1);
    }


    private Image getThumbnail(File video) {
        String path = video.getAbsolutePath();
        String output = "thumbnails/" + getPrettyName(video) + ".png";
        runCommand("lib/ffmpeg/windows/ffmpeg -i \"" + path + "\" -ss 00:00:01.000 -vframes 1 \"" + output + "\" -y", false);
        return new Image("file:" + output);
    }

    private void runCommand(String command, Boolean output) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc;
            proc = rt.exec(command);
            proc.waitFor();

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            if(output){
                System.out.println("Here is the standard output of the command:\n");
                String s;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }

                // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):\n");
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Color randomColor() {
        Random random = new Random();
        final float hue = random.nextFloat() * 360;
        final float saturation = (random.nextInt(4000) + 2000) / 10000f;
        final float luminance = 0.9f;
        return Color.hsb(hue, saturation, luminance);
    }

    private void importToGrid(File image, TilePane pane) {
        pane.getChildren().add(createNode(image));
    }

    private VBox createNode(File file) {
        VBox videoContainer = new VBox();
        Pane thumbnail = new Pane();
        thumbnail.setPrefWidth(200);
        thumbnail.setPrefHeight(200);

        BackgroundFill backgroundFill = new BackgroundFill(randomColor(), new CornerRadii(5),
                new Insets(0, 0, 0, 0));
        thumbnail.setBackground(new Background(backgroundFill));

        new Thread(() -> {
            Image image = getThumbnail(file);
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(-1, -1, false, false, false, true));
            thumbnail.setBackground(new Background(backgroundImage));
        }).start();

        Text text = new Text(getPrettyName(file));
        text.setWrappingWidth(123);
        text.setStyle("-fx-fill: white;");
        StackPane textContainer = new StackPane(text);
        textContainer.maxWidth(123);
        videoContainer.getChildren().addAll(thumbnail, textContainer);
        return videoContainer;
    }
}
