package model;

import javafx.collections.FXCollections;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class Model {
    //fileArrayList
    private ObservableList<File> fileArrayList = FXCollections.observableArrayList();

    public Model(ListView<String> downloadList) {
        downloadList.setItems(getFileArrayList());
    }

    public synchronized ObservableList<File> getFileArrayList() {
        return fileArrayList;
    }

    //For window dragging
    private double xOffset = 0, yOffset=0;


    public void SendButtonData() {
        if(!this.model.getFileArrayList().isEmpty()){
            Task<Void> task = new UploadClient(this.model, this);
            this.sendBar.progressProperty().bind(task.progressProperty());
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public void windowDragged(MouseEvent event) {
        Main.getPrimaryStage().setX(event.getScreenX() + xOffset);
        Main.getPrimaryStage().setY(event.getScreenY() + yOffset);
    }

    public void windowPressed(MouseEvent event) {
        xOffset = Main.getPrimaryStage().getX() - event.getScreenX();
        yOffset = Main.getPrimaryStage().getY() - event.getScreenY();
    }

    public void downloadButtonAction(TextField urlField) {
        try {

            if(!urlField.getText().isEmpty()){

            }
            //Observable
            this.uploadList.setItems(model.getFileArrayList());

            //Choose one File
            fileChooser = new FileChooser();
            fileChooser.setTitle("SELECT YOUR FILES!");
            fileList = fileChooser.showOpenMultipleDialog(new Stage());

            model.getFileArrayList().addAll(fileList);

        }catch(NullPointerException e ){
            System.err.println("No fileList was added or selected!");
        }
        catch(Exception e ){
            e.printStackTrace();
        }
    }

    public void minimizeWindow() {
        Main.getPrimaryStage().setIconified(true);
    }
}
