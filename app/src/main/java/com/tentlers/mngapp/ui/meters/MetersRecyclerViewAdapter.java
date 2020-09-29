package com.tentlers.mngapp.ui.meters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
            holder.meterReadingState.setText(holder.chosenReading.getReadingState() == AllMetersData.BILLED ?
                    AllMetersData.BILLING_STATUS : holder.chosenReading.getHeadingText());
            currentReadingState = holder.chosenReading.getReadingState();
        } else holder.meterReadingState.setVisibility(View.GONE);

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

        public final View colorChanger;
        public final TextView billID, date, reading, showBillID, meterReadingState;
        LinearLayout orientationChanger;
        private AllMetersData chosenReading;

        public ViewHolder(View view) {
            super(view);
            colorChanger = view.findViewById(R.id.meter_relativelayout_for_orientation);
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
            this.colorChanger.setBackgroundColor(chosenReading.getColorState());
            this.orientationChanger.setGravity(chosenReading.getOrientationState());

            /*set the text style and text of the show bill id textview.*/
            this.showBillID.setTextAppearance(chosenReading.getTextStyle());
            this.showBillID.setText(chosenReading.getHeadingText());

            /*show bill id only if state is billed;*/
            if (chosenReading.getReadingState() == AllMetersData.BILLED) {
                this.billID.setText(String.valueOf(chosenReading.getBillId()));
            }

            this.reading.setText(String.valueOf(chosenReading.getLastMeterReading()));
            this.date.setText(chosenReading.getDate().toString());

        }
    }
}