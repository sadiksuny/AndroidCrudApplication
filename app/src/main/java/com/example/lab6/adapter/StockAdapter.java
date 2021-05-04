package com.example.lab6.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.R;
import com.example.lab6.Stock;
import com.example.lab6.ui.stocks.StocksViewModel;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    LayoutInflater inflater;
    ArrayList<Stock> stocks;
    ArrayList<Stock> allStocks;
    private OnItemClickListener mListener;
    private View view;
    private Fragment fragment;
    private StocksViewModel stocksViewModel;


    public StockAdapter(ArrayList<Stock>stocks, View root, Fragment fragment, Context ctx){
        this.stocks=stocks;
        this.allStocks = stocks;
        this.inflater=LayoutInflater.from(ctx);
        view= root;
        this.fragment=fragment;
    }
    public void setStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
        this.allStocks = stocks;
        notifyDataSetChanged();
    }
    public void deleteStock(ArrayList<Stock> stocks, int position){
        this.stocks= stocks;
        this.stocks.remove(position);
        //notifyDataSetChanged();
    }
    public void updateStock(ArrayList<Stock> stocks, int position,Stock stock){
        this.stocks=stocks;
        this.stocks.set(position,stock);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    public void displayAllStocks(){
        this.stocks=this.allStocks;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener{

        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public StockAdapter(Context ctx, ArrayList<Stock> stocks){
        this.inflater=LayoutInflater.from(ctx);
        this.stocks=stocks;
        //notifyDataSetChanged();

    }


    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.item_list_layout, parent, false);


        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ViewHolder holder, int position) {
        Stock currStock= stocks.get(position);
        holder.stockTitle.setText(stocks.get(position).getStockName());
        holder.stockPrice.setText(String.valueOf(stocks.get(position).getStockPrice()));
        ImageView edit_Image= holder.editImage;
        edit_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StocksViewModel.setCurrentStock(currStock);
                NavHostFragment.findNavController(fragment).navigate(R.id.action_navigation_dashboard_to_editFragment);

            }
        });



    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stockTitle, stockPrice;
        ImageView editImage, deleteImage;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            stockTitle= itemView.findViewById(R.id.stockTitle);
            stockPrice=itemView.findViewById(R.id.stockPrice);
            editImage=itemView.findViewById(R.id.editImageView);
            deleteImage= itemView.findViewById(R.id.deleteImageView);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position= getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position= getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }



                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    public Filter getFilter() {
        return stockFilter;
    }

    private Filter stockFilter = new Filter() {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Stock> filterResults = new ArrayList<>();

            if(constraint == null || constraint.length() ==0){
                filterResults.addAll(allStocks);
            }
            else{

                String filter = constraint.toString().toLowerCase().trim();
                for(Stock stock : allStocks){
                    if(stock.getStockName().toLowerCase().contains(filter)){
                        filterResults.add(stock);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterResults;

            return results;
        }

        //publish to UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            setStocks((ArrayList<Stock>) results.values);
        }
    };
}
