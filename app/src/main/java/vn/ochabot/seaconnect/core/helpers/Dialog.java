package vn.ochabot.seaconnect.core.helpers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import vn.ochabot.seaconnect.R;

/**
 * @author Thien.
 */
public class Dialog {

    Context context;
    int titleResId;
    String titleText;
    float titleTextSize;
    int contentResId;
    String contentText;
    SpannableStringBuilder contentSpan;
    int contentTextFontResId;
    float contentTextSize;
    int confirmText;
    int confirmTextColorResId;
    int confirmTextFontResId;
    float confirmTextSize;
    int rejectText;
    int rejectTextColorResId;
    int rejectTextFontResId;
    float rejectTextSize;
    boolean isShowFullScreen;
    boolean isSmallStyle;
    View.OnClickListener onConfirm;
    View.OnClickListener onRejected;

    public Dialog(Context context, int titleResId, String titleText, float titleTextSize, int contentResId, String contentText, SpannableStringBuilder contentSpan, int contentTextFontResId, float contentTextSize,
                  int confirmText, int confirmTextColorResId, int confirmTextFontResId, float confirmTextSize, int rejectText, int rejectTextColorResId, int rejectTextFontResId, float rejectTextSize,
                  View.OnClickListener onConfirm, View.OnClickListener onRejected, boolean isShowFullScreen, boolean isSmallStyle) {
        this.context = context;
        this.titleResId = titleResId;
        this.titleText = titleText;
        this.titleTextSize = titleTextSize;
        this.contentResId = contentResId;
        this.contentText = contentText;
        this.contentSpan = contentSpan;
        this.contentTextFontResId = contentTextFontResId;
        this.contentTextSize = contentTextSize;
        this.confirmText = confirmText;
        this.confirmTextColorResId = confirmTextColorResId;
        this.confirmTextFontResId = confirmTextFontResId;
        this.confirmTextSize = confirmTextSize;
        this.rejectText = rejectText;
        this.rejectTextColorResId = rejectTextColorResId;
        this.rejectTextFontResId = rejectTextFontResId;
        this.rejectTextSize = rejectTextSize;
        this.onConfirm = onConfirm;
        this.onRejected = onRejected;
        this.isShowFullScreen = isShowFullScreen;
        this.isSmallStyle = isSmallStyle;
    }

    public void showDialog() {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.view_confirm_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.PopupAlertTheme)
                .setView(view)
                .create();

        dialog.setCanceledOnTouchOutside(false);


        TextView titleView = view.findViewById(R.id.oc_text_title);
        if (titleResId > 0) {
            titleView.setText(titleResId);
        } else if (!TextUtils.isEmpty(titleText)) {
            titleView.setText(titleText);
        } else {
            titleView.setVisibility(View.GONE);
        }
        if (titleTextSize > 0f) {
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
        }

        TextView contentView = view.findViewById(R.id.oc_text_info);
        if (contentResId > 0) {
            contentView.setText(contentResId);
        } else if (!TextUtils.isEmpty(contentText)) {
            contentView.setText(contentText);
        } else if (contentSpan != null) {
            contentView.setText(contentSpan);
        }
        if (contentTextFontResId > 0) {
            contentView.setTypeface(ResourcesCompat.getFont(context, contentTextFontResId));
        }
        if (contentTextSize > 0f) {
            contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentTextSize);
        }

        TextView noButton = view.findViewById(R.id.oc_text_no);
        if (rejectTextColorResId > 0) {
            noButton.setTextColor(ContextCompat.getColor(context, rejectTextColorResId));
        }
        if (rejectTextFontResId > 0) {
            noButton.setTypeface(ResourcesCompat.getFont(context, rejectTextFontResId));
        }
        if (rejectText > 0) {
            noButton.setText(rejectText);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (onRejected != null) {
                        onRejected.onClick(v);
                    }
                }
            });
        } else {
            noButton.setVisibility(View.GONE);
            view.findViewById(R.id.divider).setVisibility(View.GONE);
        }
        if (rejectTextSize > 0f) {
            noButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, rejectTextSize);
        }

        TextView yesButton = view.findViewById(R.id.oc_text_yes);
        if (confirmTextColorResId > 0) {
            yesButton.setTextColor(ContextCompat.getColor(context, confirmTextColorResId));
        }
        if (confirmTextFontResId > 0) {
            yesButton.setTypeface(ResourcesCompat.getFont(context, confirmTextFontResId));
        }
        if (confirmText > 0) {
            yesButton.setText(confirmText);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (onConfirm != null) {
                        onConfirm.onClick(v);
                    }
                }
            });
        } else {
            yesButton.setVisibility(View.GONE);
            view.findViewById(R.id.divider).setVisibility(View.GONE);
        }
        if (confirmTextSize > 0f) {
            yesButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, confirmTextSize);
        }

        dialog.show();
    }
}
