package com.example.simplemarketapplication.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.posts.post.PostUser;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditUserPostDialogFragment extends DialogFragment {

    private Listener mListener;
    private PostUser mPostUser;

    public PostUser getPostUser() {
        return mPostUser;
    }

    public void setPostUser(PostUser mPostUser) {
        this.mPostUser = mPostUser;
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

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_user_dialog, null);
        TextInputEditText nameTextInput = view.findViewById(R.id.name_input_view);
        TextInputEditText phoneTextInput = view.findViewById(R.id.price_input_view);
        nameTextInput.setText(mPostUser.getUserName());
        phoneTextInput.setText(mPostUser.getPhoneNumber());
        builder.setView(view);

        builder
                .setTitle(R.string.edit_user)
                .setPositiveButton(R.string.ok_button, (dialog, which) -> {
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
                        mListener.onEditUserPost(new EditUserPostDialogFragment(), userName, phoneNumber);
                    }
                })
                .setNegativeButton(R.string.cancel_button, null);
        return builder.create();
    }

    public interface Listener {
        void onEditUserPost(EditUserPostDialogFragment fragment, String name, String phone);
    }
}
