package com.example.lab6;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StockDao {
    @Insert
     void insert(Stock stock);
     @Update
    void update(Stock stock);

     @Delete
    void delete(Stock stock);

     @Query("DELETE FROM stock")
    void deleteAll();

     @Query("SELECT * FROM stock WHERE stockName LIKE :stockName")
     List<Stock> getStockByName(String stockName);

     @Query("SELECT * FROM stock")
     LiveData<List<Stock>> getAllStocks();


}
