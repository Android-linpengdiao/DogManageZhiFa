package com.base.manager;

import android.app.Activity;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.BaseApplication;
import com.base.R;
import com.base.TextAdapter;
import com.base.ZxingManager;
import com.base.databinding.ViewAuthenticationDialogBinding;
import com.base.databinding.ViewRankDialogBinding;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class DialogManager {

    private static final String TAG = "DialogManager";

    private static DialogManager mInstance;

    private DialogManager() {
    }

    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DialogManager();
                }
            }
        }
        return mInstance;
    }

    public static AppCompatDialog createLoadingDialog(Activity act) {
        return createLoadingDialog(act, "");
    }

    public static AppCompatDialog createLoadingDialog(Activity act, String content) {
        if (null != act) {
            AppCompatDialog appCompatDialog = new AppCompatDialog(act, R.style.LoadingDialogTheme);
            View view = act.getLayoutInflater().inflate(R.layout.layout_loading, null);
            appCompatDialog.setContentView(view);
            TextView title = view.findViewById(R.id.content);
            ImageView progress = view.findViewById(R.id.progress);
            if (!CommonUtil.isBlank(content)) {
                title.setText("" + content);
            }
            Animation antv = AnimationUtils.loadAnimation(act, R.anim.loading_progressbar);
            LinearInterpolator lin = new LinearInterpolator();
            antv.setInterpolator(lin);
            antv.setRepeatCount(-1);
            progress.startAnimation(antv);
            appCompatDialog.setCanceledOnTouchOutside(false);
            return appCompatDialog;
        } else {
            return null;
        }
    }

    public interface Listener {
        void onItemLeft();

        void onItemRight();
    }

    public interface OnClickListener {
        void onClick(View view, Object object);
    }

    public static AlertDialog showConfirmDialog(Activity act, String content, final Listener listener) {
        return showConfirmDialog(act, null, content, null, null, listener);
    }

    public static AlertDialog showConfirmDialog(Activity act, String title, String content, final Listener listener) {
        return showConfirmDialog(act, title, content, null, null, listener);
    }

    public static AlertDialog showConfirmDialog(Activity act, String title, String content, String leftText, String rightText, final Listener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        window.setContentView(R.layout.view_confirm_dialog_alert);
        TextView tvTitle = window.findViewById(R.id.title);
        TextView tvContent = window.findViewById(R.id.content);
        TextView tvLeft = window.findViewById(R.id.tv_left);
        TextView tvRight = window.findViewById(R.id.tv_right);
        tvContent.setText(content);
//        tvContent.setVisibility(CommonUtil.isBlank(content) ? View.GONE : View.VISIBLE);
        if (!CommonUtil.isBlank(title)) {
            tvTitle.setText(title);
        }
        if (!CommonUtil.isBlank(leftText)) {
            tvLeft.setText(leftText);
        }
        if (!CommonUtil.isBlank(rightText)) {
            tvRight.setText(rightText);
        }
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemLeft();
                }
                dialog.cancel();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemRight();
                }
                dialog.cancel();
            }
        });
        return dialog;
    }

    public static void showQRCodeDialog(Activity act, String qrCode, String type, int id) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        window.setContentView(R.layout.view_qrcode_dialog_alert);
        ImageView userIconImageView = window.findViewById(R.id.userIconImageView);
        ImageView qrCodeImageView = window.findViewById(R.id.qrCodeImageView);
        TextView userNameTextView = window.findViewById(R.id.userNameTextView);
//        userNameTextView.setText(BaseApplication.getInstance().getUserInfo().getName());
        Bitmap bitmap = ZxingManager.getInstance(act).createQRCode(qrCode + "?" + type + "=" + id);
        qrCodeImageView.setImageBitmap(bitmap);
