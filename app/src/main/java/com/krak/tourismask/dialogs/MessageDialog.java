package com.krak.tourismask.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
// Важно использовать именно эту версию AlertDialog, т.к. она легче катомизируется
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.krak.tourismask.R;


/*
    Диалоговое окно с текстом
 */
public class MessageDialog extends DialogFragment {

    private String message = null;
    private String title = null;

    public MessageDialog(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public MessageDialog(String title) {
        this.title = title;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.CustomAlertDialog));
        if (message != null){
            builder.setMessage(message);
        }
        AlertDialog dialog = builder.setTitle(title).setPositiveButton("Ok", null).create();
        dialog.setOnShowListener(arg0 -> {
            // Задаём кнопке "OK" белый цвет текста
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
        });
        return dialog;
    }
}
