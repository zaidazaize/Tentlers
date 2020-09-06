package com.tentlers.mngapp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tentlers.mngapp.R;

import androidx.annotation.NonNull;

public class SaveDialog {


    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    Context context;
    View view;

    public SaveDialog(@NonNull final Activity context, final View view) {
        this.context = context;
        this.view = view;
    }

    public MaterialAlertDialogBuilder getCancelDialog(DialogInterface.OnClickListener positiveCallback,
                                                      DialogInterface.OnClickListener neutalCalback) {
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setTitle(R.string.exit)
                .setMessage("Are You Sure to Exit? ")
                .setPositiveButton("Exit", positiveCallback)
                .setNeutralButton("Cancel", neutalCalback);

        return materialAlertDialogBuilder;
    }

    public MaterialAlertDialogBuilder getSaveDialogue(DialogInterface.OnClickListener positiveCallback,
                                                      DialogInterface.OnClickListener negativeCallback,
                                                      DialogInterface.OnClickListener neutalCalback) {
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setTitle(R.string.save_changes)
                .setMessage("Are You Sure to Save Changes ")

                .setPositiveButton("save", positiveCallback)
                .setNeutralButton("Cancel", neutalCalback)
                .setNegativeButton("Discard", negativeCallback);
        return materialAlertDialogBuilder;
    }
}