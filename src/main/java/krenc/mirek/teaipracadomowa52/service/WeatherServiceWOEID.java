package krenc.mirek.teaipracadomowa52.service;

import krenc.mirek.teaipracadomowa52.model.ConsolidatedWeather;
import krenc.mirek.teaipracadomowa52.model.Weather;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//klasa do pobierania numeru woeid  Where On Earth ID. Docs.
/*
https://www.metaweather.com/api/location/search/?query=warsaw
dostaje
    {
        "title": "Warsaw",
        "location_type": "City",
        "woeid": 523920,
        "latt_long": "52.235352,21.009390"
    }

i wtedy
https://www.metaweather.com/api/location/523920
dostaje
{
    "consolidated_weather": [
        {
            "id": 4890521468141568,
            "weather_state_name": "Light Cloud",
            "weather_state_abbr": "lc",
            "wind_direction_compass": "WNW",
            "created": "2020-04-16T13:21:58.674656Z",
            "applicable_date": "2020-04-16",
            "min_temp": 7.574999999999999,
            "max_temp": 16.855,
            "the_temp": 13.805,
            "wind_speed": 12.386167961308624,
            "wind_direction": 284.83110787147086,
            "air_pressure": 1012.0,
            "humidity": 49,
            "visibility": 14.583581881810227,
            "predictability": 70
        },
...
    ],
    "title": "Warsaw",
    "location_type": "City",
    "woeid": 523920,
    "latt_long": "52.235352,21.009390",
    "timezone": "Europe/Warsaw"
}

 */
@Component
public class WeatherServiceWOEID {

    private RestTemplate restTemplate;
    private final String API ="https://www.metaweather.com/api/location";

    public Weather getWheatherData(String url)
    {
        restTemplate = new RestTemplate();
        String URL = API + url;
        System.out.println("URL=" + URL);
        String result = restTemplate.getForObject(URL, String.class);
        Weather weather = restTemplate.getForObject(URL, Weather.class);

        System.out.println("HUMIDITY=" + weather.getConsolidatedWeather()[0].getHumidity());
        System.out.println("MAX TEMP=" + weather.getConsolidatedWeather()[0].getMaxTemp());
        System.out.println("STATE=" + weather.getConsolidatedWeather()[0].getWeatherStateAbbr());

        return weather;
    }
}
