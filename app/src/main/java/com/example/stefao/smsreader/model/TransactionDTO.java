package com.example.stefao.smsreader.model;

import java.io.Serializable;

/**
 * Created by stefao on 4/30/2018.
 */

public class TransactionDTO implements Serializable {

    private Long id;

    private double amount;

    private String date;

    private String message;

    private CategoryDTO categoryDTO;

    private PoiDTO poiDTO;

    private UserCategoryDTO userCategoryDTO;

    public TransactionDTO(){}

    public TransactionDTO(Long id, double amount, String date, String message, CategoryDTO categoryDTO, PoiDTO poiDTO, UserCategoryDTO userCategoryDTO) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.message = message;
        this.categoryDTO = categoryDTO;
        this.poiDTO = poiDTO;
        this.userCategoryDTO=userCategoryDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public PoiDTO getPoiDTO() {
        return poiDTO;
    }

    public void setPoiDTO(PoiDTO poiDTO) {
        this.poiDTO = poiDTO;
    }

    public UserCategoryDTO getUserCategoryDTO() {
        return userCategoryDTO;
    }

    public void setUserCategoryDTO(UserCategoryDTO userCategoryDTO) {
        this.userCategoryDTO = userCategoryDTO;
    }
}

