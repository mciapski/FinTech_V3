package EntityClasses;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Company {

  SimpleStringProperty ranking;
  SimpleStringProperty walor;
  SimpleStringProperty ticker;
  SimpleStringProperty rynek;
  SimpleDoubleProperty cena;
  SimpleLongProperty iloscAkcji;
  SimpleDoubleProperty rsi;


  public Company(String ranking, String walor, String ticker, String rynek, Double cena, Long iloscAkcji, Double rsi) {
    this.ranking = new SimpleStringProperty(ranking);
    this.walor = new SimpleStringProperty(walor);
    this.ticker = new SimpleStringProperty(ticker);
    this.rynek = new SimpleStringProperty(rynek);
    this.cena = new SimpleDoubleProperty(cena);
    this.iloscAkcji = new SimpleLongProperty(iloscAkcji);
    this.rsi = new SimpleDoubleProperty(rsi);

  }

  public String getRanking() {
    return ranking.get();
  }

  public SimpleStringProperty rankingProperty() {
    return ranking;
  }

  public void setRanking(String ranking) {
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
}
