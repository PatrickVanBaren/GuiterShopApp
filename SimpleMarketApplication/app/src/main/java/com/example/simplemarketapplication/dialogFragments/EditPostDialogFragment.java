package com.example.simplemarketapplication.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.posts.post.PostProduct;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditPostDialogFragment extends DialogFragment {

    private Listener mListener;
    private PostProduct mPostProduct;

    public void setPost(PostProduct postProduct){
        mPostProduct = postProduct;
    }

    public PostProduct getPost(){
        return this.mPostProduct;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (EditPostDialogFragment.Listener) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder warningBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_post_dialog, null);
        TextInputEditText nameTextInput = view.findViewById(R.id.name_input_view);
        TextInputEditText priceTextInput = view.findViewById(R.id.price_input_view);
        nameTextInput.setText(mPostProduct.getDescription());
        priceTextInput.setText(mPostProduct.getPrice());
        builder.setView(view);

        builder
                .setTitle(R.string.edit_product)
                .setPositiveButton(R.string.edit_button, ((dialog, which) -> {
                    final TextInputEditText nameTextEditInput = getDialog().findViewById(R.id.name_input_view);
                    final TextInputEditText priceTextEditInput = getDialog().findViewById(R.id.price_input_view);
                    final String nameText = nameTextEditInput.getText().toString();
                    final String priceText = priceTextEditInput.getText().toString();
                    if (nameText.isEmpty() || priceText.isEmpty()) {
                        warningBuilder
                                .setTitle(R.string.warning)
                                .setMessage(R.string.post_error_warning)
                                .setPositiveButton(R.string.ok_button, null)
                                .create().show();
                    } else {
                        mListener.onEditPost(new EditPostDialogFragment(), nameText, priceText);
                    }
                }))
                .setNegativeButton(R.string.negative_button, null);
        return builder.create();
    }

    public interface Listener {
        void onEditPost(EditPostDialogFragment fragment, String name, String price);
    }
}
