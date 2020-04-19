package krenc.mirek.teaipracadomowa52.controller;

import krenc.mirek.teaipracadomowa52.model.ConsolidatedWeather;
import krenc.mirek.teaipracadomowa52.model.Weather;
import krenc.mirek.teaipracadomowa52.service.CountriesAndCapitolsInEurope;
import krenc.mirek.teaipracadomowa52.service.WeatherServiceWOEID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class WeatherController {

    private List<City> urlsCities;
    private CountriesAndCapitolsInEurope countriesAndCapitolsInEurope;
    private WeatherServiceWOEID weatherServiceWOEID;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Autowired
    public WeatherController(WeatherServiceWOEID weatherServiceWOEID, CountriesAndCapitolsInEurope countriesAndCapitolsInEurope) {
        this.weatherServiceWOEID = weatherServiceWOEID;
        this.countriesAndCapitolsInEurope = countriesAndCapitolsInEurope;
        this.urlsCities = new ArrayList<>();
        for (Map.Entry<String,String> map: countriesAndCapitolsInEurope.getCitiesUrlMap().entrySet())
        {
            //key = url, value= name
            if (map.getValue().toLowerCase().equals("england"))
                continue;
            urlsCities.add(new City(map.getKey(), map.getValue()));
        }

        Collections.sort(urlsCities);

//        urlsCities.add(new City("/523920", "Warsaw"));
//        urlsCities.add(new City("/551801", "Vienna"));
//        urlsCities.add(new City("/638242", "Berlin"));
    }

    @GetMapping
    public String goToStartSession(Model model, HttpSession session)
    {
        model.addAttribute("miasta", urlsCities);
        model.addAttribute("wybrane", new City());
        //session.setAttribute("weatherAttr", new Weather());
        Weather weather = (Weather) session.getAttribute("weatherAttr");
        ConsolidatedWeather consolidatedWeather = (ConsolidatedWeather) session.getAttribute("consolidatedWeatherAttr");
        model.addAttribute("metadata", weather);
        model.addAttribute("pogoda", consolidatedWeather);
        return "index";
    }

    @PostMapping
    public String showWeatherSession(City city, Model model, HttpSession session)
    {
        if (city == null)
        {
            return "error";
        }
        Weather jsonWeatherData = getWeather(city);
        //obrazek https://www.metaweather.com/static/img/weather/png/hr.png
        //zalezna od "weather_state_abbr": "hc"
        RedirectView redirectView = new RedirectView("/", true);
        session.setAttribute("weatherAttr", jsonWeatherData);
        session.setAttribute("consolidatedWeatherAttr", jsonWeatherData.getConsolidatedWeather()[0]);
        return "redirect:/";
    }

    private Weather getWeather(City city) {
        String URL = urlsCities.stream()
                .filter(c -> c.getName().equals(city.getName()))
                .findFirst().get().getUrl();

        Weather jsonWeatherData = weatherServiceWOEID.getWheatherData(URL);
        String minTemp = df2.format(jsonWeatherData.getConsolidatedWeather()[0].getMinTemp());
        jsonWeatherData.getConsolidatedWeather()[0].setMinTemp(Double.parseDouble(minTemp));
        String maxTemp = df2.format(jsonWeatherData.getConsolidatedWeather()[0].getMaxTemp());
        jsonWeatherData.getConsolidatedWeather()[0].setMaxTemp(Double.parseDouble(maxTemp));
        return jsonWeatherData;
    }
}

class City implements Comparable<City>{
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public City() {
    }

    public City(String url, String name) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(City o) {
        return this.getName().compareTo(o.getName());
    }
}
