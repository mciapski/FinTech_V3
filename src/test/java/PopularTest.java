import Download.TheMostPopularCompanies;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PopularTest {

  @Test


  void initialize() {


  }

  @org.junit.jupiter.api.Test
  void getValuesTheMostPopularCompanies() {
    ObservableList<TheMostPopularCompanies> theMostPopularCompanies = FXCollections.observableArrayList();
    try {
      Connection connection = Jsoup.connect("https://www.bankier.pl/gielda/notowania/ranking-popularnosci");
      Document document = connection.get();

      //System.out.println(document.outerHtml());

      Elements walor = document.getElementsByClass("colWalor");
      Elements ticker = document.getElementsByClass("colTicker");
      Elements position = document.getElementsByClass("colPosition");
      Elements market = document.getElementsByClass("colMarket");
      //Elements fluent = document.getElementsByClass("rgb027154060");
      //Elements changeInPercenage = document.getElementsByClass("colZmianaProcentowa change down");

      List<String> walorsAsString = walor.eachText();
      List<String> tickerAsString = ticker.eachText();
      List<String> positionAsString = position.eachText();
      List<String> marketAsString = market.eachText();

      for (int i = 1; i < 10; i++) {
        theMostPopularCompanies.add(new TheMostPopularCompanies(
             positionAsString.get(i)
            , walorsAsString.get(i)
            , tickerAsString.get(i)
            , marketAsString.get(i)
        ))
        ;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    assertEquals(theMostPopularCompanies.get(0).getRanking(), "1");
    assertEquals(theMostPopularCompanies.get(0).getWalor(), "ARTGAMES");
    assertEquals(theMostPopularCompanies.get(0).getTicker(), "ARG");
    assertEquals(theMostPopularCompanies.get(0).getRynek(), "NewConnect");

  }
}