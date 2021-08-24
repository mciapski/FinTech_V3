package Download;

import java.util.Date;

public class Company {

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

  public String getDateTitle() {
    return dateTitle;
  }

  public String getOpeningTite() {
    return openingTite;
  }

  public String getHighestTitle() {
    return highestTitle;
  }

  public String getLowestTitle() {
    return lowestTitle;
  }

  public String getClosingTitle() {
    return closingTitle;
  }

  public String getVolumeTitle() {
    return volumeTitle;
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
}
