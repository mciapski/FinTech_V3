package Download;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.crypto.Data;
import java.io.*;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OperationsImpl implements Operations {

  List<Company> listOfCompanyValues = new ArrayList<>();
  //String tickerName = getTicker();

  @Override
  public String getTicker() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Please, choose ticker of your company");
    String choiceTicker = scanner.nextLine();
    return choiceTicker;
  }

  @Override
  public String getDateRange() throws IOException {
    try {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Put your date range");
      System.out.println("Choose day fist (yyyy-mm-dd)");
      String day1 = scanner.nextLine();
      LocalDate localDate = LocalDate.parse(day1);
      LocalDate today = LocalDate.now();
      if (localDate.isAfter(today)) {
        throw new IllegalArgumentException("Date has to be at least 'day before today' date");
      } else {
        System.out.println("Choose day second (yyyy-mm-dd)");
        String day2 = scanner.nextLine();
        LocalDate localDate1 = LocalDate.parse(day2);
        if (localDate1.isBefore(localDate) || localDate1.isEqual(localDate)) {
          throw new IllegalArgumentException(" Date 2 has to be after Date 1 and not equal");
        } else {
          String partOfURLwithDate = ("d1=" + day1 + "&d2=" + day2).replaceAll("-", "");
          return partOfURLwithDate;
        }
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    clean();
    throw new IllegalArgumentException("Wrong date value");
  }

  @Override
  public void openConnection(String choiceTicker, String dateRange) {
    try {
      Connection connection = Jsoup.connect("https://stooq.pl/q/d/?s=" + choiceTicker + "&c=0&" + dateRange + "&o=1111111&o_s=1&o_d=1&o_p=1&o_n=1&o_o=1&o_m=1&o_x=1");
      Document document1 = connection.get();
      getLinkDownloadAndSave(document1);
    } catch (IOException | IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void getLinkDownloadAndSave(Document document) throws IOException {
    Elements links = document.select("a[href^=\"q/d/l/\"]");
    if (links.size() > 0) {
      if (links.size() > 1) {
        System.out.println("More than one link founded. Downloading first match");
      }
      Element link = links.first();
      assert link != null;
      String linkURL = link.absUrl("href");
      System.out.println(linkURL);
      save(link, download(linkURL));
    } else {
      clean();
      throw new IllegalArgumentException("Wrong links value");
    }
  }

  @Override
  public byte[] download(String getLink) throws IOException {
    return Jsoup.connect(getLink).header("Accept-Encoding", "gzip, deflate")
        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
        .ignoreHttpErrors(true)
        .ignoreContentType(true)
        .maxBodySize(0).timeout(8000000)
        .execute().bodyAsBytes();
  }

  @Override
  public void save(Element element, byte[] bytes) throws IOException {
    String savedFileName = element.text();
    if (!savedFileName.endsWith(".csv")) {
      savedFileName.concat(".").concat("csv");
    }
    FileOutputStream fos = new FileOutputStream(savedFileName);
    fos.write(bytes);
    fos.close();
    System.out.println("File has been downloaded");
  }

  @Override
  public void clean() throws IOException {

    File file = new File("Pobierz dane w pliku csv");
    BufferedWriter reader = new BufferedWriter(new FileWriter(file));
    reader.write(" ");
    reader.close();
    System.out.println("File has been cleaned");
  }

  @Override
  public void testReading() throws IOException {
    File file = new File("Pobierz dane w pliku csv");
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String text = null;
    while ((text = reader.readLine()) != null) {
      System.out.println(text);
    }
  }

  @Override
  public void readValuesFromFile() {
    try {
      File file = new File("Pobierz dane w pliku csv");
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String text = null;
      while ((text = reader.readLine()) != null) {
        convertToObject(text);
      }
    } catch (IOException f) {
      f.printStackTrace();
    }
  }

  @Override
  public void showTheMostPopular() {
    try {
      Connection connection = Jsoup.connect("https://www.bankier.pl/gielda/notowania/ranking-popularnosci");
      Document document = connection.get();
      //System.out.println(document.outerHtml());

      Elements walor = document.getElementsByClass("colWalor");
      Elements ticker = document.getElementsByClass("colTicker");
      Elements position = document.getElementsByClass("colPosition");

      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < walor.size(); i++) {
        sb.append(position.get(i).text()).append("  ")
            .append(walor.get(i).text()).append("  ").append(ticker.get(i).text()).append("\n");
      }
      System.out.println(sb);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void convertToObject(String verseToConvert) {
    try {
      String[] objects = verseToConvert.split(",");

      if (objects[0].equalsIgnoreCase("Data") && objects[1].equalsIgnoreCase("Otwarcie")
          && objects[2].equalsIgnoreCase("Najwyzszy") && objects[3].equalsIgnoreCase("Najnizszy")
          && objects[4].equalsIgnoreCase("Zamkniecie") && objects[5].equalsIgnoreCase("Wolumen")) {
        Company titles = new Company(objects[0], objects[1], objects[2], objects[3], objects[4], objects[5]);
        addToStorageList(titles);
      } else {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String val = objects[0];
        Date date = format.parse(objects[0]);
        Company company = new Company(date, Double.parseDouble(objects[1]),
            Double.parseDouble(objects[2]),
            Double.parseDouble(objects[3]),
            Double.parseDouble(objects[4]),
            Double.parseDouble(objects[5]));
        addToStorageList(company);
      }
    } catch (ParseException p) {
      p.printStackTrace();
    }
  }

  @Override
  public void addToStorageList(Company company) {

    listOfCompanyValues.add(company);
  }

  @Override
  public void showValues(List<Company> list) {
    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    sb.append(listOfCompanyValues.get(0).getDateTitle())
        .append(" ")
        .append(listOfCompanyValues.get(0).getOpeningTite())
        .append(" ")
        .append(listOfCompanyValues.get(0).getHighestTitle())
        .append(" ")
        .append(listOfCompanyValues.get(0).getLowestTitle())
        .append(" ")
        .append(listOfCompanyValues.get(0).getClosingTitle())
        .append(" ")
        .append(listOfCompanyValues.get(0).getVolumeTitle())
        .append("\n");
    for (int i = 1; i < listOfCompanyValues.size(); i++) {
      sb2.append(listOfCompanyValues.get(i).getDate())
          .append(" ")
          .append(listOfCompanyValues.get(i).getOpening())
          .append(" ")
          .append(listOfCompanyValues.get(i).getHighest())
          .append(" ")
          .append(listOfCompanyValues.get(i).getLowest())
          .append(" ")
          .append(listOfCompanyValues.get(i).getClosing())
          .append(" ")
          .append(listOfCompanyValues.get(i).getVolume())
          .append("\n");
    }
    System.out.println(sb);
    System.out.println(sb2);
  }

}