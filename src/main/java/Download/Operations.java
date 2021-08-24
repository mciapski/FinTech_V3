package Download;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Operations {
  String getTicker();

  String getDateRange() throws IOException;

  void openConnection(String choiceTicker,String dateRange);

  void getLinkDownloadAndSave(Document document) throws IOException;

  byte[] download(String getLink) throws IOException;

  void save(Element element, byte[] bytes) throws IOException;

  void clean() throws IOException;

  void testReading() throws IOException;

  void readValuesFromFile();

  void showTheMostPopular();

  void convertToObject(String verseToConvert);

  void addToStorageList(Company company);

  void showValues(List<Company> list);
}
