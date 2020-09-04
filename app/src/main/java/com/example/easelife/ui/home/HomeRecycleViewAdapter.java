package com.example.easelife.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easelife.R;
import com.example.easelife.data.tables.queryobjects.HouseForHomeFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.HomeHelper> {
    final LayoutInflater inflater;
    List<HouseForHomeFragment> mAllHouse;
    final OnItemClickListener listener;

    @Override
    public void onBindViewHolder(@NonNull HomeHelper holder, int position) {


        HouseForHomeFragment tableHouse = mAllHouse.get(position);
        holder.houseid.setText(String.valueOf(position + 1));
        holder.houseName.setText(tableHouse.houseName);
        holder.houseDate.setText(tableHouse.date.toString());
        holder.totalRooms.setText(String.valueOf(tableHouse.noOfRooms));
        holder.occupiedRooms.setText(String.valueOf(tableHouse.occupiedRooms));

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

    public void setHouses(List<HouseForHomeFragment> allhouse) {
        mAllHouse = allhouse;
        notifyDataSetChanged();
    }

    public HouseForHomeFragment getHouseAtPosition(int position) {
        return mAllHouse.get(position);
    }

    @Override
    public int getItemCount() {
        if (mAllHouse != null) {
            return mAllHouse.size();
        } else return 0;
    }

    /* Interface to handle list item click events. */
    public interface OnItemClickListener {
        void onItemClicked(int position, View v);

        void onImageCliked(int position, View view);

    }

    public static class HomeHelper extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView houseid, houseName, totalRooms, occupiedRooms, houseDate;
        private final OnItemClickListener homeOnClicklistener;

        public HomeHelper(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            houseid = itemView.findViewById(R.id.home_listitem_house_no);
            houseName = itemView.findViewById(R.id.home_listitem_house_name);
            totalRooms = itemView.findViewById(R.id.home_listitem_house_totalrooms);
            occupiedRooms = itemView.findViewById(R.id.home_listitem_house_occupiedrooms);
            houseDate = itemView.findViewById(R.id.home_listitem_house_date);
            homeOnClicklistener = listener;
            ImageView menuImageview = itemView.findViewById(R.id.house_room_listitem_image_more);
            menuImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {/*for showing pop up menu*/
                    listener.onImageCliked(getLayoutPosition(), v);
                }
            });
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            homeOnClicklistener.onItemClicked(getLayoutPosition(), v);
        }
    }
}
