package com.example.stefao.smsreader.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stefao on 4/30/2018.
 */

public class UserDTO implements Serializable{

    private Long id;

    private String password;

    private String email;

    private float totalBudget;

    private List<UserCategoryDTO> userCategories;

    public UserDTO() {
    }

    public UserDTO(Long id, String password, String email, float totalBudget, List<UserCategoryDTO> userCategories) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.totalBudget = totalBudget;
        this.userCategories = userCategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(float totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<UserCategoryDTO> getUserCategories() {
        return userCategories;
    }

    public void setUserCategories(List<UserCategoryDTO> userCategories) {
        this.userCategories = userCategories;
    }
}

