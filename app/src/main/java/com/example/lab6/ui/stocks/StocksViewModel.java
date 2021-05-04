package com.example.lab6.ui.stocks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab6.Stock;
import com.example.lab6.StockDB;

import java.util.List;

public class StocksViewModel extends AndroidViewModel {

    private LiveData<List<Stock>> allStocks;
    private static StockDB stockDB;
    private static Stock currentStock;

    public static Stock getCurrentStock() {
        return currentStock;
    }

    public static void setCurrentStock(Stock currentStock) {
        StocksViewModel.currentStock = currentStock;
    }

    public StocksViewModel(@NonNull Application application) {
        super(application);
        stockDB= StockDB.getInstance(application);
        allStocks= stockDB.stockDao().getAllStocks();
    }

    public LiveData<List<Stock>> getAllStocks(){
        return allStocks;
    }

    public static StockDB getStockDB(){
        return stockDB;
    }



}