//        GlideLoader.LoderCircleImage(act, BaseApplication.getInstance().getUserInfo().getIcon(), userIconImageView);
    }

    public static void showGroupNameDialog(final Activity act, String name, String title, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_group_name_dialog_alert);
        TextView titleTextView = window.findViewById(R.id.title);
        final EditText contentEditText = window.findViewById(R.id.content);
        TextView tvLeft = window.findViewById(R.id.tv_left);
        TextView tvRight = window.findViewById(R.id.tv_right);
        titleTextView.setText(title);
        contentEditText.setText(name);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                if (listener != null) {
                    if (CommonUtil.isBlank(content)) {
                        ToastUtils.showShort(act, "请输入群名称");
                    } else {
                        listener.onClick(v, content);
                    }
                }
                dialog.cancel();
            }
        });
    }

    public static void showRoomNameDialog(final Activity act, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_room_name_dialog);
        final EditText contentEditText = window.findViewById(R.id.content);
        final TextView txtNumView = window.findViewById(R.id.txtNumView);
        TextView tvLeft = window.findViewById(R.id.tv_left);
        TextView tvRight = window.findViewById(R.id.tv_right);
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null) {
                    txtNumView.setText(charSequence.length() + "/60");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                if (listener != null) {
                    if (CommonUtil.isBlank(content)) {
                        ToastUtils.showShort(act, "请填写房间主题");
                    } else {
                        listener.onClick(v, content);
                    }
                }
                dialog.cancel();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                CommonUtil.hideSoftInput(act, contentEditText);
            }
        });
    }

    public static void showEditTextDialog(final Activity act, String title, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_edittext_dialog);
        final EditText contentEditText = window.findViewById(R.id.content);
        final TextView txtNumView = window.findViewById(R.id.txtNumView);
        TextView tvTitle = window.findViewById(R.id.title);
        TextView tvLeft = window.findViewById(R.id.tv_left);
        TextView tvRight = window.findViewById(R.id.tv_right);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null) {
                    txtNumView.setText(charSequence.length() + "/60");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                if (listener != null) {
                    if (CommonUtil.isBlank(content)) {
                        ToastUtils.showShort(act, "请输入内容");
                    } else {
                        listener.onClick(v, content);
                    }
                }
                dialog.cancel();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                CommonUtil.hideSoftInput(act, contentEditText);
            }
        });
    }

    public static void showClipboardDialog(final Activity act, final String content) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_age_dialog_alert);
        TextView clipboardView = window.findViewById(R.id.content);
        clipboardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.clipboard(act, content);
                dialog.cancel();
            }
        });
    }

    public static void showAudioDialog(Activity act) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_audio_dialog_alert);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        }, 1000);
    }

    public void showRankDialog(Activity activity, List<String> list, int position, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_rank_dialog, null);
        window.setContentView(contentView);
        ViewRankDialogBinding binding = DataBindingUtil.bind(contentView);
        TextAdapter textAdapter = new TextAdapter(activity);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerView.setAdapter(textAdapter);
        textAdapter.setDone(position);
        textAdapter.refreshData(list);
        textAdapter.setOnClickListener(new com.base.view.OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                String content = (String) object;
                listener.onClick(view, content);
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    /**
     * @param activity
     * @param type       0-选择身份  1-编辑  2-提示
     * @param titleStr
     * @param contentStr
     * @param confirmStr
     * @param listener
     */
    public static void showAuthenticationDialog(final Activity activity, final int type, String titleStr, String contentStr, String confirmStr, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_authentication_dialog, null);
        window.setContentView(contentView);
        final ViewAuthenticationDialogBinding binding = DataBindingUtil.bind(contentView);
        if (!TextUtils.isEmpty(titleStr)) {
            binding.titleView.setText(titleStr);
        }
        if (!TextUtils.isEmpty(contentStr)) {
            binding.contentView.setText(contentStr);
        }
        if (!TextUtils.isEmpty(confirmStr)) {
            binding.confirmView.setText(confirmStr);
        }

        binding.radioGroupView.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        binding.editView.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        binding.contentView.setVisibility(type == 2 ? View.VISIBLE : View.GONE);

        binding.confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int checkedId = binding.radioGroupView.getCheckedRadioButtonId();
//                int authenticationType = 0;
//                if (checkedId == R.id.radioButtonOrgan) {
//                    authenticationType = 0;
//                } else if (checkedId == R.id.radioButtonTeacher) {
//                    authenticationType = 1;
//                } else if (checkedId == R.id.radioButtonStudent) {
//                    authenticationType = 2;
//                }
                if (type == 0) {
                    listener.onClick(view, binding.radioGroupView.getCheckedRadioButtonId());
                } else if (type == 1) {
                    String content = binding.editView.getText().toString().trim();
                    if (CommonUtil.isBlank(content)) {
                        ToastUtils.showShort(activity, "请输入口令...");
                        return;
                    }
                    listener.onClick(view, content);
                } else {
                    listener.onClick(view, null);
                }
                dialog.cancel();
            }
        });
        binding.cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    /**
     * @param activity
     * @param type       0-选择身份  1-编辑  2-提示
     * @param titleStr
     * @param contentStr
     * @param confirmStr
     * @param listener
     */
    public static void showAuthenticationDialog(Activity activity, int type, String titleStr, SpannableStringBuilder contentStr, String confirmStr, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_authentication_dialog, null);
        window.setContentView(contentView);
        final ViewAuthenticationDialogBinding binding = DataBindingUtil.bind(contentView);
        if (!TextUtils.isEmpty(titleStr)) {
            binding.titleView.setText(titleStr);
        }
        if (!TextUtils.isEmpty(contentStr)) {
            binding.contentView.setText(contentStr);
        }
        if (!TextUtils.isEmpty(confirmStr)) {
            binding.confirmView.setText(confirmStr);
        }

        binding.radioGroupView.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        binding.editView.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        binding.contentView.setVisibility(type == 2 ? View.VISIBLE : View.GONE);

        binding.cancelView.setVisibility(type == 2 ? View.GONE : View.VISIBLE);
        binding.lineView.setVisibility(type == 2 ? View.GONE : View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.confirmView.getLayoutParams();
        layoutParams.weight = 0;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = CommonUtil.dip2px(activity, 126);

        binding.confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedId = binding.radioGroupView.getCheckedRadioButtonId();
                int authenticationType = 0;
                if (checkedId == R.id.radioButtonOrgan) {
                    authenticationType = 0;
                } else if (checkedId == R.id.radioButtonTeacher) {
                    authenticationType = 1;
                } else if (checkedId == R.id.radioButtonStudent) {
                    authenticationType = 2;
                }
                listener.onClick(view, binding.radioGroupView.getCheckedRadioButtonId());
                dialog.cancel();
            }
        });
        binding.cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    public static void showSuperDialog(final Activity act, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_super_dialog_alert);
        final EditText contentEditText = window.findViewById(R.id.content);
        TextView superLoveTextView = window.findViewById(R.id.superLoveTextView);
        TextView superFocusTextView = window.findViewById(R.id.superFocusTextView);
        TextView tvLeft = window.findViewById(R.id.tv_left);
        TextView tvRight = window.findViewById(R.id.tv_right);
