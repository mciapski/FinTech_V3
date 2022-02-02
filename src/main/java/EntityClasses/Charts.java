package EntityClasses;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Charts implements Initializable {
  public static List<Company> listOfCompanyValues = new ArrayList<>();

  public static void RSIcalculation(String ticker) throws IOException {
    //get
    /* <Dates preparation>*/
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate lastDay = LocalDate.now().minusDays(1);
    String lastDayAfterFormatting = lastDay.format(dateTimeFormatter);

    LocalDate firstDay = LocalDate.of(2020, 1, 1);
    String firstDayAfterFormatting = firstDay.format(dateTimeFormatter);
    String partOfURLcodeWithDate = (
        "d1="
            .concat(firstDayAfterFormatting)
            .concat("&d2=")
            .concat(lastDayAfterFormatting))
        .replaceAll("-", "");

    /* </Dates preparation>*/



    /* <open connection and download>*/

    Connection connectionToDownload = Jsoup.connect("https://stooq.pl/q/d/?s=" + ticker + "&c=0&" + partOfURLcodeWithDate + "&o=1111111&o_s=1&o_d=1&o_p=1&o_n=1&o_o=1&o_m=1&o_x=1")
        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36")
        .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
        .header("cookie", "adblock_d394Nsw9An2fM04s=1; _ga=GA1.2.396930757.1636217256; __gads=ID=e028d61378657039-2212c26b30cb0044:T=1636217256:RT=1636217256:S=ALNI_MYnNwQ5-iTwNlgWvZ767borxyqEzw; uid=plu722rnk7iamljswlma0lh9kb; cookie_ta=i~d&t~c&a~lg&z~450&l~0&d~1&ch~0&f~0&lt~58&r~0&o~1; PHPSESSID=3kinf61sk78puok6m8lfvj3p30; cookie_uu=211114000; _gid=GA1.2.1769044411.1636895432; cookie_user=%3F0001dllg0000115fsd1300e3%7Ccdr+kgh+4ms+ale+ing+alr+alg+mrc; _gat_gtag_UA_64441802_2=1; FCCDCF=[null,null,[\"[[],[],[],[],null,null,true]\",1636896413289],[\"CPPQvA5PPQvA5EsABBPLB0CoAP_AAH_AAB5YHQpD7T7FbSFCyP55fLsAMAhXRkCEAqQAAASABmABQAKQIAQCkkAQFASgBAACAAAgICZBAQIMCAgACUABQABAAAEEAAAABAAIIAAAgAEAAAAIAAACAIAAAAAIAAAAEAAAmwgAAIIACAAABAAAAAAAAAAAAAAAAgdCgPsLsVtIUJI_Gk8uwAgCFdGQIQCoAAAAIAGYAAAApAgBAKQQBAABKAAAAIAACAgJgEBAggACAABQAFAAEAAAAAAAAAAAAggAACAAQAAAAgAAAIAgAAAAAgAAAAAAACBCAAAggAIAAAAAAAAAAAAAAAAAAACAAA.f-gAAAAAAAA\",\"1~2072.66.70.89.93.108.122.149.2202.162.167.196.2253.241.2299.253.259.2357.311.317.323.2373.338.358.415.440.449.2506.2526.482.486.494.495.2568.2571.2575.540.574.2677.817.864.981.1051.1095.1097.1127.1201.1205.1211.1276.1301.1365.1415.1449.1570.1577.1651.1716.1765.1870.1878.1889\",\"E3914F95-DA56-4509-B612-00734E04DF3D\"],null,null,[]]; FCNEC=[[\"AKsRol-sCUlwAylHlorMk_NCkm3koiFxk96rT_b5Uv3NwdGH0RkZTa_EbgCTRW0HBOOcHcfqAuAuJ1baAC_jeoLR_cqApLBYL34Vu0kS6Tmbyj2nM3lTPuU1diwE6sxR7d1uKJYrdHhF0isGK-DV1kyRXgCr49VxEw==\"],null,[]]; privacy=1636896418")
        .ignoreHttpErrors(true);
    Document documentToDownload = connectionToDownload.get();
    // Getting links
    Elements links = documentToDownload.select("a[href^=\"q/d/l/\"]");

    Element link = links.first();
    assert link != null;
    String linkURL = link.absUrl("href");
    System.out.println(linkURL);
    //Downloading
    byte[] downloadinAsByte = Jsoup.connect(linkURL).header("Accept-Encoding", "gzip, deflate")
        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
        .ignoreHttpErrors(true)
        .ignoreContentType(true)
        .maxBodySize(0).timeout(8000000)
        .execute().bodyAsBytes();
    //Saving
    String savedFileName = ticker.concat(".csv");

    FileOutputStream file = new FileOutputStream(savedFileName);
    file.write(downloadinAsByte);
    file.close();

  }

  public static void readValuesFromFile(String ticker) {
    try {
      File file = new File(ticker.concat(".csv"));
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String text = null;
      while ((text = reader.readLine()) != null) {
        convertToObject(text);
      }
    } catch (IOException f) {
      f.printStackTrace();
    }
  }

  public static void convertToObject(String verseToConvert) {
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

  public static void addToStorageList(Company company) {

    listOfCompanyValues.add(company);
  }
  @FXML
  LineChart<String, Double> stock_chart;
DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    XYChart.Series series = new XYChart.Series();
    for (int i = 1; i < listOfCompanyValues.size(); i++) {
      series.getData().add(new XYChart.Data<>(dateFormat.format(listOfCompanyValues.get(i).getDate())
          ,listOfCompanyValues.get(i).getClosing()
          ));
    }
    stock_chart.getData().add(series);

  }

}

