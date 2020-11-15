package com.tentlers.mngapp.ui.login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment implements Toolbar.OnMenuItemClickListener, OnCompleteListener<Void> {

    String userName;
    FirebaseUser firebaseUser;
    FragmentLoginBinding binding;
    HouseViewModal viewModal;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Snackbar.make(binding.getRoot(), "Please Enter Valid Details", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);

        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.loginToolbar.setOnMenuItemClickListener(this);
        binding.loginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userName = s.toString();
            }
        });

        /*check if the user name already exists*/
        if (viewModal.getUserName() != null || viewModal.getUserName().length() != 0) {
            binding.loginUserName.setText(viewModal.getUserName());
        }
        return binding.getRoot();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menuitem_house_save) {
            if (userName != null && userName.length() != 0) {
                if (firebaseUser != null) {
                    showprogressBar(true);

                    if (userName.equals(viewModal.getUserName())) {
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                    } else {
                        UserProfileChangeRequest changeRequest =new  UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();
                        firebaseUser.updateProfile(changeRequest).addOnCompleteListener(this);
                    }

                }
            }
        }
        return true;
    }

    private void showprogressBar(boolean isShow) {
        binding.loginOutlineUsername.setVisibility(isShow?View.GONE:View.VISIBLE);
        binding.loginProgressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isComplete()) {
            Navigation.findNavController(binding.getRoot()).navigateUp();
        } else {
            showprogressBar(false);
        }
    }
}