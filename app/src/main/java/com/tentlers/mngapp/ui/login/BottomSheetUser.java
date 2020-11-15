package com.tentlers.mngapp.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tentlers.mngapp.databinding.BottomSheetUserBinding;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheetUser extends BottomSheetDialogFragment {
    BottomSheetUserBinding binding;
    OnEditUserClickedListener listener;


    public BottomSheetUser() {
    }

    public interface OnEditUserClickedListener {
        public void OnEditUserClicked(View view);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetUserBinding.inflate(inflater,container,false);
        binding.textviewBottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnEditUserClicked(v);
            }
        });
        return binding.getRoot();
    }
}
