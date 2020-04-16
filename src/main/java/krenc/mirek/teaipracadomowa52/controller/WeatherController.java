package krenc.mirek.teaipracadomowa52.controller;

import krenc.mirek.teaipracadomowa52.service.WeatherServiceWOEID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class WeatherController {

    private List<City> urlsCities;
    private WeatherServiceWOEID weatherServiceWOEID;

    public WeatherController(WeatherServiceWOEID weatherServiceWOEID) {
        this.weatherServiceWOEID = weatherServiceWOEID;
        this.urlsCities = new ArrayList<>();
        urlsCities.add(new City("/523920", "Warsaw"));
        urlsCities.add(new City("/551801", "Vienna"));
        urlsCities.add(new City("/638242", "Berlin"));
    }

    @GetMapping
    public String goToStart(Model model)
    {

        model.addAttribute("miasta", urlsCities);
        model.addAttribute("wybrane", new City());
        return "index";
    }

    @PostMapping
    public String showWeather(City city)
    {
        System.out.println(city.getName());
        String URL = urlsCities.stream()
                .filter(c -> c.getName().equals(city.getName()))
                .findFirst().get().getUrl();

        String json = weatherServiceWOEID.getWheatherData(URL);
        System.out.println(json);

        //obrazek https://www.metaweather.com/static/img/weather/png/hr.png
        //lub ikona https://www.metaweather.com/static/img/weather/png/64/hr.png
        //zalezna od "weather_state_abbr": "hc"
        return "redirect:/";
    }
}

class City {
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
}
