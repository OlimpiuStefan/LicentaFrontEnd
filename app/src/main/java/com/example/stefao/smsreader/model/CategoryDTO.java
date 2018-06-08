package com.example.stefao.smsreader.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stefao on 4/30/2018.
 */

public class CategoryDTO implements Serializable {

    private Long id;

    private String subcategory;

    private List<UserCategoryDTO> userCategories;

    private List<TransactionDTO> transactions;

    public CategoryDTO(){}

    public CategoryDTO(Long id, String subcategory, List<UserCategoryDTO> userCategories, List<TransactionDTO> transactions) {
        this.id = id;
        this.subcategory = subcategory;
        this.userCategories = userCategories;
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public List<UserCategoryDTO> getUserCategories() {
        return userCategories;
    }

    public void setUserCategories(List<UserCategoryDTO> userCategories) {
        this.userCategories = userCategories;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }


}

