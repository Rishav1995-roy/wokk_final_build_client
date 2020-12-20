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
    public RelativeLayout parent;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gallery);
        ivImage=findViewById(R.id.ivImage);
        llPrevious=findViewById(R.id.llPrevious);
        llNext=findViewById(R.id.llNext);
        ivClose=findViewById(R.id.ivClose);
        tvTitle=findViewById(R.id.tvTitle);
        tvCaption=findViewById(R.id.tvCaption);
        parent=findViewById(R.id.parent);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
    public CustomGalleryAlert(@NonNull Context context) {
        super(context,R.style.my_dialoge_style);
    }
}
