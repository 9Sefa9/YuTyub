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
    private ObservableList<String> fileArrayList = FXCollections.observableArrayList();

    public Model(ListView<String> downloadList) {
        downloadList.setItems(fileArrayList);
    }
    public void downloadButtonProcess(TextField urlField) {
        try {

            if(!urlField.getText().isEmpty()){
                fileArrayList.add(urlField.getText());
            }
            //Observable
          //  this.uploadList.setItems(model.getFileArrayList());

            //Choose one File
            //fileChooser = new FileChooser();
           // fileChooser.setTitle("SELECT YOUR FILES!");
            //fileList = fileChooser.showOpenMultipleDialog(new Stage());

            //model.getFileArrayList().addAll(fileList);

        }catch(Exception e ){
           e.printStackTrace();
        }
    }

    //For window dragging
    private double xOffset = 0, yOffset=0;


    public void sendButtonAction() {
     /*   if(!this.model.getFileArrayList().isEmpty()){
            Task<Void> task = new UploadClient(this.model, this);
            this.sendBar.progressProperty().bind(task.progressProperty());
            Thread thread = new Thread(task);
            thread.start();
        }

      */
    }

    public void windowDraggedProcess(MouseEvent event) {
        Main.getPrimaryStage().setX(event.getScreenX() + xOffset);
        Main.getPrimaryStage().setY(event.getScreenY() + yOffset);
    }

    public void windowPressedProcess(MouseEvent event) {
        xOffset = Main.getPrimaryStage().getX() - event.getScreenX();
        yOffset = Main.getPrimaryStage().getY() - event.getScreenY();
    }


    public void minimizeWindowProcess() {
        Main.getPrimaryStage().setIconified(true);
    }

    public void deleteButtonAction() {
        /*if(!this.model.getFileArrayList().isEmpty()) {
            ObservableList<File> selectedFiles = this.downloadList.getSelectionModel().getSelectedItems();
            System.err.println("DELETE FROM LIST :: " + selectedFiles);
            this.model.getFileArrayList().removeAll(selectedFiles);
         }

         */

    }

    public void deleteButtonProcess() {
    }
}
