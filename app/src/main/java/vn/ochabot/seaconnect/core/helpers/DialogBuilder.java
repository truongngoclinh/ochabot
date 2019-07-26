package vn.ochabot.seaconnect.core.helpers;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;

public class DialogBuilder {
    private Context mContext;
    private int mTitleResId;
    private String mTitleText;
    private float mTitleTextSize;
    private int mContentResId;
    private String mContentText;
    private int mContentTextFontResId;
    private float mContentTextSize;
    private SpannableStringBuilder mContentSpan;
    private int mConfirmText;
    private int mConfirmTextColorResId;
    private int mConfirmTextFontResId;
    private float mConfirmTextSize;
    private int mRejectText;
    private int mRejectTextColorResId;
    private int mRejectTextFontResId;
    private float mRejectTextSize;
    private boolean mIsShowFullScreen = true;
    private boolean mIsSmallStyle = false;
    private View.OnClickListener mOnConfirm;
    private View.OnClickListener mOnRejected;

    public DialogBuilder(Context context) {
        mContext = context;
    }

    public DialogBuilder titleResId(int titleResId) {
        mTitleResId = titleResId;
        return this;
    }

    public DialogBuilder titleText(String titleText) {
        mTitleText = titleText;
        return this;
    }

    public DialogBuilder titleTextSize(float titleTextSize) {
        mTitleTextSize = titleTextSize;
        return this;
    }

    public DialogBuilder contentResId(int contentResId) {
        mContentResId = contentResId;
        return this;
    }

    public DialogBuilder contentText(String contentText) {
        mContentText = contentText;
        return this;
    }

    public DialogBuilder contentSpan(SpannableStringBuilder contentSpan) {
        mContentSpan = contentSpan;
        return this;
    }

    public DialogBuilder contentTextFontResId(int contentTextFontResId) {
        mContentTextFontResId = contentTextFontResId;
        return this;
    }

    public DialogBuilder contentTextSize(float contentTextSize) {
        mContentTextSize = contentTextSize;
        return this;
    }

    public DialogBuilder confirmText(int confirmText) {
        mConfirmText = confirmText;
        return this;
    }

    public DialogBuilder confirmTextColorResId(int confirmTextColorResId) {
        mConfirmTextColorResId = confirmTextColorResId;
        return this;
    }

    public DialogBuilder confirmTextFontResId(int confirmTextFontResId) {
        mConfirmTextFontResId = confirmTextFontResId;
        return this;
    }

    public DialogBuilder confirmTextSize(float confirmTextSize) {
        mConfirmTextSize = confirmTextSize;
        return this;
    }

    public DialogBuilder rejectText(int rejectText) {
        mRejectText = rejectText;
        return this;
    }

    public DialogBuilder rejectTextColorResId(int rejectTextColorResId) {
        mRejectTextColorResId = rejectTextColorResId;
        return this;
    }

    public DialogBuilder rejectTextFontResId(int rejectTextFontResId) {
        mRejectTextFontResId = rejectTextFontResId;
        return this;
    }

    public DialogBuilder rejectTextSize(float rejectTextSize) {
        mRejectTextSize = rejectTextSize;
        return this;
    }

    public DialogBuilder onConfirmClick(View.OnClickListener onConfirm) {
        mOnConfirm = onConfirm;
        return this;
    }

    public DialogBuilder onRejectClick(View.OnClickListener onRejected) {
        mOnRejected = onRejected;
        return this;
    }

    public DialogBuilder isShowFullScreen(boolean isShowFullScreen) {
        mIsShowFullScreen = isShowFullScreen;
        return this;
    }

    public DialogBuilder isSmallStyle(boolean isSmallStyle) {
        mIsSmallStyle = isSmallStyle;
        return this;
    }

    public Dialog createDialog() {
        return new Dialog(mContext, mTitleResId, mTitleText, mTitleTextSize, mContentResId, mContentText, mContentSpan, mContentTextFontResId, mContentTextSize,
                mConfirmText, mConfirmTextColorResId, mConfirmTextFontResId, mConfirmTextSize, mRejectText, mRejectTextColorResId, mRejectTextFontResId,
                mRejectTextSize, mOnConfirm, mOnRejected, mIsShowFullScreen, mIsSmallStyle);
    }
}