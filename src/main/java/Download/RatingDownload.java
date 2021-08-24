package Download;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class RatingDownload {
  public static void main(String[] args) throws IOException {


    OperationsImpl operation = new OperationsImpl();
    operation.showTheMostPopular();
    operation.openConnection(operation.getTicker(), operation.getDateRange());
//    //operation.testReading();
    operation.readValuesFromFile();
    operation.showValues(operation.listOfCompanyValues);
    PriceChartWithoutVolume companyChart = new PriceChartWithoutVolume("Company chart", " ",operation.listOfCompanyValues);
    companyChart.showChart(companyChart);

//    /**
//     ------------------------ CONNECTION BLOCK----------------------------------
//     */
//    String cdr = "ing";
//    Connection connection = Jsoup.connect("https://stooq.pl/q/d/?s="+cdr);
//    Document document = connection.get();
//
//    Elements links = document.select("a[href^=\"q/d/l/\"]");
//    if (links.size() > 0) {
//      if (links.size() > 1) {
//        System.out.println("More than one link founded. Downloading first match");
//      }
//      Element link = links.first();
//      String linkURL = link.attr("abs:href");
//      System.out.println(linkURL);
//      /**
//       ------------------------ CONNECTION BLOCK----------------------------------
//       */
//      /**
//       ------------------------ DOWNLOAD BLOCK----------------------------------
//       */
//      byte[] bytes = Jsoup.connect(linkURL).ignoreContentType(true).timeout(600000).execute().bodyAsBytes();
//      /**
//       ------------------------ DOWNLOAD BLOCK----------------------------------
//       */
//      /**
//       ------------------------ SAVE BLOCK----------------------------------
//       */
//      String savedFileName = link.text();
//      if (!savedFileName.endsWith(".csv")) {
//        savedFileName.concat(".").concat("csv");
//      }
//      FileOutputStream fos = new FileOutputStream(savedFileName);
//      fos.write(bytes);
//      fos.close();
//      System.out.println("File has been downloaded");
//      /**
//       ------------------------ SAVE BLOCK----------------------------------
//       */
//      /**
//       ------------------------ READ BLOCK----------------------------------
//       */
//
//      File file = new File("Pobierz dane w pliku csv");
//      BufferedReader reader = new BufferedReader(new FileReader(file));
//      while (reader.readLine() != null) {
//        System.out.println(reader.readLine());
//      }
//      /**
//       ------------------------ READ BLOCK----------------------------------
//       */
  }
}



