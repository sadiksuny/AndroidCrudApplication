package com.example.lab6;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "stock")
public class Stock {
    private String stockName;



    private String stockPrice;


    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    //private int priority;

    public Stock(String stockName, String stockPrice) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    /**
    public int getPriority() {
        return priority;
    }
     **/

    public int getId() {
        return id;
    }


    @PrimaryKey(autoGenerate = true)
    private int id;



}
