package com.example.easelife.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.easelife.R;
import com.example.easelife.data.tables.TableHouse;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.HomeHelper> {
    final LayoutInflater inflater;
    List<TableHouse> mAllHouse;
    final OnItemClickListener listener;

    /* Interface to handle list item click events. */
    public interface OnItemClickListener {
        void onItemClicked(int position ,View v);
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
        if (getItemCount() != 0) {

            TableHouse tableHouse = mAllHouse.get(position);
            Log.d("tostring", tableHouse.toString());
            holder.houseid.setText(String.valueOf(tableHouse.getHouseId()));
            holder.houseName.setText(tableHouse.houseName);

            if (tableHouse.address != null) {
                holder.houseAddress.setText(tableHouse.address.toString());
            } else holder.houseAddress.setText("Address: Not Available");
            holder.houseDate.setText(tableHouse.getDate().toString());
            holder.totalRooms.setText(String.valueOf(tableHouse.getNoOfRooms()));
            holder.emptyRooms.setText(String.valueOf(tableHouse.emptyrooms));
            holder.onBindListener();
        } else {
            holder.houseName.setText("House Name");
            holder.houseid.setText("23");
            holder.emptyRooms.setText("12");
            holder.totalRooms.setText("20");

        }
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

    public class HomeHelper extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView houseid, houseName, houseAddress, totalRooms, emptyRooms, houseDate;
        private final OnItemClickListener homeOnClicklistener;

        public HomeHelper(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            houseid = itemView.findViewById(R.id.home_listitem_house_no);
            houseAddress = itemView.findViewById(R.id.home_listitem_house_address);
            houseName = itemView.findViewById(R.id.home_listitem_house_name);
            totalRooms = itemView.findViewById(R.id.home_listitem_house_totalrooms);
            emptyRooms = itemView.findViewById(R.id.home_listitem_house_emptyrooms);
            houseDate = itemView.findViewById(R.id.home_listitem_house_date);
            homeOnClicklistener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            homeOnClicklistener.onItemClicked(getLayoutPosition(),v);
        }

        public void onBindListener() {
            this.itemView.setOnClickListener(this);
        }
    }
}
