package OperationClasses;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class theMostPopularCompaniesOperationsTest {

  @Test
  void getListOfCurrentPricesTheMostPopular() throws IOException {
    //get
    Connection connection = Jsoup.connect("https://www.bankier.pl/gielda/notowania/ranking-popularnosci");
    Document document = connection.get();

    Elements walor = document.getElementsByClass("colWalor");
    List<String> walorsAsString = walor.eachText();
    walorsAsString.remove(0);
    List<Double> counter = new ArrayList<>();
    //while
    for (int i = 0; i < walorsAsString.size(); i++) {
      Connection connectionBankierForEach = Jsoup.connect
          ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(walorsAsString.get(i)));
      Document documentFromBankierForEach = connectionBankierForEach.get();

      Element price = documentFromBankierForEach.getElementsByClass("profilLast").first();
      String[] priceInStringAfterSplit = price.text().split(" ");
      Double priceInDouble = Double.valueOf(priceInStringAfterSplit[0].replace(",", "."));
      counter.add(priceInDouble);
      System.out.println(counter.get(i));
    }
    //then
    assertEquals(50, counter.size());
  }

  @Test
  void getListOfSharesAmountTheMostPopular() throws IOException {
    //get
    Connection connection = Jsoup.connect("https://www.bankier.pl/gielda/notowania/ranking-popularnosci");
    Document document = connection.get();

    Elements walor = document.getElementsByClass("colWalor");
    List<String> walorsAsString = walor.eachText();
    walorsAsString.remove(0);
    List<Long> counter = new ArrayList<>();

    //while
    for (int i = 0; i < walorsAsString.size(); i++) {
      Connection connectionBankierForEach = Jsoup.connect
          ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(walorsAsString.get(i)));
      Document documentFromBankierForEach = connectionBankierForEach.get();

      Element sharesAmount = documentFromBankierForEach.getElementById("boxStockRations")
          .select("tr:nth-of-type(3)")
          .select("td:nth-of-type(2)").first();
      String[] sharesAmountInStringAfterFirstSplit = sharesAmount.text().split("&nbsp;");
      String sharesAmountAfterFirstConcat = "";
      for (int j = 0; j < sharesAmountInStringAfterFirstSplit.length; j++) {
        sharesAmountAfterFirstConcat += sharesAmountInStringAfterFirstSplit[j];
      }
      String sharesAmountAfterSecondSplit = sharesAmountAfterFirstConcat.replace(" ", "").replace("szt.", "");
      Long sharesAmountInInteger = Long.valueOf(sharesAmountAfterSecondSplit);
      counter.add(sharesAmountInInteger);
      System.out.println(counter.get(i));
    }
    //then
    assertEquals(50, counter.size());
  }

  @Test
  void RSIcalculation() throws IOException {
    //get
    /* <Dates preparation>*/
    int counter = 0;
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

    /*<Ticker list preparation>*/
    Connection connectionToGetTickers = Jsoup.connect("https://www.bankier.pl/gielda/notowania/ranking-popularnosci");
    Document document = connectionToGetTickers.get();

    Elements ticker = document.getElementsByClass("colTicker");
    List<String> tickerAsString = ticker.eachText();
    tickerAsString.remove(0);
    /*</Ticker list preparation>*/

    /* <open connection and download>*/
    for (int i = 1; i < 2; i++) {
      Connection connectionToDownload = Jsoup.connect("https://stooq.pl/q/d/?s=cdr&c=0&d1=20000802&d2=20211112&o=1111111&o_s=1&o_d=1&o_p=1&o_n=1&o_o=1&o_m=1&o_x=1").ignoreContentType(true).ignoreHttpErrors(true).header("Accept-Encoding", "gzip, deflate")
          .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36")
          .header("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
          .header("Accept-Encoding", "gzip, deflate, br")
          .header("cookie","adblock_d394Nsw9An2fM04s=1; _ga=GA1.2.396930757.1636217256; __gads=ID=e028d61378657039-2212c26b30cb0044:T=1636217256:RT=1636217256:S=ALNI_MYnNwQ5-iTwNlgWvZ767borxyqEzw; uid=plu722rnk7iamljswlma0lh9kb; PHPSESSID=3ogcck4288re1ec2lfdggg21m2; cookie_uu=220110000; cookie_user=%3F0001dllg0000114fsd1300e3%7Ccdr+kgh+4ms+ale+ing+alr+alg+mrc; _gid=GA1.2.1328440632.1641845861; cookie_ta=i~d&t~c&a~lg&z~116&l~0&d~1&ch~0&f~0&lt~58&r~3&o~1; FCCDCF=[null,null,null,[\"CPSncvyPSncvyEsABBPLB9CoAP_AAH_AAB5YHQpD7T7FbSFCyP55fLsAMAhXRkCEAqQAAASABmABQAKQIAQCkkAQFASgBAACAAAgICZBAQAMCAgACUABQABAAAEEAAAABAAIIAAAgAEAAAAIAAACAIAAAAAIAAAAEAAAmwgAAIIACAAABAAAAAAAAAAAAAAAAgdCgPsLsVtIUJI_Gk8uwAgCFdGQIQCoAAAAIAGYAAAApAgBAKQQBAABKAAAAIAACAgJgEBAAgACAABQAFAAEAAAAAAAAAAAAggAACAAQAAAAgAAAIAgAAAAAgAAAAAAACBCAAAggAIAAAAAAAAAAAAAAAAAAACAAA.f-gAAAAAAAA\",\"1~2072.66.70.89.93.108.122.149.2202.162.167.196.2253.241.2299.259.2357.311.317.323.2373.338.358.415.440.449.2506.2526.482.486.494.495.2568.2571.2575.540.574.2677.817.864.981.1051.1095.1097.1127.1201.1205.1211.1276.1301.1365.1415.1449.1451.1570.1577.1651.1716.1765.1870.1878.1889\",\"0D1ADC66-D3DC-4A7E-A73B-7B9C03B796D7\"],null,null,[]]; FCNEC=[[\"AKsRol9dC4lLhnDk6ZwUv18NAOjSh6jD657OasmXpIK2eG8lsVFmjUMVg3gXhxLapv64CsgQ_PvrnJHHoOK47zG5My8nG3YFh4J4cwLtHzkQWt0hqN7lCShSz2M6S2bWkizQogr8K9CRfU5fChHUrQXesJhH6KHfJw==\"],null,[]]; privacy=1641845872")
          .ignoreHttpErrors(true)
          .maxBodySize(0).timeout(8000000);
      Document documentToDownload = connectionToDownload.followRedirects(false).referrer("https://stooq.pl/q/d/?s=cdr").get();
      System.out.println(documentToDownload.outerHtml());
      // Getting links

      Elements links = documentToDownload.select("a[href^=\"q/d/l/\"]");

      Element link = links.first();
      assert link != null;
      String linkURL = link.absUrl("href");
      System.out.println(linkURL);
      //Downloading
      byte[] downloadinAsByte = Jsoup.connect(linkURL).header("Accept-Encoding", "gzip, deflate, br")
          .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
          .ignoreHttpErrors(true)
          .ignoreContentType(true)
          .maxBodySize(0).timeout(8000000)
          .execute().bodyAsBytes();
      //Saving
      String savedFileName = tickerAsString.get(i).concat(".csv");

      FileOutputStream file = new FileOutputStream(savedFileName);
      file.write(downloadinAsByte);
      file.close();

      System.out.println(counter);

    }


  }

  @Test
  public void showRSIfromLink() throws IOException {
    Connection connectionBiznesRadar = Jsoup.connect("https://www.biznesradar.pl/analiza-techniczna-profile/akcje_gpw");
    Document documentFromBiznesRadar = connectionBiznesRadar.get();

    Elements rsiValues = documentFromBiznesRadar.getElementsByClass("bvalue");
    Elements rsiValuesDouble = documentFromBiznesRadar.select("td:nth-of-type(2)");
    List<String> rsiValuesAsDouble = rsiValuesDouble.eachText();
    List<String> rsiValuesAsString = rsiValues.eachText();
    List<String> filteredRSInames = new ArrayList<>();
    List<Double> rsiValuesList = rsiValuesAsDouble.stream().map(Double::valueOf).collect(Collectors.toList());

    for (int i = 0; i < rsiValuesAsString.size(); i++) {
      if (rsiValuesAsString.get(i).length() >= 4) {
        String filteredValue = rsiValuesAsString.get(i).substring(3).replace("(", "").replace(")", "").replace(" ", "");
        filteredRSInames.add(filteredValue);
      } else {
        filteredRSInames.add(rsiValuesAsString.get(i));
      }
      System.out.println(filteredRSInames.get(i));
      System.out.println(rsiValuesAsDouble.get(i));
      System.out.println(rsiValuesList.get(i));
    }
  }


  public Map<List<String>, List<Double>> showRSIfromLinkNC() throws IOException {
    Map<List<String>, List<Double>> mapOfNamesAndValues = new HashMap<>();

    Connection connectionBiznesRadar = Jsoup.connect("https://www.biznesradar.pl/analiza-techniczna-profile/newconnect");
    Document documentFromBiznesRadar = connectionBiznesRadar.get();

    Elements rsiValues = documentFromBiznesRadar.getElementsByClass("bvalue");
    Elements rsiValuesDouble = documentFromBiznesRadar.select("td:nth-of-type(2)");
    List<String> rsiValuesAsDouble = rsiValuesDouble.eachText().stream().map(x -> x.replace("bd.", "0")).collect(Collectors.toList());
    List<String> rsiValuesAsString = rsiValues.eachText();
    List<String> filteredRSInames = new ArrayList<>();
    List<Double> rsiValuesList = rsiValuesAsDouble.stream().map(Double::valueOf).collect(Collectors.toList());

    for (int i = 0; i < rsiValuesAsString.size(); i++) {
      if (rsiValuesAsString.get(i).length() >= 4) {
        String filteredValue = rsiValuesAsString.get(i).substring(3).replace("(", "").replace(")", "").replace(" ", "");
        filteredRSInames.add(filteredValue);
      } else {
        filteredRSInames.add(rsiValuesAsString.get(i));
      }
//      System.out.println(filteredRSInames.get(i));
//      System.out.println(rsiValuesAsDouble.get(i));
//      System.out.println(rsiValuesList.get(i));
    }
    mapOfNamesAndValues.put(filteredRSInames, rsiValuesList);
//    for (Map.Entry<List<String>,List<Double>> entry:mapOfNamesAndValues.entrySet()) {
//      for(int i = 0;i<entry.getValue().size();i++){
//        System.out.println(entry.getKey().get(i)+";"+entry.getValue().get(i));
//      }
//    }
    return mapOfNamesAndValues;
  }

  @Test
  public void filteringTest() throws IOException {
    int sumator = 0;
    Connection connection = Jsoup.connect("https://www.bankier.pl/gielda/notowania/ranking-popularnosci");
    Document document = connection.get();

    //System.out.println(document.outerHtml());
    Elements walor = document.getElementsByClass("colWalor");
    List<String> namesListWalor = walor.eachText();


    List<Double> newRSIList = new ArrayList<>();
    List<String> newRSInames = new ArrayList<>();
    Map<List<String>, List<Double>> mapOfList = showRSIfromLinkNC();

    for (Map.Entry<List<String>, List<Double>> entry : mapOfList.entrySet()) {
      for (int i = 0; i < namesListWalor.size(); i++) {
        for (int j = 0; j < entry.getKey().size(); j++) {
          if (namesListWalor.get(i).equalsIgnoreCase(entry.getKey().get(j))) {
            newRSIList.add(entry.getValue().get(j));
            newRSInames.add(entry.getKey().get(j));
            sumator += 1;
          }
        }
      }
      System.out.println(newRSInames);
      System.out.println(newRSIList);
      System.out.println(newRSIList.size());
      System.out.println(sumator);
    }
  }
}
