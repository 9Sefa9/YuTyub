package main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainClass{


        public static void main(String []args) {

        }

}


























/*
public class main.MainClass {

    static WebClient webClient;
    static HtmlPage homepage,trackListPage,downloadPage;
    public static void main(String[] args) {

        try {
            ignoreLogs();
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setAppletEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            homepage = webClient.getPage("https://www.youtubeconverter.io/");

           // homepage.refresh();
            windowTitle(homepage);
            //System.out.println(homepage.asXml());
            HtmlInput form = homepage.getHtmlElementById("ytUrl");
            form.setTextContent("https://www.youtube.com/watch?v=Sbx6FJmf4c0&list=RDC8BC53Jd1Mg&index=12");
            //homepage = webClient.getCurrentWindow().getEnclosedPage();
            //HtmlDivision btn = homepage.getFirstByXPath("/div[@id='convertBtn']");
           // downloadPage = btn.click();
           // webClient.waitForBackgroundJavaScript(2000);
            //homepage = (HtmlPage) homepage.getEnclosingWindow().getTopWindow().getEnclosedPage();
            windowTitle(homepage);
            //downloadMp3FromServer();
          // System.out.println(homepage.asXml());

            //HtmlAnchor mp3Anchor = homepage.getFirstByXPath("//a[@id='dbtn-mp3128']");

          //  downloadPage= mp3Anchor.click();
           // webClient.waitForBackgroundJavaScript(1000);
            //homepage= (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
            //windowTitle(downloadPage);
           // downloadMp3FromServer();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
            /*
            final HtmlTextInput linkForm = homepage.getElementByName("url");
            linkForm.setText("https://www.youtubepp.com/watch?v=-5ZN29cpD0E&list=RDSbx6FJmf4c0&index=4");
            final HtmlButton convertButton = (HtmlButton) homepage.getFirstByXPath("//button[@type='submit']");
            trackListPage = convertButton.click();

            windowTitle(trackListPage);
            System.out.println("Printing Tracks..");
            webClient.waitForBackgroundJavaScript(2000);
            //HtmlDivision trackList = (HtmlDivision) page.getByXPath("//div[@class='");
            List<?> imgs = trackListPage.getByXPath("//img[@class='playlist__thumbnail']");
            for (Object obj : imgs) {
                HtmlImage img = (HtmlImage) obj;
                System.out.println(img.getAltAttribute());
            }
            List<?> anchors = trackListPage.getByXPath("//div[@class='button-group']//a");

            for(Object obj: anchors){
                HtmlAnchor anch = (HtmlAnchor) obj;
                System.out.println(anch.getHrefAttribute());
            }

            HtmlAnchor firstTrack = (HtmlAnchor) anchors.get(0);
            downloadPage = firstTrack.click();
            webClient.waitForBackgroundJavaScript(5000);
            downloadPage= (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            windowTitle(downloadPage);

            HtmlAnchor secondTrack = (HtmlAnchor) anchors.get(0);
            downloadPage = secondTrack.click();
            webClient.waitForBackgroundJavaScript(5000);
            downloadPage= (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            //  System.out.println(downloadPage.asXml());
            // HtmlDivision downloadPercent = downloadPage.getFirstByXPath("//div[@class='percent']");
          //  System.out.println("lol?: "+downloadPercent.getAlignAttribute());
            windowTitle(downloadPage);
          //  System.out.println("download");
           // downloadMp3FromServer();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    public static synchronized void downloadMp3FromServer() throws IOException {
       // webClient.waitForBackgroundJavaScript(10000);
        HtmlAnchor downloadAnchor = downloadPage.getFirstByXPath("//a[@id='dbtn-mp3128']");
        InputStream reader = null;
        OutputStream os = null;

        try {
            reader = downloadAnchor.click().getWebResponse().getContentAsStream();
            os = new FileOutputStream("G:/Users/Progamer/Desktop/test"+".mp3");
            System.out.println("G:/Users/Progamer/Desktop/test"+".mp3");
            byte[] buffer = new byte[8192];
            int read;
            while((read = reader.read(buffer)) != -1){
                os.write(buffer,0,read);
            }
            System.out.println("Download done! Please check your path.");


        }catch (Exception i){
            i.printStackTrace();
        }finally{
            try{
                if(reader != null)
                    reader.close();

                if(os != null)
                    os.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    public static void windowTitle(HtmlPage page) {
        System.out.println("Window Url: " + page.getUrl());
    }
    public static void ignoreLogs() {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

    }
}
*/