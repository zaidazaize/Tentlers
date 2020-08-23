package com.example.easelife.ui.tenants;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easelife.R;
import com.example.easelife.data.tables.tenants.TenantNameHouseRoom;

import java.util.List;

public class MyTenantsRecyclerViewAdapter extends RecyclerView.Adapter<MyTenantsRecyclerViewAdapter.ViewHolder> {

    private List<TenantNameHouseRoom> mTenantList;

    public MyTenantsRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tenants_list_item, parent, false);
        return new ViewHolder(view);
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

    public void setTenantList(List<TenantNameHouseRoom> tenantList) {
        mTenantList = tenantList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTenantName, mHouseName, mRoomName;
        public TenantNameHouseRoom chossenTenant;

        public ViewHolder(View itemview) {
            super(itemview);
            mTenantName = itemview.findViewById(R.id.tenant_listitem_tenant_name);
            mHouseName = itemview.findViewById(R.id.tenant_listitem_tenant_house_name);
            mRoomName = itemview.findViewById(R.id.tenant_listitem_tenant_room_name);
        }
    }
}