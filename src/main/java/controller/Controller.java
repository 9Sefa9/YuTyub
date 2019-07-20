package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;
import model.DownloadClient;
import model.Model;
import model.UploadClient;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.List;

//TODO: reject button funktioniert irgendwieeeeeee   nicht ganz richtig. es bleibt i.wie hängen. versuche mit 3 gb!
public class Controller implements Serializable {
    private List<File> fileList;
    private FileChooser fileChooser;
    private Model model;

    private Controller controller;
    private Button accept,reject;
    private Task<Void> downloadTask;
    private Thread downloadThread;
    private HBox hbox;
    private ProgressBar pbar;
    @FXML private Pane pane;
    @FXML private ListView<File> uploadList;
    @FXML private Button deleteButton;
    @FXML private Button downloadButton;
    @FXML private ListView<String> downloadList;
    @FXML private TextField urlField;

    @FXML
    public void initialize(){
        model = new Model(downloadList);
        controller = this;
        uploadList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.getDownloadList().setCellFactory(c -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String myObject, boolean b) {
                    super.updateItem(myObject, myObject == null || b);
                    try {
                        String path = DownloadClient.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                        String decodedPath = URLDecoder.decode(path, "UTF-8");

                        if (myObject != null && !new File(decodedPath + myObject).exists()) {
                            System.out.println(myObject);

                            downloadTask = new DownloadClient(controller, myObject);
                            downloadThread = new Thread(downloadTask);
                            hbox = new HBox();
                            pbar = new ProgressBar();
                            pbar.setMinHeight(34);


                            hbox.getChildren().clear();
                            hbox.getChildren().addAll(pbar, reject);
                            setText(" " + myObject);
                            setGraphic(hbox);

                            pbar.progressProperty().bind(downloadTask.progressProperty());
                            downloadThread.start();
                            if (downloadThread.isAlive()) {
                                hbox.getChildren().remove(reject);

                                pbar.progressProperty().addListener(new ChangeListener<Number>() {
                                    @Override
                                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                        setText(" " + (newValue.doubleValue() * 100) + "%/100%" + " name: " + myObject);
                                    }
                                });
                            }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    if (downloadTask.isDone()) {
                                        Platform.runLater(() -> {
                                            setText("Download done:" + myObject);
                                            setGraphic(null);
                                        });
                                        break;
                                    }
                                    if (downloadTask.isCancelled()) {
                                        Platform.runLater(() -> {
                                            setText("Download canceled!:" + myObject);
                                            setGraphic(null);
                                        });
                                        break;
                                    }
                                }
                            }
                        }).start();
                            setText(" " + myObject);
                            setGraphic(hbox);
                    }

                    else{
                        setText("");
                        setGraphic(null);
                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            return cell;
        });
    }
    @FXML
    private void downloadButtonAction() { this.model.downloadButtonAction(urlField); }
    @FXML
    public void deleteDataButton(){
        if(!this.model.getFileArrayList().isEmpty()) {
            ObservableList<File> selectedFiles = this.uploadList.getSelectionModel().getSelectedItems();
            System.err.println("DELETE FROM LIST :: " + selectedFiles);
            this.model.getFileArrayList().removeAll(selectedFiles);
        }
    }
    @FXML
    public void windowDragged(MouseEvent event){ this.model.windowDragged(event);}
    @FXML
    public void windowPressed(MouseEvent event){ this.model.windowPressed(event); }

    @FXML
    public void minimizeWindow(){this.model.minimizeWindow(); }
    @FXML
    public void closeProgram(){
        System.exit(0);
    }

    public synchronized ListView<String> getDownloadList() {
        return downloadList;
    }

}