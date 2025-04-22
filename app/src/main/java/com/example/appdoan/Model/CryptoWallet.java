package com.example.appdoan.Model;
public class CryptoWallet {

    // class parameters
    private String cryptoSymbol;
    private String picUrl;
    private Double changePercent;
    private Double propertyAmount;
    private Double cryptoBalance;

    // Constructor

    public CryptoWallet(String cryptoSymbol, String picUrl, Double changePercent, Double propertyAmount, Double cryptoBalance) {
        this.cryptoSymbol = cryptoSymbol;
        this.picUrl = picUrl;
        this.changePercent = changePercent;
        this.propertyAmount = propertyAmount;
        this.cryptoBalance = cryptoBalance;
    }

    // Getter & Setter

    public String getCryptoSymbol() {
        return cryptoSymbol;
    }

    public void setCryptoSymbol(String cryptoSymbol) {
        this.cryptoSymbol = cryptoSymbol;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    public Double getPropertyAmount() {
        return propertyAmount;
    }

    public void setPropertyAmount(Double propertyAmount) {
        this.propertyAmount = propertyAmount;
    }

    public Double getCryptoBalance() {
        return cryptoBalance;
    }

    public void setCryptoBalance(Double cryptoBalance) {
        this.cryptoBalance = cryptoBalance;
    }
}
