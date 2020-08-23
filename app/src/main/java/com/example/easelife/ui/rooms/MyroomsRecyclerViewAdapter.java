package com.example.easelife.ui.rooms;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easelife.R;
import com.example.easelife.data.tables.TableRooms;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyroomsRecyclerViewAdapter extends RecyclerView.Adapter<MyroomsRecyclerViewAdapter.ViewHolder> {

    private List<TableRooms> mRoomList;
    private final Drawable mYesdrawabel, mNoDrawable;

    public MyroomsRecyclerViewAdapter(Drawable yes, Drawable NoDrawable) {
        mYesdrawabel = yes;
        mNoDrawable = NoDrawable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_house_rooms_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemCount() != 0) {
            TableRooms room = mRoomList.get(position);
            holder.mRoomno.setText(String.valueOf(room.roomNo));
            if (room.tenantsName != null) {
                holder.mTenantName.setText(room.tenantsName);
            }
            if (room.isOcupied) {
                holder.isOccupied.setBackground(mYesdrawabel);
            } else holder.isOccupied.setBackground(mNoDrawable);

            holder.mRoomCreateDate.setText(room.date.toString());
            if (room.roomName != null) {
                holder.mRoomName.setText(room.roomName);
            }else holder.mRoomName.setText("Room"+ room.roomNo);

        }
    }

    public void setmRoomList(List<TableRooms> roomList) {
        mRoomList = roomList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRoomList != null) {
            return mRoomList.size();
        } else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mRoomno, mRoomName, mTenantName, isOccupied, mRoomCreateDate;

        public ViewHolder(View rootListitem) {
            super(rootListitem);

            mRoomno = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_no);
            mRoomName = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_name);
            mTenantName = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_tenant);
            isOccupied = (TextView) rootListitem.findViewById(R.id.house_room_listitem_rooms_tenant_status);
            mRoomCreateDate = (TextView) rootListitem.findViewById(R.id.house_room_listitem_rooms_date);

        }
    }
}