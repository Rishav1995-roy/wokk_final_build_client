package com.app.wokk.combine;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.app.wokk.customAlert.RotateDialog;

public class BaseClass extends AppCompatActivity {

    public RotateDialog rotateDialog;


    public void hideKeyBoardText(TextView textView) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    public void hideKeyBoardLinearlayout(LinearLayout linearLayout) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
    }

    public void hideKeyBoardButton(Button button) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(button.getWindowToken(), 0);
    }

    public void hideKeyBoardEditText(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void hideKeyBoardRelativeLayout(RelativeLayout relativeLayout) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
    }

    public void hideKeyBoardImageView(ImageView imageView) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(imageView.getWindowToken(), 0);
    }

    public void showRotateDialog(){
        if (rotateDialog == null) {
            rotateDialog = new RotateDialog(this);
        }
        rotateDialog.show();
    }

    public void hideRotateDialog() {
        if (rotateDialog != null && rotateDialog.isShowing()) {
            rotateDialog.dismiss();
        }
        rotateDialog = null;
    }
}
