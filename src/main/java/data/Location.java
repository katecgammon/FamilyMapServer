package data;

public class Location {
    String latitude;
    String longitude;
    String city;
    String country;

    public float getLatitude() {
        return Float.parseFloat(latitude);
    }

    public float getLongitude() {
        return Float.parseFloat(longitude);

    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