//        superFocusTextView.setText(String.format(act.getString(R.string.OnlyLeftLikes), String.valueOf(BaseApplication.getInstance().getUserInfo().getRemainingSuperFocus())));
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                if (listener != null) {
                    listener.onClick(v, content);
                }
                dialog.cancel();
            }
        });
        superLoveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v, null);
                }
            }
        });
    }

    public static void showSexDialog(final Activity act, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_sex_dialog_alert);
        final RadioGroup radioGroupView = window.findViewById(R.id.radio_group_view);
        View confirmView = window.findViewById(R.id.confirmView);
        radioGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_button_sex_0) {
                } else if (checkedId == R.id.radio_button_sex_1) {
                } else if (checkedId == R.id.radio_button_sex_2) {
                }
            }
        });
        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int searchFriendSex = 0;
                    if (radioGroupView.getCheckedRadioButtonId() == R.id.radio_button_sex_0) {
                        searchFriendSex = 0;
                    } else if (radioGroupView.getCheckedRadioButtonId() == R.id.radio_button_sex_1) {
                        searchFriendSex = 1;
                    } else if (radioGroupView.getCheckedRadioButtonId() == R.id.radio_button_sex_2) {
                        searchFriendSex = 2;
                    }
                    listener.onClick(v, searchFriendSex);
                }
                dialog.cancel();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                listener.onClick(null, null);
            }
        });

    }

    public static void showCheckUserIconDialog(final Activity act, final OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.view_check_user_icon_dialog_alert);
        ImageView userIconImageView = window.findViewById(R.id.userIconImageView);
        View confirmView = window.findViewById(R.id.confirmView);
//        GlideLoader.LoderImage(act, BaseApplication.getInstance().getUserInfo().getIcon(), userIconImageView, 4);
        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (listener != null) {
                    listener.onClick(v, null);
                }
            }
        });

    }

    public static void showServiceDialog(Activity act, final OnClickListener onClickListener, final Listener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(act.getResources().getColor(R.color.transparent));
        window.setContentView(R.layout.view_service_dialog_alert);
        TextView titleView = window.findViewById(R.id.titleView);
        TextView tvDesc = window.findViewById(R.id.tv_desc);
        TextView tvConfirm = window.findViewById(R.id.tv_confirm);
        TextView tvCancel = window.findViewById(R.id.tv_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                onClickListener.onClick(v, null);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                onClickListener.onClick(v, null);
            }
        });
        setTypeface(act,titleView);

        String userText = "《服务协议》";
        String yinsiText = "《隐私政策》";
        String content = tvDesc.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        // 设置字体颜色
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#489DFA")), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#489DFA")), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体大小
//        spannableString.setSpan(new AbsoluteSizeSpan(CommonUtil.sp2px(mContext, 14)), content.indexOf(stateText), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置点击
        spannableString.setSpan(new MyClickableSpan(act, userText, listener), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new MyClickableSpan(act, yinsiText, listener), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvDesc.setText(spannableString);
        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件

    }

    public static class MyClickableSpan extends ClickableSpan {

        private Activity activity;
        private String msg;
        private Listener listener;

        public MyClickableSpan(Activity activity, String cm, Listener listener) {
            this.activity = activity;
            this.msg = cm;
            this.listener = listener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View view) {
            if (msg.equals("《服务协议》")) {
                listener.onItemLeft();
            } else if (msg.equals("《隐私政策》")) {
                listener.onItemRight();
            }
        }
    }

    public static void setTypeface(Activity activity, TextView textView) {
        Typeface typeface = BaseApplication.getInstance().getTypeface();
        textView.setTypeface(typeface);
    }

}

