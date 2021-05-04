package com.example.lab6;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab6.ui.stocks.StocksViewModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EditFragment extends Fragment {
    private Stock stock;
    private NavController navController;
    private EditText eTitle;
    private EditText ePrice;
    private Button saveButton;
    private Context ctx;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_edit, container, false);
        this.stock= StocksViewModel.getCurrentStock();

        eTitle=root.findViewById(R.id.eTitle);
        eTitle.setText(stock.getStockName());

        ePrice=root.findViewById(R.id.ePrice);
        ePrice.setText(stock.getStockPrice());

        saveButton= root.findViewById(R.id.eSaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title= eTitle.getText().toString().trim();
                String price= ePrice.getText().toString().trim();
                stock.setStockName(title);
                stock.setStockPrice(price);

                Observable<Stock> observable = io.reactivex.Observable.just(stock);
                Observer<Stock> observer= new Observer<Stock>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Stock stock) {
                        StocksViewModel.getStockDB().stockDao().update(stock);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.observeOn(Schedulers.io()).subscribe(observer);
                InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(root.getWindowToken(), 0);
                navController.navigate(R.id.action_editFragment_to_navigation_dashboard);

            }
        });

        return root;
    }
}