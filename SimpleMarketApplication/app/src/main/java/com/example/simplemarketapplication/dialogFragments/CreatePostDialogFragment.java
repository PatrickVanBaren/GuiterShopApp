package com.example.simplemarketapplication.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.simplemarketapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreatePostDialogFragment extends DialogFragment {

    private Listener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (CreatePostDialogFragment.Listener) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder warningBuilder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle(R.string.product_create_title)
                .setView(LayoutInflater.from(getActivity()).inflate(R.layout.fragment_post_dialog, null, false))
                .setPositiveButton(R.string.positive_button, ((dialog, which) -> {
                    final TextInputEditText textInputEditTextName = getDialog().findViewById(R.id.name_input_view);
                    final TextInputEditText textInputEditTextPrice = getDialog().findViewById(R.id.price_input_view);
                    final String textName = textInputEditTextName.getText().toString();
                    final String textPrice = textInputEditTextPrice.getText().toString();
                    if (textName.isEmpty() || textPrice.isEmpty()) {
                        warningBuilder
                                .setTitle(R.string.warning)
                                .setMessage(R.string.post_error_warning)
                                .setPositiveButton(R.string.ok_button, null)
                                .create().show();
                    } else {
                        mListener.onCreatePost(new CreatePostDialogFragment(), textName, textPrice);
                    }
                }))
                .setNegativeButton(R.string.negative_button, null);
        return builder.create();
    }

    public interface Listener {
        void onCreatePost(CreatePostDialogFragment fragment, String name, String price);
    }
}
