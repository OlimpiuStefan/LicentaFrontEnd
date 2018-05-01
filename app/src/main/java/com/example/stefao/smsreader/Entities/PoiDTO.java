package com.example.stefao.smsreader.Entities;

import java.util.List;

/**
 * Created by stefao on 4/30/2018.
 */

public class PoiDTO {

    private Long id;

    private Double latitude;

    private Double longitude;

    private String name;

    private String type;

    private List<TransactionDTO> transactions;

    public PoiDTO(){}

    public PoiDTO(Long id, Double latitude, Double longitude, String name, String type, List<TransactionDTO> transactions) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.type = type;
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
