package com.tentlers.mngapp.ui.rooms;

import android.util.Log;
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

    private final OnRoomItemClickedListener listener;

    private List<RoomForRoomList> mRoomList;

    public MyroomsRecyclerViewAdapter(OnRoomItemClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_house_rooms_list_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

            RoomForRoomList room = mRoomList.get(position);
            holder.mRoomno.setText(String.valueOf(room.roomNo));

        if (room.ocupiedStatus) {
            holder.isOccupied.setVisibility(View.VISIBLE);
            holder.mTenantName.setText(room.tenantName);
            Log.d("tenantName", room.tenantName);
        } else {
            holder.isOccupied.setVisibility(View.INVISIBLE);
            holder.mTenantName.setText(holder.itemView.getContext().getString(R.string.no_tenant_added));
        }
        holder.setPopupMenuImageListener(room.ocupiedStatus);
        holder.mRoomName.setText(room.roomName);

    }

    public RoomForRoomList getRoomAtPosition(int position) {
        return mRoomList.get(position);
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

    public interface OnRoomItemClickedListener {
        void onPopUpMenuImageClickListener(View v, int position, boolean isOcupied);

        void onRoomItemClickListener(View v, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mRoomno, mRoomName, mTenantName;
        public final ImageView isOccupied, popupMenuImage;
        private final OnRoomItemClickedListener roomItemClickedListener;

        public ViewHolder(View rootListitem, final OnRoomItemClickedListener listener1) {
            super(rootListitem);

            roomItemClickedListener = listener1;

            mRoomno = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_no);
            mRoomName = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_name);
            mTenantName = (TextView) rootListitem.findViewById(R.id.house_room_listitem_room_tenant);
            isOccupied = (ImageView) rootListitem.findViewById(R.id.house_room_listitem_rooms_tenant_status);
            popupMenuImage = (ImageView) rootListitem.findViewById(R.id.house_room_istitem_image_popup_menu);

            rootListitem.setOnClickListener(this);/*handles to show the specific room fragment*/

        }

        /*handles the showing of the popup menu*/
        public void setPopupMenuImageListener(final boolean occupiedStatus) {
            popupMenuImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    roomItemClickedListener.onPopUpMenuImageClickListener(v, getLayoutPosition(), occupiedStatus);
                }
            });
        }

        @Override
        public void onClick(View v) {
            roomItemClickedListener.onRoomItemClickListener(v, getLayoutPosition());
        }
    }
}