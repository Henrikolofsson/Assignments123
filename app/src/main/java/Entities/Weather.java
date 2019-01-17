package Entities;

public class Weather {
    private String weatherType;
    private float weatherResult;

    public Weather(String weatherType, float weatherResult){
        this.weatherType = weatherType;
        this.weatherResult = weatherResult;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherResult() {
        return Float.toString(weatherResult);
    }

    public void setWeatherResult(float weatherResult) {
        this.weatherResult = weatherResult;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherType='" + weatherType + '\'' +
                ", weatherResult=" + weatherResult +
                '}';
    }
}
