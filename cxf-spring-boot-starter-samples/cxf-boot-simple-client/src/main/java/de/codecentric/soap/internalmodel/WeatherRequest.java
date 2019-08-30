package de.codecentric.soap.internalmodel;

public class WeatherRequest {
    private String zipcode;
    private int userSeniority;
    private int cash;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getUserSeniority() {
        return userSeniority;
    }

    public void setUserSeniority(int userSeniority) {
        this.userSeniority = userSeniority;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}
