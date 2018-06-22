package io.display.displayiosampleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import io.display.displayiosampleapp.util.StaticValues;
import io.display.sdk.Controller;

public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Controller.getInstance().init(this, StaticValues.APP_ID, false);
    }

    @Override
    protected void onDestroy() {
        Controller.getInstance().onDestroy();
        super.onDestroy();
    }

    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showNotification(String message, int length, boolean error) {
        Toast toast = Toast.makeText(this, message, length);
        toast.getView().setBackgroundResource(error ? R.drawable.bg_red_toast : R.drawable.bg_green_toast);
        toast.show();
    }
}
