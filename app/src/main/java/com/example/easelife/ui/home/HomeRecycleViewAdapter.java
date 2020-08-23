package com.example.easelife.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easelife.R;
import com.example.easelife.data.tables.TableHouse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.HomeHelper> {
    final LayoutInflater inflater;
    List<TableHouse> mAllHouse;
    final OnItemClickListener listener;

    /* Interface to handle list item click events. */
    public interface OnItemClickListener {
        void onItemClicked(int position, View v);
    }

    public HomeRecycleViewAdapter(Context context, OnItemClickListener l) {
        inflater = LayoutInflater.from(context);
        listener = l;
    }

    @NonNull
    @Override
    public HomeHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_home_list_item, parent, false);
        return new HomeHelper(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHelper holder, int position) {


        TableHouse tableHouse = mAllHouse.get(position);
        holder.houseid.setText(String.valueOf(position + 1));
        holder.houseName.setText(tableHouse.houseName);

        if (tableHouse.address != null) {
            holder.houseAddress.setText(tableHouse.address.toString());
        } else holder.houseAddress.setText("Address: Not Available");
        holder.houseDate.setText(tableHouse.getDate().toString());
        holder.totalRooms.setText(String.valueOf(tableHouse.getNoOfRooms()));
        holder.occupiedRooms.setText(String.valueOf(tableHouse.emptyrooms));
        holder.onBindListener();

    }

    public void setHouses(List<TableHouse> allhouse) {
        mAllHouse = allhouse;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mAllHouse != null) {
            return mAllHouse.size();
        } else return 0;
    }

    public TableHouse getHouseAtPosition(int position) {
        return mAllHouse.get(position);
    }

    public static class HomeHelper extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView houseid, houseName, houseAddress, totalRooms, occupiedRooms, houseDate;
        private final OnItemClickListener homeOnClicklistener;

        public HomeHelper(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            houseid = itemView.findViewById(R.id.home_listitem_house_no);
            houseAddress = itemView.findViewById(R.id.home_listitem_house_address);
            houseName = itemView.findViewById(R.id.home_listitem_house_name);
            totalRooms = itemView.findViewById(R.id.home_listitem_house_totalrooms);
            occupiedRooms = itemView.findViewById(R.id.home_listitem_house_occupiedrooms);
            houseDate = itemView.findViewById(R.id.home_listitem_house_date);
            homeOnClicklistener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            homeOnClicklistener.onItemClicked(getLayoutPosition(), v);
        }

        public void onBindListener() {
            this.itemView.setOnClickListener(this);
        }
    }
}
