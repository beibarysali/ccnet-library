package com.beibarys.lib;

public class BillType {
    String denomCodeFirstByte;
    String countryCode;
    String denomCodeSecondByte;

    public BillType(String denomCodeFirstByte, String countryCode, String denomCodeSecondByte) {
        this.denomCodeFirstByte = denomCodeFirstByte;
        this.countryCode = countryCode;
        this.denomCodeSecondByte = denomCodeSecondByte;
    }

    public String getDenomCodeFirstByte() {
        return denomCodeFirstByte;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getDenomCodeSecondByte() {
        return denomCodeSecondByte;
    }

    @Override
    public String toString() {
        return "BillType{" +
                "denomCodeFirstByte=" + denomCodeFirstByte +
                ", countryCode='" + countryCode + '\'' +
                ", denomCodeSecondByte=" + denomCodeSecondByte +
                '}';
    }
}
