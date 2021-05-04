package com.example.lab6.ui.stocks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.DataOperation;
import com.example.lab6.R;
import com.example.lab6.Stock;
import com.example.lab6.adapter.StockAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public class StocksFragment extends Fragment {

    private static final String TAG = "Tag";
    private StocksViewModel stocksViewModel;

    private RecyclerView recyclerView;
    private StockAdapter adapter;
    public static ArrayList<Stock> stockList;
    private Stock stock;
    private FloatingActionButton addButton;
    private FloatingActionButton deleteAllButton;
    private Observable<Stock> observable;
    private String statement_type;
    private AlertDialog dialog;
    private DataOperation dataOperation;
    private NavController navController;

    private TextView stockTitle;
    private TextView stockPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //allow option menu for search
        setHasOptionsMenu(true);
        navController =  NavHostFragment.findNavController(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search stock by Name");

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();


        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                adapter.displayAllStocks();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        stocksViewModel =
                new ViewModelProvider(this).get(StocksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stocks, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);

        recyclerView= root.findViewById(R.id.stockList);

        stockList = new ArrayList<Stock>();
        stock =new Stock("","");
        stockList.add(stock);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter= new StockAdapter(stockList,root, this, getContext());
        recyclerView.setAdapter(adapter);
        addButton= root.findViewById(R.id.addButton);

        stockTitle= root.findViewById(R.id.stockTitle);
        stockPrice= root.findViewById(R.id.stockPrice);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newStockDialog();
                dataOperation= DataOperation.INSERT;

            }
        });


        deleteAllButton= root.findViewById(R.id.deleteAllButton);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataOperation= DataOperation.DELETEALL;
                Log.d(TAG, "deleteAllButton clicked");
                Stock stock = new Stock("","");
                stockList.clear();
                Observable<Stock> observable = Observable.just(stock);
                Observer<Stock> observer = getStockObserver(stock);
                observable.observeOn(Schedulers.io()).subscribe(observer);
                //adapter.notifyDataSetChanged();
            }
        });

        adapter.setOnItemClickListener(new StockAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                dataOperation=DataOperation.DELETE;
                Stock stock;
                stock= stockList.get(position);
                stockList.remove(position);
                Observable<Stock> observable= io.reactivex.Observable.just(stock);
                Observer<Stock> observer= new Observer<Stock>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Stock stock) {
                        StocksViewModel.getStockDB().stockDao().delete(stock);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.observeOn(Schedulers.io()).subscribe(observer);

            }

            @Override
            public void onEditClick(int position) {




            }
        });


        stocksViewModel.getAllStocks().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                StocksFragment.stockList= (ArrayList<Stock>) stocks;
                adapter.setStocks(stockList);

            }


        });
        return root;
    }



    private void newStockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.fragment_add, null);
        EditText newStockName = view.findViewById(R.id.edit_stock_title);
        EditText newStockPrice = view.findViewById(R.id.edit_price);
        builder.setView(view)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            String stock_title;
            String stock_price;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stock_title= newStockName.getText().toString().trim();
                stock_price= newStockPrice.getText().toString().trim();
                Stock stock = new Stock(stock_title,stock_price);
                Observable<Stock> observable = Observable.just(stock);
                Observer<Stock> observer = getStockObserver(stock);
                observable.observeOn(Schedulers.io()).subscribe(observer);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();

    }


    private Observer<Stock> getStockObserver(Stock stock){
        return new Observer<Stock>() {
            private static final String TAG = "";

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Stock stock) {
                if(dataOperation==DataOperation.INSERT){
                    List<Stock> stockSearch= StocksViewModel.getStockDB().stockDao().getStockByName(stock.getStockName());
                    if(stockSearch.size()==0){
                        stocksViewModel.getStockDB().stockDao().insert(stock);
                    }else{
                        Snackbar.make(getView(),"Duplicate stock", Snackbar.LENGTH_SHORT).show();
                    }





                }else if(dataOperation==DataOperation.DELETEALL){
                    stocksViewModel.getStockDB().stockDao().deleteAll();
                }else if(dataOperation==DataOperation.DELETE){
                    stocksViewModel.getStockDB().stockDao().delete(stock);
                }else if(dataOperation==DataOperation.UPDATE){
                    stocksViewModel.getStockDB().stockDao().update(stock);
                    Log.d(TAG, "onNext: Update getting called");
                }

                else{

                }

            }

            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }


            public void onComplete() {
                ///adapter.setStocks(stocks);
                Log.d(TAG, "All items are emitted!");
            }

        };


    }


}