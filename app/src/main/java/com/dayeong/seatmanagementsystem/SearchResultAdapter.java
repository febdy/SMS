package com.dayeong.seatmanagementsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dayeong on 2016-07-27.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private ArrayList<SearchResultItem> searchResultItemList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storeName, distance;

        public ViewHolder(View v) {
            super(v);
            storeName = (TextView) v.findViewById(R.id.txt_store_name);
            distance = (TextView) v.findViewById(R.id.txt_distance);
        }
    }

    public SearchResultAdapter(Context context, ArrayList<SearchResultItem> mySearchResultItemList) {
        this.context = context;
        searchResultItemList = mySearchResultItemList;
    }

    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String listStoreName = searchResultItemList.get(position).getStoreName();
        int listDistance = searchResultItemList.get(position).getDistance();

        holder.storeName.setText(listStoreName);
        holder.distance.setText(String.valueOf(listDistance));
    }

    @Override
    public int getItemCount() {
        return searchResultItemList.size();
    }

}
