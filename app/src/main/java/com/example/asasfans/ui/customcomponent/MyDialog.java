package com.example.asasfans.ui.customcomponent;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.asasfans.R;

/**
 * @author: akari
 * @date: 2022/3/5
 * @description $
 */
public class MyDialog extends Dialog implements View.OnClickListener {

    private TextView mTitle, mContent;
    private TextView mConfirm, mCancel;

    private Context mContext;
    private String content;
    private OncloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public MyDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public MyDialog(@NonNull Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public MyDialog(@NonNull Context context, int themeResId, OncloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    public MyDialog(@NonNull Context context, int themeResId, String content, OncloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected MyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    /**
     * 设置弹框标题
     * @param title 标题内容
     * @return
     */
    public MyDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置弹框的提示内容
     * @param content 弹框的提示内容
     * @return
     */
    public MyDialog setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 设置弹框确认键的内容
     * @param name 确认键显示内容
     * @return
     */
    public MyDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    /**
     * 设置弹框取消键的内容
     * @param name 取消键显示内容
     * @return
     */
    public MyDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        setCanceledOnTouchOutside(false);
        mTitle = findViewById(R.id.dialog_title);
        mContent = findViewById(R.id.dialog_content);
        mConfirm = findViewById(R.id.confirm);
        mCancel = findViewById(R.id.cancel);

        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (listener != null) {
                    listener.onClick(true);
                }
                this.dismiss();
                break;
            case R.id.cancel:
                if (listener != null) {
                    listener.onClick(false);
                }
                this.dismiss();
                break;
        }
    }

    public interface OncloseListener {
        void onClick(boolean confirm);
    }
}

