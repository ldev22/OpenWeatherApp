package com.wungatech.openweather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionDialog {

    private final Context context;
    private final String permission;
    private final int rationaleMessage;
    private final String positiveButtonText;
    private final String negativeButtonText;
    private final OnPermissionResultListener onPermissionResult;

    public PermissionDialog(
            Context context,
            String permission,
            int rationaleMessage,
            String positiveButtonText,
            String negativeButtonText,
            OnPermissionResultListener onPermissionResult) {
        this.context = context;
        this.permission = permission;
        this.rationaleMessage = rationaleMessage;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
        this.onPermissionResult = onPermissionResult;
    }

    public void show() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(rationaleMessage)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPermissionResult.onPermissionResult(false);
                    }
                })
                .setCancelable(false)
                .create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_background);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.sunrise));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.rounded_dialog_background);

                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.sunrise));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.rounded_dialog_background);
            }
        });

        alertDialog.show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                (AppCompatActivity) context,
                new String[]{permission},
                PERMISSION_REQUEST_CODE
        );
    }

    private static final int PERMISSION_REQUEST_CODE = 123;

    public static void handlePermissionResult(
            int requestCode,
            int[] grantResults,
            OnPermissionResultListener onPermissionResult) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                onPermissionResult.onPermissionResult(true);
            } else {
                // Permission denied
                onPermissionResult.onPermissionResult(false);
            }
        }
    }

    public interface OnPermissionResultListener {
        void onPermissionResult(boolean granted);
    }
}

