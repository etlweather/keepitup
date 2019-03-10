package de.ibba.keepitup.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import de.ibba.keepitup.R;
import de.ibba.keepitup.ui.NetworkTaskMainActivity;
import de.ibba.keepitup.util.BundleUtil;
import de.ibba.keepitup.util.StringUtil;

public class GeneralConfirmDialog extends DialogFragment {

    public enum Type {
        DELETE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(GeneralConfirmDialog.class.getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(GeneralConfirmDialog.class.getName(), "onCreateView");
        View view = inflater.inflate(R.layout.dialog_general_confirm, container);
        String message = BundleUtil.bundleToMessage(GeneralConfirmDialog.class.getSimpleName(), Objects.requireNonNull(getArguments()));
        prepareConfirmMessage(view, message);
        prepareOkCancelImageButtons(view);
        return view;
    }

    private void prepareConfirmMessage(View view, String message) {
        Log.d(GeneralConfirmDialog.class.getName(), "prepareConfirmMessage");
        TextView messageText = view.findViewById(R.id.textview_dialog_general_confirm_message);
        messageText.setText(message);
    }

    private void prepareOkCancelImageButtons(View view) {
        Log.d(GeneralConfirmDialog.class.getName(), "prepareOkCancelImageButtons");
        ImageView okImage = view.findViewById(R.id.imageview_dialog_general_confirm_ok);
        ImageView cancelImage = view.findViewById(R.id.imageview_dialog_general_confirm_cancel);
        okImage.setOnClickListener(this::onOkClicked);
        cancelImage.setOnClickListener(this::onCancelClicked);
    }

    private void onOkClicked(@SuppressWarnings("unused") View view) {
        Log.d(GeneralConfirmDialog.class.getName(), "onOkClicked");
        NetworkTaskMainActivity activity = (NetworkTaskMainActivity) getActivity();
        String typeString = BundleUtil.bundleToMessage(GeneralConfirmDialog.Type.class.getSimpleName(), Objects.requireNonNull(getArguments()));
        if (StringUtil.isEmpty(typeString)) {
            Log.e(GeneralConfirmDialog.class.getName(), GeneralConfirmDialog.Type.class.getSimpleName() + " not specified.");
            Objects.requireNonNull(activity).onConfirmDialogOkClicked(this, null);
            return;
        }
        Type type = null;
        try {
            type = Type.valueOf(typeString);
        } catch (IllegalArgumentException exc) {
            Log.e(GeneralConfirmDialog.class.getName(), GeneralConfirmDialog.Type.class.getSimpleName() + "." + typeString + " does not exist");
        }
        Objects.requireNonNull(activity).onConfirmDialogOkClicked(this, type);
    }

    private void onCancelClicked(@SuppressWarnings("unused") View view) {
        Log.d(GeneralConfirmDialog.class.getName(), "onCancelClicked");
        NetworkTaskMainActivity activity = (NetworkTaskMainActivity) getActivity();
        Objects.requireNonNull(activity).onConfirmDialogCancelClicked(this);
    }
}
