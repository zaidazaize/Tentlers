package com.tentlers.mngapp.ui.home.specifichouse;

import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class GetDeleteHouseDialog {

    public MaterialAlertDialogBuilder getdeleteHouseDilog(Context context, DialogInterface.OnClickListener listener) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle("Delete House ?")
                .setMessage("This will delete house and all its related data.")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Delete House", listener);
        return dialogBuilder;
    }
}
