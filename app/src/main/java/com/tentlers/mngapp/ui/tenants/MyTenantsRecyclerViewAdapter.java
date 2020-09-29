package com.tentlers.mngapp.ui.tenants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyTenantsRecyclerViewAdapter extends RecyclerView.Adapter<MyTenantsRecyclerViewAdapter.ViewHolder> {

    private List<TenantNameHouseRoom> mTenantList;
    OnTenantClickListener tenantClickListener;

    public MyTenantsRecyclerViewAdapter(OnTenantClickListener listener) {
        tenantClickListener = listener;
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
        holder.mHouseName.setText(holder.chossenTenant.houseName);
        holder.mRoomName.setText(holder.chossenTenant.roomName);
    }

    @Override
    public int getItemCount() {
        if (mTenantList != null) {
            return mTenantList.size();
        }
        return 0;
    }

    public interface OnTenantClickListener {
        public void onTenantClicked(View v, int position);
    }

    public void setTenantList(List<TenantNameHouseRoom> tenantList) {
        mTenantList = tenantList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTenantName, mHouseName, mRoomName;
        public TenantNameHouseRoom chossenTenant;
        OnTenantClickListener onTenantClickListener;

        public ViewHolder(View itemview, OnTenantClickListener listener) {
            super(itemview);
            onTenantClickListener = listener;
            mTenantName = itemview.findViewById(R.id.tenant_listitem_tenant_name);
            mHouseName = itemview.findViewById(R.id.tenant_listitem_tenant_house_name);
            mRoomName = itemview.findViewById(R.id.tenant_listitem_tenant_room_name);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTenantClickListener.onTenantClicked(v, getAdapterPosition());
        }
    }
}