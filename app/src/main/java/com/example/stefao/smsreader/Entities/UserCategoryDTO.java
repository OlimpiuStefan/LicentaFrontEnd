package com.example.stefao.smsreader.Entities;

import java.util.List;

/**
 * Created by stefao on 4/30/2018.
 */

public class UserCategoryDTO {

    private Long id;

    private UserDTO userDTO;

    private CategoryDTO categoryDTO;

    private float categoryBudget;

    private List<TransactionDTO> transactions;

    public UserCategoryDTO(){}

    public UserCategoryDTO(Long id, UserDTO userDTO, CategoryDTO categoryDTO, float categoryBudget, List<TransactionDTO> transactions) {
        this.id = id;
        this.userDTO = userDTO;
        this.categoryDTO = categoryDTO;
        this.categoryBudget = categoryBudget;
        this.transactions=transactions;
    }

    public UserCategoryDTO(UserDTO userDTO, CategoryDTO categoryDTO, float categoryBudget, List<TransactionDTO> transactions) {
        this.userDTO = userDTO;
        this.categoryDTO = categoryDTO;
        this.categoryBudget = categoryBudget;
        this.transactions=transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public float getCategoryBudget() {
        return categoryBudget;
    }

    public void setCategoryBudget(float categoryBudget) {
        this.categoryBudget = categoryBudget;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
