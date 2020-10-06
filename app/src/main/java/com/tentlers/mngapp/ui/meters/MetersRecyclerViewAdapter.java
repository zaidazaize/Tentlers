package com.tentlers.mngapp.ui.meters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MetersRecyclerViewAdapter extends RecyclerView.Adapter<MetersRecyclerViewAdapter.ViewHolder> {

    private List<AllMetersData> allMetersList;
    private int currentReadingState;

    public MetersRecyclerViewAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meter_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setChosenReading(allMetersList.get(position));
        if (currentReadingState != holder.chosenReading.getReadingState()) {
            holder.meterReadingState.setVisibility(View.VISIBLE);

            /*set the text on heading that defferentiates b/w the reading states*/
            holder.meterReadingState.setText(holder.itemView.getContext().getString(
                    holder.chosenReading.getReadingState() == AllMetersData.BILLED ?
                            AllMetersData.BILLING_STATUS : holder.chosenReading.getHeadingText()));
            currentReadingState = holder.chosenReading.getReadingState();
        } else holder.headingViewer.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        if (allMetersList == null) {
            return 0;
        } else return allMetersList.size();
    }

    public AllMetersData getReadingAtPosition(int position) {
        return allMetersList.get(position);
    }

    public void setAllMetersList(List<AllMetersData> allMetersList) {
        this.allMetersList = allMetersList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View colorChanger, headingViewer;
        public final TextView billID, date, reading, showBillID, meterReadingState;
        LinearLayout orientationChanger;
        private AllMetersData chosenReading;

        public ViewHolder(View view) {
            super(view);
            headingViewer = view.findViewById(R.id.meter_reading_heading_desc);
            colorChanger = (MaterialCardView) view.findViewById(R.id.meter_relativelayout_for_color);
            billID = view.findViewById(R.id.meter_bill_id);
            date = view.findViewById(R.id.meter_date);
            reading = view.findViewById(R.id.meter_reading);
            showBillID = view.findViewById(R.id.meter_show_bill_id);
            meterReadingState = view.findViewById(R.id.meter_reading_state);
            orientationChanger = view.findViewById(R.id.meter_gravity_decider);


        }

        public AllMetersData getChosenReading() {
            return chosenReading;
        }

        public void setChosenReading(AllMetersData chosenReading) {
            this.chosenReading = chosenReading;

            /*set the color on the card for defining lhe bills sate*/

            /*set the orientation for defining the state of the meter reading.*/
            this.orientationChanger.setGravity(chosenReading.getOrientationState());

            /*set the text style and text of the show bill id textview.*/
            this.showBillID.setTextAppearance(chosenReading.getTextStyle());

            /*set the text on reading source description*/
            this.showBillID.setText(itemView.getContext().getString(chosenReading.getHeadingText()));

            /*show bill id only if state is billed;*/
            if (chosenReading.getReadingState() == AllMetersData.BILLED) {
                this.billID.setText(String.valueOf(chosenReading.getBillId()));
            }

            /*set the reading of the meter*/
            this.reading.setText(String.valueOf(chosenReading.getLastMeterReading()));

            /*set the reading date*/
            this.date.setText(AllMetersData.getMeterDate(chosenReading.getDate()));

        }
    }
}