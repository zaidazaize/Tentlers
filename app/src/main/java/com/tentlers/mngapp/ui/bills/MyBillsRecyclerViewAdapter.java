package com.tentlers.mngapp.ui.bills;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.tables.bills.BillItemForCard;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyBillsRecyclerViewAdapter extends RecyclerView.Adapter<MyBillsRecyclerViewAdapter.ViewHolder> {

    final int paidDrawable, unpaidDrawable, expandmore, expandless;
    List<BillItemForCard> billsList;

    public MyBillsRecyclerViewAdapter(int paid, int unpaid, int expandmore, int expandless) {
        paidDrawable = paid;
        unpaidDrawable = unpaid;
        this.expandmore = expandmore;
        this.expandless = expandless;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bills_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.chosenBill = billsList.get(position);

        /*set Date*/
        holder.textViewdatetime.setText(holder.chosenBill.createDate.toString());

        /*Set payment status icon and text*/
        if (holder.chosenBill.isBillPaid) {
            holder.imageViewPaymentstatus.setImageDrawable(ContextCompat.getDrawable(holder.mCard.getContext(), paidDrawable));
            holder.paymentStatus.setText(R.string.paid);
        } else {
            holder.imageViewPaymentstatus.setImageDrawable(ContextCompat.getDrawable(holder.mCard.getContext(), unpaidDrawable));
            holder.paymentStatus.setText(R.string.pending);
        }

        /*Set total amount*/
        holder.textViewTotalamt.setText(String.valueOf(holder.chosenBill.totalAmt));

        /*Set amount dec. image view and listener*/
        holder.setAmtDescListener(ContextCompat.getDrawable(holder.mCard.getContext(), expandmore)
                , ContextCompat.getDrawable(holder.mCard.getContext(), expandless));

        /*Set descriptive amount*/
        holder.monthlyCharges.setText(String.valueOf(holder.chosenBill.monthlycharge));
        holder.aditionalCharges.setText(String.valueOf(holder.chosenBill.additionalcharge));
        holder.electricitycharges.setText(String.valueOf(holder.chosenBill.electricCost));

        /*set tenant name */
        holder.tenantName.setText(holder.chosenBill.tenantName);

        /*set house Name*/
        holder.housename.setText(holder.chosenBill.houseName);

        /*set room name*/
        holder.roomname.setText(holder.chosenBill.roomName);

    }

    @Override
    public int getItemCount() {
        if (billsList != null) {
            return billsList.size();
        } else return 0;
    }

    public void setBillsList(List<BillItemForCard> billsList) {
        this.billsList = billsList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mCard, relativeAmtDesc;
        public final ImageView imageViewPaymentstatus, imageViewAmtDesc;
        public final TextView textViewdatetime, paymentStatus, textViewTotalamt, monthlyCharges, aditionalCharges, electricitycharges, tenantName, roomname, housename;
        public BillItemForCard chosenBill;
        boolean expandmorestate;

        public ViewHolder(View view) {
            super(view);
            mCard = view;
            imageViewPaymentstatus = view.findViewById(R.id.bill_imageview_payment_status_icon);
            imageViewAmtDesc = view.findViewById(R.id.bill_imageview_amt_desc);
            relativeAmtDesc = view.findViewById(R.id.bill_card_relative_view_amt_desc);
            textViewdatetime = view.findViewById(R.id.bill_textview_time_and_date);
            paymentStatus = view.findViewById(R.id.bill_textview_payment_status);
            textViewTotalamt = view.findViewById(R.id.bill_total_amt);
            monthlyCharges = view.findViewById(R.id.bill_card_monthly_charges);
            aditionalCharges = view.findViewById(R.id.bill_card_additional_charges);
            electricitycharges = view.findViewById(R.id.bill_card_electricty_charges);
            tenantName = view.findViewById(R.id.bill_card_tenant_name);
            roomname = view.findViewById(R.id.bill_card_room_name);
            housename = view.findViewById(R.id.bill_card_house_name);
        }

        public void setAmtDescListener(final Drawable expandMore, final Drawable expandLess) {
            imageViewAmtDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!expandmorestate) {
                        imageViewAmtDesc.setImageDrawable(expandLess);
                        relativeAmtDesc.setVisibility(View.VISIBLE);
                        expandmorestate = true;
                    } else {
                        imageViewAmtDesc.setImageDrawable(expandMore);
                        relativeAmtDesc.setVisibility(View.GONE);
                        expandmorestate = false;
                    }
                }
            });
        }

    }

}