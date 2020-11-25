package com.app.wokk.customAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import com.app.wokk.R;
import java.util.Objects;
import static android.graphics.Color.TRANSPARENT;

public class CustomGalleryAlert extends Dialog {

    public ImageView ivImage;
    public LinearLayout llPrevious;
    public LinearLayout llNext;
    public ImageView ivClose;
    public TextView tvTitle;
    public TextView tvCaption;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gallery);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable((Drawable) (new ColorDrawable(TRANSPARENT)));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        getWindow().setGravity(Gravity.TOP);
        ivImage=findViewById(R.id.ivImage);
        llPrevious=findViewById(R.id.llPrevious);
        llNext=findViewById(R.id.llNext);
        ivClose=findViewById(R.id.ivClose);
        tvTitle=findViewById(R.id.tvTitle);
        tvCaption=findViewById(R.id.tvCaption);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
    public CustomGalleryAlert(@NonNull Context context) {
        super(context);
    }
}
