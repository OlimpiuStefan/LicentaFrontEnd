package com.example.stefao.smsreader.model;

/**
 * Created by stefao on 5/24/2018.
 */

public class PoiRatingDTO {

    private Long id;

    private String quality;

    private String price;

    private String accesibility;

    private String amability;

    private PoiDTO poi;

    public PoiRatingDTO(){}

    public PoiRatingDTO(Long id, String quality, String price, String accesibility, String amability, PoiDTO poi) {
        this.id = id;
        this.quality = quality;
        this.price = price;
        this.accesibility = accesibility;
        this.amability = amability;
        this.poi = poi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAccesibility() {
        return accesibility;
    }

    public void setAccesibility(String accesibility) {
        this.accesibility = accesibility;
    }

    public String getAmability() {
        return amability;
    }

    public void setAmability(String amability) {
        this.amability = amability;
    }

    public PoiDTO getPoi() {
        return poi;
    }

    public void setPoi(PoiDTO poi) {
        this.poi = poi;
    }

    @Override
    public String toString() {
        return "PoiRatingDTO{" +
                "quality='" + quality + '\'' +
                ", price='" + price + '\'' +
                ", accesibility='" + accesibility + '\'' +
                ", amability='" + amability + '\'' +
                '}';
    }
}

