package OperationClasses;

import EntityClasses.Company;
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
import java.util.*;
import java.util.stream.Collectors;

public class PopularCompanies implements Initializable {


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      tableView.setItems(getValuesTheMostPopularCompanies());
    } catch (IOException e) {
      e.printStackTrace();
    }
    // set up the columns in the table
    ranking.setCellValueFactory(new PropertyValueFactory<>("ranking"));
    walor.setCellValueFactory(new PropertyValueFactory<>("walor"));
    ticker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
    rynek.setCellValueFactory(new PropertyValueFactory<>("rynek"));
    cena.setCellValueFactory(new PropertyValueFactory<>("cena"));
    iloscAkcji.setCellValueFactory(new PropertyValueFactory<>("iloscAkcji"));

    //load dummy data
    //tableView.setItems(getValuesTheMostPopularCompanies());


  }

  @FXML
  public TableView<Company> tableView;
  @FXML
  public TableColumn<Company, String> ranking;
  @FXML
  public TableColumn<Company, String> walor;
  @FXML
  public TableColumn<Company, String> ticker;
  @FXML
  public TableColumn<Company, String> rynek;
  @FXML
  public TableColumn<Company, Double> cena;
  @FXML
  public TableColumn<Company, Long> iloscAkcji;
  @FXML
  public TableColumn<Company, Double> rsi;


  public ObservableList<Company> getValuesTheMostPopularCompanies() throws IOException {
    ObservableList<Company> popularCompanies = FXCollections.observableArrayList();
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
    walorsAsString.remove(0);
    if (walorsAsString.isEmpty()) {
      throw new IllegalArgumentException("List of walors is empty");
    }
    List<String> tickerAsString = ticker.eachText();
    tickerAsString.remove(0);
    if (tickerAsString.isEmpty()) {
      throw new IllegalArgumentException("List of tickers is empty");
    }
    List<String> positionAsString = position.eachText();
    positionAsString.remove(0);
    if (positionAsString.isEmpty()) {
      throw new IllegalArgumentException("List of indexes is empty");
    }
    List<String> marketAsString = market.eachText();
    marketAsString.remove(0);
    if (marketAsString.isEmpty()) {
      throw new IllegalArgumentException("List of markets is empty");
    }
    List<Double> prices = getListOfCurrentPricesTheMostPopular(walorsAsString);
    if (prices.isEmpty()) {
      throw new IllegalArgumentException("List of prices is empty");
    }
    List<Long> sharesAmount = getListOfSharesAmountTheMostPopular(walorsAsString);
    if (sharesAmount.isEmpty()) {
      throw new IllegalArgumentException("List of sharesAmount is empty");
    }
    List<Double> rsiValue = new ArrayList<>();


    for (int i = 0; i < walorsAsString.size(); i++) {
      popularCompanies.add(new Company(
          positionAsString.get(i)
          , walorsAsString.get(i)
          , tickerAsString.get(i)
          , marketAsString.get(i)
          , prices.get(i)
          , sharesAmount.get(i)
          , rsiValue.get(i))
      );
    }
    return popularCompanies;


  }

  public List<Double> getListOfCurrentPricesTheMostPopular(List<String> listOfWalor) throws IOException {
    List<Double> listOfCurrentPricesTheMostPopular = new ArrayList<>();
    for (int i = 0; i < listOfWalor.size(); i++) {
      Connection connectionBankierForEach = Jsoup.connect
          ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(listOfWalor.get(i)));
      Document documentFromBankierForEach = connectionBankierForEach.get();

      Element price = documentFromBankierForEach.getElementsByClass("profilLast").first();
      String[] priceInStringAfterSplit = price.text().split(" ");
      Double priceInDouble = Double.valueOf(priceInStringAfterSplit[0].replace(",", "."));
      listOfCurrentPricesTheMostPopular.add(priceInDouble);
    }
    return listOfCurrentPricesTheMostPopular;
  }

  public List<Long> getListOfSharesAmountTheMostPopular(List<String> listOfWalor) {
    List<Long> listOfSharesAmountTheMostPopular = new ArrayList<>();
    for (int i = 0; i < listOfWalor.size(); i++) {
      try {
        Connection connectionBankierForEach1 = Jsoup.connect
            ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(listOfWalor.get(i)));
        Document documentFromBankierForEach1 = connectionBankierForEach1.get();

        Element sharesAmount = documentFromBankierForEach1.getElementById("boxStockRations")
            .select("tr:nth-of-type(3)")
            .select("td:nth-of-type(2)").first();
        String sharesAmountInStringAfterFilter = sharesAmount.text().replaceAll("&nbsp;", "").replaceAll(" ", "").replace("szt.", "");
        Long sharesAmountInInteger = Long.valueOf(sharesAmountInStringAfterFilter);
        listOfSharesAmountTheMostPopular.add(sharesAmountInInteger);
      } catch (IOException io) {
        io.printStackTrace();
      }
    }
    return listOfSharesAmountTheMostPopular;
  }

  public Map<List<String>, List<Double>> getRSIandNamesfromWEB() {
    Map<List<String>, List<Double>> mapOfNamesAndValues = new HashMap<>();
    List rsiList = new ArrayList();
    try {
      Connection connectionBiznesRadar = Jsoup.connect("https://www.biznesradar.pl/analiza-techniczna-profile/akcje_gpw");
      Document documentFromBiznesRadar = connectionBiznesRadar.get();

      Elements rsiNames = documentFromBiznesRadar.getElementsByClass("bvalue");
      Elements rsiValues = documentFromBiznesRadar.select("td:nth-of-type(2)");
      List<String> rsiValuesAsString = rsiNames.eachText();
      List<String> filteredRSInames = new ArrayList<>();
      List<Double> rsiValuesList = rsiValues.eachText().stream().map(Double::valueOf).collect(Collectors.toList());
      List<Double> finalRsiValuesList = new ArrayList<>();


      for (int i = 0; i < rsiValuesAsString.size(); i++) {
        if (rsiValuesAsString.get(i).length() >= 4) {
          String filteredValue = rsiValuesAsString.get(i).substring(3).replace("(", "").replace(")", "").replace(" ", "");
          filteredRSInames.add(filteredValue);
        } else {
          filteredRSInames.add(rsiValuesAsString.get(i));
        }
        mapOfNamesAndValues.put(filteredRSInames, rsiValuesList);
      }
    } catch (IOException io) {
      io.printStackTrace();
    }
    return mapOfNamesAndValues;
  }

  public List<Double> getFilteredRSIList(Map<List<String>, List<Double>> mapOfNamesAndRSIValues, List<String> listOfWalors) {
    List<Double> newRSIList = new ArrayList<>();
    for (Map.Entry<List<String>, List<Double>> entry : mapOfNamesAndRSIValues.entrySet()) {
      for (int i = 0; i < listOfWalors.size(); i++) {
        for (int j = 0; j < entry.getKey().size(); j++) {
          if (listOfWalors.get(i).equalsIgnoreCase(entry.getKey().get(j))) {
            newRSIList.add(entry.getValue().get(j));
          }
        }
      }
    }
    return newRSIList;
  }


}
