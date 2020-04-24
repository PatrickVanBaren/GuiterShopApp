package com.example.simplemarketapplication.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.posts.post.PostShoppingBasket;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateUserDialogFragment extends DialogFragment {

    private Listener mListener;
    private PostShoppingBasket mPostProduct;

    public void setPost(PostShoppingBasket postProduct){
        mPostProduct = postProduct;
    }

    public PostShoppingBasket getPost(){
        return this.mPostProduct;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (Listener) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder warningBuilder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle(R.string.user_create_title)
                .setPositiveButton(R.string.positive_button, (dialog, which) -> {
                    final TextInputEditText userNameInput = getDialog().findViewById(R.id.enter_user_name_line_view);
                    final TextInputEditText phoneNumberInput = getDialog().findViewById(R.id.phone_number_line_view);
                    final String userName = userNameInput.getText().toString();
                    final String phoneNumber = phoneNumberInput.getText().toString();
                    if (userName.isEmpty() || phoneNumber.isEmpty()) {
                        warningBuilder
                                .setTitle(R.string.warning)
                                .setMessage(R.string.user_error_warning)
                                .setPositiveButton(R.string.ok_button, null)
                                .create().show();
                    } else {
                        mListener.onCreateUser(new CreateUserDialogFragment(), userName, phoneNumber);
                    }
                })
                .setNegativeButton(R.string.negative_button, null)
                .setView(LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_dialog, null, false));
        return builder.create();
    }

    public interface Listener {
        void onCreateUser(CreateUserDialogFragment fragment, String name, String phone);
    }
}
