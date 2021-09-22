
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Weather {

    //1. С помощью http запроса получить в виде json строки погоду в Санкт-Петербурге на период времени,
    // пересекающийся со следующим занятием (например, выборка погода на следующие 5 дней - подойдет)
    //2. Подобрать источник самостоятельно. Можно использовать api accuweather, порядок следующий:
    // зарегистрироваться, зарегистрировать тестовое приложение для получения api ключа,
    // найдите нужный endpoint и изучите документацию. Бесплатный тарифный план предполагает получение
    // погоды не более чем на 5 дней вперед (этого достаточно для выполнения д/з).


    public static String GetData(String url) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите наименование города: ");
        String city = in.nextLine();

        String jsonStringCity = GetData("http://dataservice.accuweather.com/locations/v1/cities/search?apikey=qRXZrlCFn9lsv2xuLauzeidFDqe5z5bA&q="+ city + "&language=ru");

        Gson gCity = new Gson();
        CityData[] cityRes = gCity.fromJson(jsonStringCity, CityData[].class);

        if (cityRes.length > 0) {
            String jsonString = GetData("http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+ cityRes[0].Key + "?&apikey=qRXZrlCFn9lsv2xuLauzeidFDqe5z5bA&details=true&metric=true&language=ru");

            Gson g = new Gson();
            Result dailyForecasts = g.fromJson(jsonString, Result.class);

            for (int i = 0; i < dailyForecasts.DailyForecasts.size(); i++) {
                System.out.println("В городе " + cityRes[0].LocalizedName + " на дату " + dailyForecasts.DailyForecasts.get(i).Date
                        + " ожидается " + dailyForecasts.DailyForecasts.get(i).Day.LongPhrase
                        + ", минимальная температура " + dailyForecasts.DailyForecasts.get(i).Temperature.Minimum.Value
                        + (char) 186 + "C, максимальная температура "
                        + dailyForecasts.DailyForecasts.get(i).Temperature.Maximum.Value + (char) 186 + "C");
            }
        }
        else System.out.println("Город не найден");
    }
    class Result {
        public List<Forecasts> DailyForecasts;

    }

    class DayInf {
        public String LongPhrase;
    }

    class Forecasts {
        public String Date;
        public Temp Temperature;
        public DayInf Day;
    }
    class Temp{
        public Min Minimum;
        public Max Maximum;
    }

    class Min{
        public double Value;
    }

    class Max{
        public double Value;
    }

    class CityData{
        public String LocalizedName;
        public String Key;

    }

    }


