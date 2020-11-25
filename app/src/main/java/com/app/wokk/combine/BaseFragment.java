package com.app.wokk.combine;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.wokk.customAlert.RotateDialog;

import java.util.Objects;

public class BaseFragment extends Fragment {

    public RotateDialog rotateDialog;

    public void hideKeyBoardText(TextView textView) {
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    public void hideKeyBoardLinearlayout(LinearLayout linearLayout) {
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
    }

    public void hideKeyBoardButton(Button button) {
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(button.getWindowToken(), 0);
    }

    public void hideKeyBoardEditText(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void hideKeyBoardRelativeLayout(RelativeLayout relativeLayout) {
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
    }

    public void hideKeyBoardImageView(ImageView imageView) {
        InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(imageView.getWindowToken(), 0);
    }

    public void showRotateDialog(){
        if (rotateDialog == null) {
            rotateDialog = new RotateDialog(Objects.requireNonNull(getActivity()));
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
