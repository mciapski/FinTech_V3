package EntityClasses;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class Company {
 // Data FX
  SimpleIntegerProperty ranking;
  SimpleStringProperty walor;
  SimpleStringProperty ticker;
  SimpleStringProperty rynek;
  SimpleDoubleProperty cena;
  SimpleLongProperty iloscAkcji;
  SimpleDoubleProperty rsi;
  Button btn_chart;
  // Data to create objects and chart
  private Date date;
  private double opening;
  private double highest;
  private double lowest;
  private double closing;
  private double volume;
  private String dateTitle;
  private String openingTite;
  private String highestTitle;
  private String lowestTitle;
  private String closingTitle;
  private String volumeTitle;



  public Company(Integer ranking, String walor, String ticker, String rynek, Double cena, Long iloscAkcji, Double rsi) {
    this.ranking = new SimpleIntegerProperty(ranking);
    this.walor = new SimpleStringProperty(walor);
    this.ticker = new SimpleStringProperty(ticker);
    this.rynek = new SimpleStringProperty(rynek);
    this.cena = new SimpleDoubleProperty(cena);
    this.iloscAkcji = new SimpleLongProperty(iloscAkcji);
    this.rsi = new SimpleDoubleProperty(rsi);
    this.btn_chart = new Button("Show chart");
    btn_chart.setOnAction(event -> {
      try {
        Charts.RSIcalculation(getTicker());
        Charts.readValuesFromFile(getTicker());
        Parent tableView = FXMLLoader.load(getClass().getResource("/Charts.fxml"));
        Scene tableViewScene = new Scene(tableView);
        //Lines gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

  }
  public Company(Date date, double opening, double highest, double lowest, double closing, double volume) {
    this.date = date;
    this.opening = opening;
    this.highest = highest;
    this.lowest = lowest;
    this.closing = closing;
    this.volume = volume;
  }
  public Company(String dateTitle, String openingTite, String highestTitle, String lowestTitle, String closingTitle, String volumeTitle) {
    this.dateTitle = dateTitle;
    this.openingTite = openingTite;
    this.highestTitle = highestTitle;
    this.lowestTitle = lowestTitle;
    this.closingTitle = closingTitle;
    this.volumeTitle = volumeTitle;
  }

  public void setRanking(int ranking) {
    this.ranking.set(ranking);
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public double getOpening() {
    return opening;
  }

  public void setOpening(double opening) {
    this.opening = opening;
  }

  public double getHighest() {
    return highest;
  }

  public void setHighest(double highest) {
    this.highest = highest;
  }

  public double getLowest() {
    return lowest;
  }

  public void setLowest(double lowest) {
    this.lowest = lowest;
  }

  public double getClosing() {
    return closing;
  }

  public void setClosing(double closing) {
    this.closing = closing;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public String getDateTitle() {
    return dateTitle;
  }

  public void setDateTitle(String dateTitle) {
    this.dateTitle = dateTitle;
  }

  public String getOpeningTite() {
    return openingTite;
  }

  public void setOpeningTite(String openingTite) {
    this.openingTite = openingTite;
  }

  public String getHighestTitle() {
    return highestTitle;
  }

  public void setHighestTitle(String highestTitle) {
    this.highestTitle = highestTitle;
  }

  public String getLowestTitle() {
    return lowestTitle;
  }

  public void setLowestTitle(String lowestTitle) {
    this.lowestTitle = lowestTitle;
  }

  public String getClosingTitle() {
    return closingTitle;
  }

  public void setClosingTitle(String closingTitle) {
    this.closingTitle = closingTitle;
  }

  public String getVolumeTitle() {
    return volumeTitle;
  }

  public void setVolumeTitle(String volumeTitle) {
    this.volumeTitle = volumeTitle;
  }

  public Integer getRanking() {
    return ranking.get();
  }

  public SimpleIntegerProperty rankingProperty() {
    return ranking;
  }

  public void setRanking(Integer ranking) {
    this.ranking.set(ranking);
  }

  public String getWalor() {
    return walor.get();
  }

  public SimpleStringProperty walorProperty() {
    return walor;
  }

  public void setWalor(String walor) {
    this.walor.set(walor);
  }

  public String getTicker() {
    return ticker.get();
  }

  public SimpleStringProperty tickerProperty() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker.set(ticker);
  }

  public String getRynek() {
    return rynek.get();
  }

  public SimpleStringProperty rynekProperty() {
    return rynek;
  }

  public void setRynek(String rynek) {
    this.rynek.set(rynek);
  }

  public Double getCena() {
    return cena.get();
  }

  public SimpleDoubleProperty cenaProperty() {
    return cena;
  }

  public void setCena(double cena) {
    this.cena.set(cena);
  }

  public Long getIloscAkcji() {
    return iloscAkcji.get();
  }

  public SimpleLongProperty iloscAkcjiProperty() {
    return iloscAkcji;
  }

  public void setIloscAkcji(long iloscAkcji) {
    this.iloscAkcji.set(iloscAkcji);
  }

  public double getRsi() {
    return rsi.get();
  }

  public SimpleDoubleProperty rsiProperty() {
    return rsi;
  }

  public void setRsi(double rsi) {
    this.rsi.set(rsi);
  }

  public Button getBtn_chart() {
    return btn_chart;
  }

  public void setBtn_chart(Button btn_chart) {
    this.btn_chart = btn_chart;
  }


}
