package com.tentlers.mngapp.ui.tenants;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.data.tables.tenants.TenantNameId;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyTenantsRecyclerViewAdapter extends RecyclerView.Adapter<MyTenantsRecyclerViewAdapter.ViewHolder> {

    private List<TenantNameHouseRoom> mTenantList;
    final OnTenantClickListener tenantClickListener;
    public HouseViewModal viewModal;

    public MyTenantsRecyclerViewAdapter(OnTenantClickListener listener, HouseViewModal viewmodal) {
        tenantClickListener = listener;
        this.viewModal = viewmodal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tenants_list_item, parent, false);
        return new ViewHolder(view, tenantClickListener);
    }

    public TenantNameHouseRoom getItemAtPosition(int position) {
        return mTenantList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.chossenTenant = mTenantList.get(position);
        holder.mTenantName.setText(holder.chossenTenant.tenantName);
        if (holder.chossenTenant.isRoomAlloted) {/*shows chip with message that no room is allotted*/
            holder.chip.setVisibility(View.GONE);
            holder.mHouseName.setText(holder.chossenTenant.houseName);
            holder.mRoomName.setText(holder.chossenTenant.getRoomName());

        } else {
            holder.chip.setVisibility(View.VISIBLE);
            holder.mHouseName.setText("");
            holder.mRoomName.setText("");
        }
        holder.mUnpaidAmt.setText(String.valueOf(holder.chossenTenant.unpaindAmt));
    }

    @Override
    public int getItemCount() {
        if (mTenantList != null) {
            return mTenantList.size();
        }
        return 0;
    }

    public interface OnTenantClickListener {
        void onTenantClicked(View v, int position);
    }

    public void setTenantList(List<TenantNameHouseRoom> tenantList) {
        mTenantList = tenantList;
        new AsyncRoomName(viewModal, this).execute(tenantList);
    }

    public void setUpdatedtenantList(List<TenantNameHouseRoom> mgotTenantList) {
        mTenantList = mgotTenantList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTenantName, mHouseName, mRoomName, mUnpaidAmt;
        public final View chip;
        public TenantNameHouseRoom chossenTenant;
        final OnTenantClickListener onTenantClickListener;

        public ViewHolder(View itemview, OnTenantClickListener listener) {
            super(itemview);
            onTenantClickListener = listener;
            mTenantName = itemview.findViewById(R.id.tenant_listitem_tenant_name);
            mHouseName = itemview.findViewById(R.id.tenant_listitem_tenant_house_name);
            mRoomName = itemview.findViewById(R.id.tenant_listitem_tenant_room_name);
            mUnpaidAmt = itemview.findViewById(R.id.tenant_listitem_amt);
            chip = itemview.findViewById(R.id.chip_no_room_alloted);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTenantClickListener.onTenantClicked(v, getAdapterPosition());
        }
    }

    public static class AsyncRoomName extends AsyncTask<List<TenantNameHouseRoom>, Void, List<TenantNameHouseRoom>> {
        private final HouseViewModal viewModal;
        private final MyTenantsRecyclerViewAdapter viewAdapter;

        AsyncRoomName(HouseViewModal houseviewModal, MyTenantsRecyclerViewAdapter adapter) {
            viewModal = houseviewModal;
            viewAdapter = adapter;
        }

        @Override
        protected List<TenantNameHouseRoom> doInBackground(List<TenantNameHouseRoom>... lists) {
            List<TenantNameHouseRoom> tenantNameIdList = lists[0];
            for (TenantNameHouseRoom s : tenantNameIdList) {
                if (s.isRoomAlloted) {
                    s.setHosueNameRoomName(viewModal.getHouseNameRoomNameFromRoomId(s.roomId));
                }
            }
            return tenantNameIdList;
        }

        @Override
        protected void onPostExecute(List<TenantNameHouseRoom> tenantNameHouseRooms) {
            super.onPostExecute(tenantNameHouseRooms);
            viewAdapter.setUpdatedtenantList(tenantNameHouseRooms);
        }
    }

}