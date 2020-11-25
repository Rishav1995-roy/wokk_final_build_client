package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.app.wokk.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import static android.graphics.Color.TRANSPARENT;

public class EditProfileAlert extends Dialog {

    public LinearLayout llEdit;
    public TextInputEditText etFirstName;
    public TextInputEditText etLastname;
    public TextInputEditText etEmail;
    public TextInputEditText etAddress;
    public TextInputEditText etPin;
    public TextInputEditText etOrganisationame;
    public TextInputEditText etBio;
    public TextInputEditText etYoutubeLink;
    public Button btnSave;
    public RelativeLayout rlFemale;
    public RelativeLayout rlMale;
    public ImageView ivMale;
    public ImageView ivFemale;
    public boolean genderSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_profile);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable)(new ColorDrawable(TRANSPARENT)));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        ivFemale=findViewById(R.id.ivFemale);
        ivMale=findViewById(R.id.ivMale);
        ivFemale=findViewById(R.id.ivFemale);
        rlMale=findViewById(R.id.rlMale);
        rlFemale=findViewById(R.id.rlFemale);
        btnSave=findViewById(R.id.btnSave);
        etYoutubeLink=findViewById(R.id.etYoutubeLink);
        etBio=findViewById(R.id.etBio);
        etOrganisationame=findViewById(R.id.etOrganisationame);
        etPin=findViewById(R.id.etPin);
        etAddress=findViewById(R.id.etAddress);
        etEmail=findViewById(R.id.etEmail);
        etLastname=findViewById(R.id.etLastname);
        etFirstName=findViewById(R.id.etFirstName);
        llEdit=findViewById(R.id.llEdit);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public EditProfileAlert(@NonNull Context context) {
        super(context);
    }
}
