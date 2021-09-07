package Download;

import Download.TheMostPopularCompanies;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class popular implements Initializable {


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    tableView.setItems(getValuesTheMostPopularCompanies());
    // set up the columns in the table
    ranking.setCellValueFactory(new PropertyValueFactory<>("ranking"));
    walor.setCellValueFactory(new PropertyValueFactory<>("walor"));
    ticker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
    rynek.setCellValueFactory(new PropertyValueFactory<>("rynek"));

    //load dummy data
    //tableView.setItems(getValuesTheMostPopularCompanies());


  }

  @FXML
  public TableView<TheMostPopularCompanies> tableView;
  @FXML
  public TableColumn<TheMostPopularCompanies, String> ranking;
  @FXML
  public TableColumn<TheMostPopularCompanies, String> walor;
  @FXML
  public TableColumn<TheMostPopularCompanies, String> ticker;
  @FXML
  public TableColumn<TheMostPopularCompanies, String> rynek;
  @FXML
  public TableColumn<TheMostPopularCompanies, String> cena;
  @FXML
  public TableColumn<TheMostPopularCompanies, String> iloscAkcji;


  public ObservableList<TheMostPopularCompanies> getValuesTheMostPopularCompanies() {
    ObservableList<TheMostPopularCompanies> theMostPopularCompanies = FXCollections.observableArrayList();
    try {
      /**
       * Data from Bankier.pl
       */
      Connection connection = Jsoup.connect("https://www.bankier.pl/gielda/notowania/ranking-popularnosci");
      Document document = connection.get();

      //System.out.println(document.outerHtml());
      Elements position = document.getElementsByClass("colPosition");
      Elements walor = document.getElementsByClass("colWalor");
      Elements ticker = document.getElementsByClass("colTicker");
      Elements market = document.getElementsByClass("colMarket");

      List<String> walorsAsString = walor.eachText();
      List<String> tickerAsString = ticker.eachText();
      List<String> positionAsString = position.eachText();
      List<String> marketAsString = market.eachText();

      getListOfCurrentPricesTheMostPopular(tickerAsString);

//      for (int i = 1; i < walorsAsString.size(); i++) {
//        theMostPopularCompanies.add(new TheMostPopularCompanies(
//            positionAsString.get(i)
//            , walorsAsString.get(i)
//            , tickerAsString.get(i)
//            , marketAsString.get(i)))
//        ;
//      }
      return theMostPopularCompanies;
    } catch (IOException ioException) {
      ioException.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
    throw new IllegalArgumentException("List is null");
  }

  public List<Double> getListOfCurrentPricesTheMostPopular(List<String> listOfTicer) {
    List<Double> listOfCurrentPricesTheMostPopular = new ArrayList<>();
    for (int i = 0; i < listOfTicer.size(); i++) {
      try {
        Connection connectionBankierForEach = Jsoup.connect
            ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(listOfTicer.get(i)));
        Document documentFromBankierForEach = connectionBankierForEach.get();

        Element price = documentFromBankierForEach.getElementsByClass("profilLast").first();
        String[] priceInStringAfterSplit = price.text().split(" ");
        Double priceInDouble = Double.valueOf(priceInStringAfterSplit[0]);
        listOfCurrentPricesTheMostPopular.add(priceInDouble);
      } catch (IOException io) {
        io.printStackTrace();
      }
    }
    return listOfCurrentPricesTheMostPopular;
  }


}
