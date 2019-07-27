package vn.ochabot.seaconnect.core.helpers;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import vn.ochabot.seaconnect.R;
import vn.ochabot.seaconnect.core.App;

/**
 * @author Thien.
 */
public class AvatarDialog {

    Context context;
    String titleText;
    String contentText;
    int confirmText;
    String avatarUrl;
    View.OnClickListener onConfirm;

    public AvatarDialog(Context context, String titleText, String contentText, int confirmText, String avatarUrl,
                        View.OnClickListener onConfirm) {
        this.context = context;
        this.titleText = titleText;
        this.contentText = contentText;
        this.confirmText = confirmText;
        this.onConfirm = onConfirm;
        this.avatarUrl = avatarUrl;
    }

    public void showDialog() {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.oc_view_bill_duration, null);
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.PopupAlertTheme)
                .setView(view)
                .create();

        dialog.setCanceledOnTouchOutside(false);

        Glide.with(App.appContext).load(avatarUrl).into((ImageView) view.findViewById(R.id.avatar));

        TextView titleView = view.findViewById(R.id.title);
        if (!TextUtils.isEmpty(titleText)) {
            titleView.setText(titleText);
        } else {
            titleView.setVisibility(View.GONE);
        }

        TextView contentView = view.findViewById(R.id.content);
        if (!TextUtils.isEmpty(contentText)) {
            contentView.setText(contentText);
        }

        TextView yesButton = view.findViewById(R.id.oc_btn_ok);
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

        dialog.show();
    }
}
