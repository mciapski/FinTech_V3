package Download;

import javafx.beans.property.SimpleStringProperty;

public class TheMostPopularCompanies {

  SimpleStringProperty ranking;
  SimpleStringProperty walor;
  SimpleStringProperty ticker;
  SimpleStringProperty rynek;
  SimpleStringProperty cena;
  SimpleStringProperty iloscAkcji;


  public TheMostPopularCompanies(String ranking, String walor, String ticker, String rynek, String cena, String iloscAkcji) {
    this.ranking = new SimpleStringProperty(ranking);
    this.walor =new SimpleStringProperty(walor);
    this.ticker =new SimpleStringProperty(ticker);
    this.rynek =new SimpleStringProperty(rynek);
    this.cena =new SimpleStringProperty(cena);
    this.iloscAkcji =new SimpleStringProperty(iloscAkcji);

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

  public String getCena() {
    return cena.get();
  }

  public SimpleStringProperty cenaProperty() {
    return cena;
  }

  public void setCena(String cena) {
    this.cena.set(cena);
  }

  public String getIloscAkcji() {
    return iloscAkcji.get();
  }

  public SimpleStringProperty iloscAkcjiProperty() {
    return iloscAkcji;
  }

  public void setIloscAkcji(String iloscAkcji) {
    this.iloscAkcji.set(iloscAkcji);
  }
}
