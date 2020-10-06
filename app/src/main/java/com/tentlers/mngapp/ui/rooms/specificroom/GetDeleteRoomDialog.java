package com.tentlers.mngapp.ui.rooms.specificroom;

import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tentlers.mngapp.R;

public class GetDeleteRoomDialog {

    public static MaterialAlertDialogBuilder getdeleteRoomDilog(Context context, DialogInterface.OnClickListener listener) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle("Delete Room ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage("This will delete room and all its related data.")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Delete Room", listener);
        return dialogBuilder;
    }
}
