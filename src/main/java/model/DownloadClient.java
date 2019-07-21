package model;

import controller.Controller;
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

public class DownloadClient extends Task<Void> {
    private String currentYoutubeSong;
    private String currentYoutubeLink;
    private int currentFileSize;
    private Long progressIndexL;
    private WebDriver driver;
    private WebDriverWait wait;

    public DownloadClient(String currentYoutubeLink, String currentYoutubeSongName){
        this.currentYoutubeLink = currentYoutubeLink;
        this.currentYoutubeSong = currentYoutubeSongName;

    }
    @Override
    protected Void call() {

        if(System.getProperty("os.name").toLowerCase().contains("win")){
            System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
        }
        else{
            System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver");
        }


        ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 2500);
        driver.get("https://www.youtubeconverter.io/");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Find the yotuubeLink Input
        WebElement urlLink = driver.findElement(By.xpath("//input[@class='url-input']"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Enter something to search for
        urlLink.sendKeys(currentYoutubeLink);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='convertBtn']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='dbtn-mp3128']"))).click();
        String successButton= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='btn btn-success']"))).getAttribute("href");

        URL url;
        InputStream reader=null;
        FileOutputStream fos=null;

        try {
            url = new URL(successButton);
            reader = url.openStream();
            fos = new FileOutputStream(System.getProperty("user.dir")+"/"+""+this.currentYoutubeSong+".mp3");

            int tmp;
            progressIndexL=0L;
            this.currentFileSize = getFileSize(url);

            byte[] buffer = new byte[currentFileSize];
            while ((tmp = reader.read(buffer)) != -1) {
                fos.write(buffer, 0, tmp);
                progressIndexL+=tmp;
                updateProgress(progressIndexL,currentFileSize);
            }

            System.out.println("Download done! Please check your path."+ System.getProperty("user.dir")+"/"+""+currentYoutubeSong+".mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                //falls file vollständig runtergeladen
                if(progressIndexL == currentFileSize){
                    // lösche es aus der Liste, da beendet. Vielleicht eine bessere beschreibung ?
                    done();
                }else {
                    cancel();
                }
                if(fos !=null)
                    fos.close();
                if(reader!=null)
                    reader.close();
                if(driver!=null){
                    driver.quit();
                }
                if(wait!=null){
                    wait=null;
                }

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
