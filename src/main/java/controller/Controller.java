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

    private Model model;
    private Controller controller;
    private Task<Void> downloadTask;
    private Thread downloadThread;
    private HBox hbox;
    private ProgressBar pbar;
    private boolean canProcess=true;
    @FXML private Pane pane;
    @FXML private Button deleteButton;
    @FXML private Button downloadButton;
    @FXML private ListView<String> downloadList;
    @FXML private TextField urlField;

    @FXML
    public void initialize(){
        model = new Model(downloadList);
        controller = this;
        downloadList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.getDownloadList().setCellFactory(lv ->{
            ListCell<String> cell = new ListCell<>();
            cell.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && canProcess) {
                    System.out.println("Adding " +newItem+" to downloadList");

                    try {
                        downloadTask = new DownloadClient(controller, newItem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    downloadThread = new Thread(downloadTask);
                    hbox = new HBox();
                    pbar = new ProgressBar();
                    pbar.setMinHeight(34);


                    hbox.getChildren().clear();
                    hbox.getChildren().add(pbar);
                    cell.setText(" "+newItem);
                    cell.setGraphic(hbox);

                    pbar.progressProperty().bind(downloadTask.progressProperty());
                    downloadThread.start();

                    if (downloadThread.isAlive()) {
                        pbar.progressProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                cell.setText(" " + (newValue.doubleValue() * 100) + "%/100%" + " name: " + newItem);
                            }
                        });
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                if (downloadTask.isDone()) {
                                    Platform.runLater(() -> {
                                        cell.setText("DONE " +newItem);
                                        cell.setGraphic(null);
                                    });
                                    break;
                                }
                                if (downloadTask.isCancelled()) {
                                    Platform.runLater(() -> {
                                        cell.setText("CANCELED " +newItem);
                                        cell.setGraphic(null);
                                    });
                                    break;
                                }
                            }
                        }
                    }).start();
                    canProcess = false;

                }else{
                    cell.setText("");
                    cell.setGraphic(null);
                }
            });
            cell.emptyProperty().addListener((obss, wasEmpty, isEmpty) -> {
                if (isEmpty) {
                    cell.setGraphic(null);
                } else {
                    cell.setGraphic(hbox);
                }
            });
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return cell ;
        });
    }



        /*this.getDownloadList().setCellFactory(c -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String myObject, boolean b) {
                    super.updateItem(myObject, b);
                    try {

                        if (myObject != null) {
                            System.out.println("Adding " +myObject+" to downloadList");

                            downloadTask = new DownloadClient(controller, myObject);
                            downloadThread = new Thread(downloadTask);
                            hbox = new HBox();
                            pbar = new ProgressBar();
                            pbar.setMinHeight(34);


                            hbox.getChildren().clear();
                            hbox.getChildren().add(pbar);
                            setText(" "+myObject);
                            setGraphic(hbox);

                            pbar.progressProperty().bind(downloadTask.progressProperty());
                            downloadThread.start();
                            if (downloadThread.isAlive()) {

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
                                            setText("DONE " + myObject);
                                            setGraphic(null);
                                        });
                                        break;
                                    }
                                    if (downloadTask.isCancelled()) {
                                        Platform.runLater(() -> {
                                            setText("CANCELED " + myObject);
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
    */
    @FXML
    private void downloadButtonAction() { this.model.downloadButtonProcess(urlField); }
    @FXML
    public void deleteButtonAction(){
       this.model.deleteButtonProcess(this.downloadList);
    }
    @FXML
    public void windowDragged(MouseEvent event){ this.model.windowDraggedProcess(event);}
    @FXML
    public void windowPressed(MouseEvent event){ this.model.windowPressedProcess(event); }

    @FXML
    public void minimizeWindow(){this.model.minimizeWindowProcess(); }
    @FXML
    public void closeProgram(){
        System.exit(0);
    }

    public synchronized ListView<String> getDownloadList() {
        return downloadList;
    }

}