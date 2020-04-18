package krenc.mirek.teaipracadomowa52.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//lista miast dla ktorych jest mozliwosc pobrania aktualnej i progozowanej pogody
@Service
public class CountriesAndCapitolsInEurope {

    //url prowadzacy do europejskich danych
    private final String URL = "https://www.metaweather.com/24865675/";

    private Map<String, String> citiesUrlMap;

    public CountriesAndCapitolsInEurope() throws IOException {
        this.citiesUrlMap = getCapitols(getCountriesAndHrefs());
    }

    public Map<String, String> getCitiesUrlMap() {
        return citiesUrlMap;
    }

    public Map<String, String> getCountriesAndHrefs() throws IOException {
        Document document = Jsoup.connect(URL).get();
        Elements row_locations = document.body().getElementsByClass("row locations");
        Document linksDocument = Jsoup.parse(row_locations.html());
        Map<String, String> collect = linksDocument.select("a").stream()
                .collect(Collectors.toMap(l -> l.attr("href"), l -> l.text()));
        return collect;
    }

    public Map<String, String> getCapitols(Map<String, String> countries) throws IOException {
        String mainAPI = "https://www.metaweather.com";
        Map<String, String> urlCity = new HashMap<>();
        for (Map.Entry map: countries.entrySet())
        {
            //tworze URL
            String URL = mainAPI + map.getKey();
            //wchodze na URL i pobieram miasto oraz jego id
            Document document = Jsoup.connect(URL).get();
            Elements citiLocation = document.body().getElementsByClass("col-lg-4 col-md-4 col-sm-4");
            Document citiDocument = Jsoup.parse(citiLocation.html());
            String link = citiDocument.select("a").attr("href");
            String[] city = citiDocument.select("a").text().split("\\s");
            urlCity.put(link, city[0]);
            //System.out.println(link + " -> " + city[0]);
        }
        return urlCity;
    }
}

