package com.tentlers.mngapp.ui.rooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.tables.rooms.RoomForRoomList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyroomsRecyclerViewAdapter extends RecyclerView.Adapter<MyroomsRecyclerViewAdapter.ViewHolder> {

    private List<RoomForRoomList> mRoomList;

    public MyroomsRecyclerViewAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_house_rooms_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (getItemCount() != 0) {
            RoomForRoomList room = mRoomList.get(position);
            holder.mRoomno.setText(String.valueOf(room.roomNo));

            if (room.isOcupied) {
                holder.isOccupied.setVisibility(View.VISIBLE);
                holder.mTenantName.setText(room.tenantName);
            } else {
                holder.isOccupied.setVisibility(View.INVISIBLE);
                holder.mTenantName.setText(holder.itemView.getContext().getString(R.string.no_tenant_added));
            }

            holder.mRoomName.setText(room.roomName);

        }
    }

    public void setmRoomList(List<RoomForRoomList> roomList) {
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
        public final TextView mRoomno, mRoomName, mTenantName;
        public final ImageView isOccupied;

        public ViewHolder(View rootListitem) {
            super(rootListitem);

            mRoomno = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_no);
            mRoomName = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_name);
            mTenantName = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_tenant);
            isOccupied = (ImageView) rootListitem.findViewById(R.id.house_room_listitem_rooms_tenant_status);

        }
    }
}