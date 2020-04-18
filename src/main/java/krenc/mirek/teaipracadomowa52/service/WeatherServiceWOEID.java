package krenc.mirek.teaipracadomowa52.service;

import krenc.mirek.teaipracadomowa52.model.ConsolidatedWeather;
import krenc.mirek.teaipracadomowa52.model.Weather;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//klasa do pobiernai d numeru woeid
//Where On Earth ID. Docs.
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
        {
            "id": 5088151636803584,
            "weather_state_name": "Clear",
            "weather_state_abbr": "c",
            "wind_direction_compass": "NW",
            "created": "2020-04-16T13:22:02.348836Z",
            "applicable_date": "2020-04-17",
            "min_temp": 2.79,
            "max_temp": 13.81,
            "the_temp": 11.809999999999999,
            "wind_speed": 7.7556940661443825,
            "wind_direction": 317.9993901614561,
            "air_pressure": 1016.0,
            "humidity": 40,
            "visibility": 15.60418406506005,
            "predictability": 68
        },
        {
            "id": 5346656121782272,
            "weather_state_name": "Light Cloud",
            "weather_state_abbr": "lc",
            "wind_direction_compass": "NNW",
            "created": "2020-04-16T13:22:04.992897Z",
            "applicable_date": "2020-04-18",
            "min_temp": -0.07499999999999998,
            "max_temp": 11.795,
            "the_temp": 9.895,
            "wind_speed": 3.2851851753545955,
            "wind_direction": 326.34367974365733,
            "air_pressure": 1022.0,
            "humidity": 40,
            "visibility": 15.708885110952039,
            "predictability": 70
        },
        {
            "id": 5890216881553408,
            "weather_state_name": "Light Cloud",
            "weather_state_abbr": "lc",
            "wind_direction_compass": "N",
            "created": "2020-04-16T13:22:08.675516Z",
            "applicable_date": "2020-04-19",
            "min_temp": 0.7200000000000001,
            "max_temp": 12.274999999999999,
            "the_temp": 9.15,
            "wind_speed": 6.664539894270791,
            "wind_direction": 357.2643772112682,
            "air_pressure": 1022.5,
            "humidity": 43,
            "visibility": 14.363305794162093,
            "predictability": 70
        },
        {
            "id": 5787618702786560,
            "weather_state_name": "Light Cloud",
            "weather_state_abbr": "lc",
            "wind_direction_compass": "NNE",
            "created": "2020-04-16T13:22:10.789135Z",
            "applicable_date": "2020-04-20",
            "min_temp": 0.9550000000000001,
            "max_temp": 12.129999999999999,
            "the_temp": 9.895,
            "wind_speed": 6.2053860051459475,
            "wind_direction": 28.0,
            "air_pressure": 1026.5,
            "humidity": 48,
            "visibility": 14.625524437286249,
            "predictability": 70
        },
        {
            "id": 5706705981669376,
            "weather_state_name": "Clear",
            "weather_state_abbr": "c",
            "wind_direction_compass": "ENE",
            "created": "2020-04-16T13:22:13.801774Z",
            "applicable_date": "2020-04-21",
            "min_temp": 1.3699999999999999,
            "max_temp": 14.3,
            "the_temp": 10.71,
            "wind_speed": 4.574252565020282,
            "wind_direction": 58.00000000000001,
            "air_pressure": 1032.0,
            "humidity": 46,
            "visibility": 9.999726596675416,
            "predictability": 68
        }
    ],
    "time": "2020-04-16T16:42:41.990470+02:00",
    "sun_rise": "2020-04-16T05:35:57.708989+02:00",
    "sun_set": "2020-04-16T19:36:31.428864+02:00",
    "timezone_name": "LMT",
    "parent": {
        "title": "Poland",
        "location_type": "Country",
        "woeid": 23424923,
        "latt_long": "51.918919,19.134300"
    },
    "sources": [
        {
            "title": "BBC",
            "slug": "bbc",
            "url": "http://www.bbc.co.uk/weather/",
            "crawl_rate": 360
        },
        {
            "title": "Forecast.io",
            "slug": "forecast-io",
            "url": "http://forecast.io/",
            "crawl_rate": 480
        },
        {
            "title": "HAMweather",
            "slug": "hamweather",
            "url": "http://www.hamweather.com/",
            "crawl_rate": 360
        },
        {
            "title": "Met Office",
            "slug": "met-office",
            "url": "http://www.metoffice.gov.uk/",
            "crawl_rate": 180
        },
        {
            "title": "OpenWeatherMap",
            "slug": "openweathermap",
            "url": "http://openweathermap.org/",
            "crawl_rate": 360
        },
        {
            "title": "World Weather Online",
            "slug": "world-weather-online",
            "url": "http://www.worldweatheronline.com/",
            "crawl_rate": 360
        }
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
