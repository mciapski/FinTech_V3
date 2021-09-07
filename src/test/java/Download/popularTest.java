package Download;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class popularTest {

  @Test
  void getListOfCurrentPricesTheMostPopular() throws IOException {
    //get
    List<String> listOfTicers = List.of("JSW");
    //while
    Connection connectionBankierForEach = Jsoup.connect
        ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(listOfTicers.get(0)));
    Document documentFromBankierForEach = connectionBankierForEach.get();

    Element price = documentFromBankierForEach.getElementsByClass("profilLast").first();
    String[] priceInStringAfterSplit = price.text().split(" ");
    Double priceInDouble = Double.valueOf(priceInStringAfterSplit[0].replace(",","."));
    //then
    assertEquals(48.8, priceInDouble);
  }


}
