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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AllCompanies implements Initializable {
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      tableView.setItems(getValuesAllCompanies());
    } catch (IOException e) {
      e.printStackTrace();
    }
    // set up the columns in the table
    walor.setCellValueFactory(new PropertyValueFactory<>("walor"));
    ticker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
    cena.setCellValueFactory(new PropertyValueFactory<>("cena"));
    iloscAkcji.setCellValueFactory(new PropertyValueFactory<>("iloscAkcji"));
  }
  @FXML
  public TableView<Company> tableView;
  @FXML
  public TableColumn<Company, String> walor;
  @FXML
  public TableColumn<Company, String> ticker;
  @FXML
  public TableColumn<Company, Double> cena;
  @FXML
  public TableColumn<Company, Long> iloscAkcji;

  public ObservableList<Company> getValuesAllCompanies() throws IOException,IllegalArgumentException {
    ObservableList<Company> companies = FXCollections.observableArrayList();
    /**
     * Data from Bankier.pl
     */
    Connection connection = Jsoup.connect("https://www.bankier.pl/gielda/notowania/akcje");
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
    List<Double> prices = getListOfCurrentPrices(walorsAsString);
    if (prices.isEmpty()) {
      throw new IllegalArgumentException("List of prices is empty");
    }
    List<Long> sharesAmount = getListOfSharesAmount(walorsAsString);
    if (sharesAmount.isEmpty()) {
      throw new IllegalArgumentException("List of sharesAmount is empty");
    }
    List<Double> rsiValue= getRSIfromWEB(walorsAsString);


    for (int i = 0; i < walorsAsString.size(); i++) {
      companies.add(new Company(
          Integer.valueOf(positionAsString.get(i))
          , walorsAsString.get(i)
          , tickerAsString.get(i)
          , marketAsString.get(i)
          , prices.get(i)
          , sharesAmount.get(i)
          ,rsiValue.get(i))
      );
    }
    return companies;


  }
  public List<Double> getListOfCurrentPrices(List<String> listOfWalor) throws IOException {
    List<Double> listOfCurrentPricesTheMostPopular = new ArrayList<>();
    for (int i = 0; i < listOfWalor.size(); i++) {
      Connection connectionBankierForEach = Jsoup.connect
          ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(listOfWalor.get(i)));
      Document documentFromBankierForEach = connectionBankierForEach.get();

      Element price = documentFromBankierForEach.getElementsByClass("profilLast").first();
      String[] priceInStringAfterSplit = price.text().split(" ");
      Double priceInDouble = Double.valueOf(priceInStringAfterSplit[0].replace(",","."));
      listOfCurrentPricesTheMostPopular.add(priceInDouble);
    }
    return listOfCurrentPricesTheMostPopular;
  }

  public List<Long> getListOfSharesAmount(List<String> listOfWalor) {
    List<Long> listOfSharesAmountTheMostPopular = new ArrayList<>();
    for (int i = 0; i < listOfWalor.size(); i++) {
      try {
        Connection connectionBankierForEach1 = Jsoup.connect
            ("https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=".concat(listOfWalor.get(i)));
        Document documentFromBankierForEach1 = connectionBankierForEach1.get();

        Element sharesAmount = documentFromBankierForEach1.getElementById("boxStockRations")
            .select("tr:nth-of-type(3)")
            .select("td:nth-of-type(2)").first();
        String sharesAmountInStringAfterFilter = sharesAmount.text().replaceAll("&nbsp;","").replaceAll(" ","").replace("szt.", "");
        Long sharesAmountInInteger = Long.valueOf(sharesAmountInStringAfterFilter);
        listOfSharesAmountTheMostPopular.add(sharesAmountInInteger);
      } catch (IOException io) {
        io.printStackTrace();
      }
    }
    return listOfSharesAmountTheMostPopular;
  }
  public List<Double> getRSIfromWEB(List<String> listOfWalor){
    List<Double> rsiList = new ArrayList<>();
    try {
      Connection connectionBiznesRadar = Jsoup.connect("https://www.biznesradar.pl/analiza-techniczna-profile/akcje_gpw");
      Document documentFromBiznesRadar = connectionBiznesRadar.get();

      Elements rsiValuesInString= documentFromBiznesRadar.getElementsByClass("qTableFull");
    }catch (IOException io){
      io.printStackTrace();
    }
    return rsiList;
  }
}