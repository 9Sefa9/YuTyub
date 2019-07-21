package model;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import controller.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Model {
    //fileArrayList
    private ObservableList<HBox> fileArrayList = FXCollections.observableArrayList();
    private Task<Void> downloadTask;
    private Text text;
    private String currentYoutubeLink="";
    private String currentYoutubeSongName="";

    public Model(ListView<HBox> downloadList) {
        downloadList.setItems(fileArrayList);
    }
    public void downloadButtonProcess(TextField urlField) {
        synchronized (fileArrayList) {
        try {
                //Wenn der Eingefügt Link nicht leer ist, ein watch? beinhaltet und nicht dem vorgänger entspricht, also bereits in der Liste ist : trage ein in die ListView.
                if (!urlField.getText().isEmpty() && urlField.getText().contains("watch?") && !currentYoutubeLink.equals(urlField.getText())) {

                    currentYoutubeLink = urlField.getText();
                    currentYoutubeSongName = Executors.newSingleThreadExecutor().submit(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            return determineTitle(currentYoutubeLink);
                        }
                    }).get();

                    //currentYoutubeSongName = new Future<String>()  determineTitle(currentYoutubeLink);
                    System.out.println("Adding " + currentYoutubeSongName + " to downloadList");
                    downloadTask = new DownloadClient(currentYoutubeLink, currentYoutubeSongName);
                    Thread downloadThread = new Thread(downloadTask);
                    HBox hbox = new HBox();
                    ProgressBar pbar = new ProgressBar();
                    text = new Text();
                    pbar.setMinHeight(34);
                    hbox.getChildren().clear();
                    hbox.getChildren().add(pbar);
                    text.setText(" " + currentYoutubeSongName);

                    hbox.getChildren().clear();
                    hbox.getChildren().addAll(pbar, text);

                    fileArrayList.add(hbox);

                    pbar.progressProperty().bind(downloadTask.progressProperty());
                    downloadThread.start();

                    if (downloadThread.isAlive()) {
                        pbar.progressProperty().addListener((observable, oldValue, newValue) -> {
                            text.setText(" " + String.format("%.02f", (newValue.doubleValue() * 100)) + "%/100%" + " name: " + currentYoutubeSongName);
                        });
                    }

                    new Thread(() -> {
                        while (true) {
                            if (downloadTask.isDone()) {
                                Platform.runLater(() -> {
                                    text.setText("DONE " + currentYoutubeSongName);
                                });
                                break;
                            }
                            if (downloadTask.isCancelled()) {
                                Platform.runLater(() -> {
                                    text.setText("CANCELED " + currentYoutubeSongName);

                                });
                                break;
                            }
                        }
                    }).start();

                }

        }catch(Exception e ){
            e.printStackTrace();
        }
        }
    }

        public synchronized String determineTitle(String youtubeLink) {
            WebClient webClient2=null;
            HtmlPage amnesty=null;
            try {
                webClient2 = new WebClient(BrowserVersion.CHROME);
                webClient2.getOptions().setThrowExceptionOnScriptError(false);
                webClient2.getOptions().setCssEnabled(false);
                webClient2.getOptions().setUseInsecureSSL(true);
                webClient2.getOptions().setJavaScriptEnabled(true);
                amnesty = webClient2.getPage("https://citizenevidence.amnestyusa.org/");
                final HtmlTextInput inputfield = amnesty.getFirstByXPath("//input[@title='Enter YouTube URL']");
                final HtmlInput input = amnesty.getFirstByXPath("//input[@value='Go']");
                inputfield.setText(youtubeLink);
                amnesty = input.click();
                webClient2.waitForBackgroundJavaScript(2000);
                HtmlAnchor title = amnesty.getAnchorByHref(youtubeLink);
                return title.getTextContent().replaceAll("\\W+", "");

            } catch (Exception e) {
                return "NameNotParsed" + new Random().nextInt(99999);
            } finally {
                try {
                    if (webClient2 != null)
                        webClient2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void deleteButtonProcess(ListView<HBox> downloadList) {
        downloadList.getItems().remove(downloadList.getSelectionModel().getSelectedItem());

    }

    public ObservableList<HBox> getFileArrayList() {
        return fileArrayList;
    }
}
