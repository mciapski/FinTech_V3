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
    for (int i = 0; i < 1; i++) {
      Connection connectionToDownload = Jsoup.connect("https://stooq.pl/q/d/?s=" + tickerAsString.get(i) + "&c=0&" + partOfURLcodeWithDate + "&o=1111111&o_s=1&o_d=1&o_p=1&o_n=1&o_o=1&o_m=1&o_x=1");
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

  @Test
  public void filteringTest() {
    int sumator = 0;
    List<String> namesListWalor = List.of("MRC", "CCC", "CDR", "ATS");
    List<String> names = List.of("XXX", "MRC", "ATS", "YYY", "CDR", "CCC");
    List<Double> values = List.of(15.5, 30.0, 40.0, 60.0, 30.0, 65.0);
    List<Double> newRSIList = new ArrayList<>();
    Map<List<String>, List<Double>> mapOfList = new HashMap<>();
    mapOfList.put(names, values);
    for (Map.Entry<List<String>, List<Double>> entry : mapOfList.entrySet()) {
      for (int i = 0; i < namesListWalor.size(); i++) {
        for (int j = 0; j < entry.getKey().size(); j++) {
          if (namesListWalor.get(i).equalsIgnoreCase(entry.getKey().get(j))) {
            newRSIList.add(entry.getValue().get(j));
            sumator += 1;
          }
        }
      }
      System.out.println(newRSIList);
      System.out.println(sumator);
    }
  }
}
