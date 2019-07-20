package model;

import controller.Controller;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DownloadClient extends Task<Void> {
    private int currentFileSize;
    private Controller controller;
    private Long progressIndexL;
    private WebDriver driver;
    private WebDriverWait wait;
    private Scanner input;
    private String youtubeURL;
    public DownloadClient(Controller controller, String youtubeURL) throws IOException {
        this.controller= controller;
        this.youtubeURL = youtubeURL;

    }
    @Override
    protected Void call() {
//        String fileName = controller.getDownloadList().getItems().get(controller.getDownloadList().getSelectionModel().getSelectedIndex());

        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/accounts/login/?source=auth_switcher']"))).click();

        switch(System.getProperty("os.name")){
            case "Linux":{
                System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver");
                break;
            }
            case "Windows":{
                System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
                break;
            }
        }

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver,2500);
        driver.get("https://www.youtubeconverter.io/");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Find the yotuubeLink Input
        WebElement urlLink =driver.findElement(By.xpath("//input[@class='url-input']"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Enter something to search for
        urlLink.sendKeys(youtubeURL);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='convertBtn']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='dbtn-mp3128']"))).click();
        String downloadUrl= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='btn btn-success']"))).getAttribute("href");
        String songName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='c-result__video-title']"))).getText();
        songName = songName.replaceAll("\\W+","");

        URL url = null;
        InputStream reader=null;
        FileOutputStream fos=null;

        try {
            url = new URL(downloadUrl);
            reader = url.openStream();
            fos = new FileOutputStream(System.getProperty("user.dir")+"/"+""+songName+".mp3");

            int tmp;
            progressIndexL=0L;
            this.currentFileSize = getFileSize(url);
            byte[] buffer = new byte[currentFileSize];
            while ((tmp = reader.read(buffer)) != -1) {
                fos.write(buffer, 0, tmp);
                updateProgress(progressIndexL,currentFileSize);
            }

            System.out.println("Download done! Please check your path."+ System.getProperty("user.dir")+"/"+""+songName+".mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                //falls file vollständig runtergeladen
                if(progressIndexL == currentFileSize){
                    // lösche es aus der Liste, da beendet. Vielleicht eine bessere beschreibung ?
                    System.out.println("isDone :: ");
                    done();
                }else if (progressIndexL != currentFileSize){
                    cancel();
                }
                if(fos !=null)
                    fos.close();
                if(reader!=null)
                    reader.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private static int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }

}
