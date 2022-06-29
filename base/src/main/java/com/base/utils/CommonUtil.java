package com.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.base.BaseApplication;
import com.base.MessageBus;
import com.base.R;
import com.base.manager.DialogManager;
import com.base.view.OnClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private static final String TAG = "CommonUtil";

    public static String getDogAge(int dogAge) {
        if (isBlank(dogAge)) {
            return "0个月";
        }
        return (dogAge / 12 > 0 ? dogAge / 12 + "岁" : "") + dogAge % 12 + "个月";

    }

    public static int getLocalVideoDuration(String videoPath) {
        //时长
        int duration;
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            duration = Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return duration;
    }

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }

    public static boolean isActivityEnable(Activity activity) {
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return false;
        }
        return true;
    }

    public static Bitmap zoomImg(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String FormatMiss(long miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        if (hh.equals("00")) {
            return mm + ":" + ss;
        } else {
            return hh + ":" + mm + ":" + ss;
        }
    }

    public static boolean isHigherVersion(String newv, String oldv) {

        String[] new_v = newv.split("\\.");
        String[] old_v = oldv.split("\\.");
        for (int i = 0; i < new_v.length; i++) {
            if (Integer.valueOf(old_v[i]) < Integer.valueOf(new_v[i])) {
                return true;
            } else if (Integer.valueOf(old_v[i]) == Integer.valueOf(new_v[i])) {
                continue;
            } else {
                break;
            }
        }

        return false;
    }

    /**
     * 设置textView结尾...后面显示的文字和颜色
     *
     * @param context    上下文
     * @param textView   textview
     * @param minLines   最少的行数
     * @param originText 原文本
     * @param endText    结尾文字
     * @param endColorID 结尾文字颜色id
     * @param isExpand   当前是否是展开状态
     */

    public static void toggleEllipsize(final Context context,
                                       final TextView textView,
                                       final int minLines,
                                       final String originText,
                                       final String endText,
                                       final int endColorID,
                                       final boolean isExpand) {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isExpand) {
//                    textView.setText(originText);
                    int paddingLeft = textView.getPaddingLeft();
                    int paddingRight = textView.getPaddingRight();
                    TextPaint paint = textView.getPaint();
                    float moreText = textView.getTextSize() * endText.length();
                    float availableTextWidth = (textView.getWidth() - paddingLeft - paddingRight) * minLines - moreText;
                    CharSequence ellipsizeStr = TextUtils.ellipsize(originText, paint, availableTextWidth, TextUtils.TruncateAt.END);
                    CharSequence temp = ellipsizeStr + endText;
                    SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                    ssb.setSpan(new ForegroundColorSpan(
                                    context.getResources().getColor(endColorID)),
                            temp.length() - endText.length(),
                            temp.length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                    ssb.setSpan(new MyClickableSpan(textView, isExpand),
                            temp.length() - endText.length(),
                            temp.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件

                    textView.setText(ssb);

                } else {
                    Log.i(TAG, "onGlobalLayout: ");
                    int paddingLeft = textView.getPaddingLeft();
                    int paddingRight = textView.getPaddingRight();
                    TextPaint paint = textView.getPaint();
                    float moreText = textView.getTextSize() * endText.length();
                    float availableTextWidth = (textView.getWidth() - paddingLeft - paddingRight) * minLines - moreText;
                    CharSequence ellipsizeStr = TextUtils.ellipsize(originText, paint, availableTextWidth, TextUtils.TruncateAt.END);
                    Log.i(TAG, "onGlobalLayout: " + (ellipsizeStr.length() < originText.length()));
                    if (ellipsizeStr.length() < originText.length()) {
                        CharSequence temp = ellipsizeStr + endText;
                        SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                        ssb.setSpan(new ForegroundColorSpan(
                                        context.getResources().getColor(endColorID)),
                                temp.length() - endText.length(),
                                temp.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                        ssb.setSpan(new MyClickableSpan(textView, isExpand),
                                temp.length() - endText.length(),
                                temp.length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件

                        textView.setText(ssb);
                        textView.setEnabled(true);

                    } else {
                        textView.setText(originText);
                        textView.setEnabled(false);

                    }
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    public static class MyClickableSpan extends ClickableSpan {

        private TextView textView;
        private boolean isExpand;

        public MyClickableSpan(TextView textView, boolean isExpand) {
            this.textView = textView;
            this.isExpand = isExpand;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View view) {
            MessageBus messageBus = new MessageBus.Builder()
                    .codeType(MessageBus.msgId_toggleEllipsize)
                    .param1(isExpand)
                    .param2(textView)
                    .build();
            RxBus.getDefault().post(messageBus);
        }
    }

    public static Bitmap viewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能

        return bitmap;
    }

    public static boolean openLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!ok) {//开了定位服务
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }
        return ok;
    }

    /**
     * 判断是否是一个合法的URL 网址
     *
     * @param url
     * @return
     */
    public static boolean isValidUrl(String url) {
        return TextUtils.isEmpty(url) == false && url.matches(Patterns.WEB_URL.pattern());
    }

    /**
     * //获取完整的域名
     *
     * @param text 获取浏览器分享出来的text文本
     */
    public static String getCompleteUrl(String text) {
        Pattern p = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?", Pattern.CASE_INSENSITIVE);
//        Pattern p = Pattern.compile("(((https?|ftp|file)://|)[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|])", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);
        matcher.find();
        return matcher.group();
    }

    //MD5加密，32位
    public static String payMD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    private final static String EMOTION_DB_NAME = "emotion.db"; //表情数据库

    public static SQLiteDatabase getEmotionDatabase() {
        try {
            String dbPath = BaseApplication.getInstance().getDatabasePath(EMOTION_DB_NAME).getParent();
            File dbPathDir = new File(dbPath);
            if (!dbPathDir.exists()) {
                dbPathDir.mkdirs();
            }

            File dest = new File(dbPathDir, EMOTION_DB_NAME);
            if (!dest.exists()) {
                dest.createNewFile();
                InputStream is = BaseApplication.getInstance().getAssets().open(EMOTION_DB_NAME);
                int size = is.available();
                byte buf[] = new byte[size];
                is.read(buf);
                is.close();
                FileOutputStream fos = new FileOutputStream(dest);
                fos.write(buf);
                fos.close();
            }

            return SQLiteDatabase.openOrCreateDatabase(BaseApplication.getInstance().getDatabasePath(EMOTION_DB_NAME), null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SpannableString getExpressionString(Context context, HashMap<String, String> emotionK, String str, int emotionSize) {
        if (CommonUtil.isBlank(str)) {
            return null;
        }
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
//	        String zhengze = "\\[[^\\]]+\\]";
        String zhengze = "\\[[\\u4e00-\\u9fa5a-z]{1,3}\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, emotionK, spannableString, sinaPatten, 0, emotionSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannableString;
    }

    public static void dealExpression(Context context, HashMap<String, String> emotionKV, SpannableString spannableString, Pattern patten, int start, int emotionSize) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            int size = sp2px(context, emotionSize);
            String value = emotionKV.get(key);
            if (CommonUtil.isBlank(value)) {
                continue;
            }
            Drawable d = Drawable.createFromStream(context.getAssets().open("emotion/" + value), null);
//            int size = sp2px(context, 20);
            d.setBounds(0, 0, size, size);
            ImageSpan imageSpan = new ImageSpan(d);
            // ImageSpan imageSpan = new ImageSpan(bitmap);
            // 计算该图片名字的长度，也就是要替换的字符串的长度
            int end = matcher.start() + key.length();
            // 将该图片替换字符串中规定的位置中
            spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            if (end < spannableString.length()) {
                // 如果整个字符串还未验证完，则继续。。
                dealExpression(context, emotionKV, spannableString, patten, end, emotionSize);
            }
            break;
        }
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void clipboard(Context context, String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
//        ToastUtils.showShort(context, context.getString(R.string.Copy));
    }

    /**
     * 系统剪贴板-获取
     */
    public static String getClipboardContent(Context context) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 返回数据
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData == null || clipData.getItemCount() <= 0) {
            return "";
        }
        ClipData.Item item = clipData.getItemAt(0);
        if (item == null || item.getText() == null) {
            return "";
        }
        return item.getText().toString();
    }

    public static String getYear(String birthTimeString, Context context) {
        String strs[] = birthTimeString.trim().split("-");
        int year = Integer.parseInt(strs[0]);

        if (year < 1900) {
            return "未知";
        }
        Integer start = 1900;
        String[] years = new String[]{
                context.getString(R.string.Rat),
                context.getString(R.string.OX),
                context.getString(R.string.Tiger),
                context.getString(R.string.Rabbit),
                context.getString(R.string.Dragon),
                context.getString(R.string.Snake),
                context.getString(R.string.Horse),
                context.getString(R.string.Sheep),
                context.getString(R.string.Monkey),
                context.getString(R.string.Rooster),
                context.getString(R.string.Dog),
                context.getString(R.string.Pig)
        };
        return years[(year - start) % years.length];
    }

    // 根据年月日计算年龄,birthTimeString:"1994-11-14"
    public static int getAgeFromBirthTime(String birthTimeString) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthTimeString.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

    /**
     * 计算星座
     * CSDN程忆难
     *
     * @param birthday
     * @return
     */
    public static String constellation(long birthday, Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("Mdd");
        Date birthdayDate = new Date(birthday);
        String format = simpleDateFormat.format(birthdayDate);
        int date = Integer.parseInt(format);
        if (date >= 121 && date <= 219) {
            return context.getString(R.string.Aquarius);
        } else if (date >= 220 && date <= 320) {
            return context.getString(R.string.Pisces);
        } else if (date >= 321 && date <= 420) {
            return context.getString(R.string.Aries);
        } else if (date >= 421 && date <= 521) {
            return context.getString(R.string.Taurus);
        } else if (date >= 522 && date <= 621) {
            return context.getString(R.string.Gemini);
        } else if (date >= 622 && date <= 722) {
            return context.getString(R.string.Cancer);
        } else if (date >= 723 && date <= 823) {
            return context.getString(R.string.Leo);
        } else if (date >= 824 && date <= 923) {
            return context.getString(R.string.Virgo);
        } else if (date >= 924 && date <= 1023) {
            return context.getString(R.string.Libra);
        } else if (date >= 1024 && date <= 1122) {
            return context.getString(R.string.Scorpio);
        } else if (date >= 1123 && date <= 1221) {
            return context.getString(R.string.Sagittarius);
        } else {
            return context.getString(R.string.Capricorn);
        }
    }

    public static boolean isPassword(Context context, String phone) {
        if (CommonUtil.isBlank(phone)) {
            ToastUtils.showShort(context, context.getString(R.string.Password));
            return false;
        } else if (phone.length() < 6) {
            ToastUtils.showShort(context, "请输入完整密码");
            return false;
        }
        return true;
    }

    public static boolean isPhone(Context context, String phone) {
        if (CommonUtil.isBlank(phone)) {
            ToastUtils.showShort(context, context.getString(R.string.EnterPhoneNumber));
            return false;
        } else if (phone.length() < 11) {
            ToastUtils.showShort(context, "请输入完整手机号");
            return false;
        }
        return true;
    }

    public static List<String> stringToList(String data) {
        List<String> list = new ArrayList<>();
        if (!CommonUtil.isBlank(data)) {
            for (String i : data.split(",")) {
                if (!i.equals("")) {
                    list.add(i);
                }
            }
        }
        return list;
    }

    public static List<String> getString() {
        String url1 = "中国";
        String url2 = "美国";
        String url3 = "英国";
        String url4 = "韩国";
        String url5 = "加拿大";
        String url6 = "新加坡";
        String url7 = "德国";
        String url8 = "意大利";
        String url9 = "希腊";
        return new ArrayList<String>(Arrays.asList(url1, url2, url3, url4, url5, url6, url7, url8, url9));
    }

    public static List<String> getListString() {
        String url1 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2836123484,3030068744&fm=26&gp=0.jpg";
        String url2 = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1789569952,4029204763&fm=26&gp=0.jpg";
        String url3 = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3580978037,2393601368&fm=26&gp=0.jpg";
        String url4 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1556616674,3087626701&fm=26&gp=0.jpg";
        String url5 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4001861342,3804929732&fm=26&gp=0.jpg";
        String url6 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3600014478,753553017&fm=26&gp=0.jpg";
        String url7 = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2024758634,1516103747&fm=26&gp=0.jpg";
        String url8 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2334656353,474671771&fm=26&gp=0.jpg";
        return new ArrayList<String>(Arrays.asList(url1, url2, url3, url4, url5, url6, url7, url8));
    }

    public static String getConstellationDate(String time) {
        String timeStamp = "0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(time);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static String getRoomDate(String time) {
        String timeStamp = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
            date = dateFormat.parse(time);
            long dateTime = date.getTime();
            Log.i("TAG", "getRoomDate: " + dateTime);
            timeStamp = String.valueOf(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static String getStringToDate(String time) {
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(time);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static String getDateToString(String time) {
        long lcc = Long.valueOf(time);
        Date d = new Date(lcc);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdr.format(d);
    }

//    public static String getMeesageTime(String time) {
//        long lcc = Long.valueOf(time);
//        Date d = new Date(lcc);
//        SimpleDateFormat sdr = new SimpleDateFormat("MM-dd");
//        return sdr.format(d);
//    }

//    public static String getVipTime(String time) {
//        long lcc = Long.valueOf(time);
//        Date d = new Date(lcc);
//        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
//        return sdr.format(d);
//    }

    public static String getMessageInfoTime(long time) {
        DateFormat oldDay = new SimpleDateFormat("dd");
        DateFormat newDay = new SimpleDateFormat("dd");
        DateFormat dfHour = new SimpleDateFormat("a h:mm");
        DateFormat dfDay = new SimpleDateFormat("MM月dd日 a H:mm");
        try {
            Date cur = new Date();
            Date chatTime = new Date(time);
            long diff = cur.getTime() - chatTime.getTime();
            if (diff / (1000 * 60 * 60 * 24) >= 1) {
                return dfDay.format(chatTime);
            } else {
                if (Integer.parseInt(oldDay.format(chatTime)) < Integer.parseInt(newDay.format(new Date()))) {
                    return dfDay.format(chatTime);
                } else {
                    return dfHour.format(chatTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return dfHour.format(new Date());
        }
    }

    public static String getMessageTime(long time) {
        DateFormat oldDay = new SimpleDateFormat("dd");
        DateFormat newDay = new SimpleDateFormat("dd");
        DateFormat dfHour = new SimpleDateFormat("a h:mm");
        DateFormat dfDay = new SimpleDateFormat("MM月dd日");
        try {
            Date cur = new Date();
            Date chatTime = new Date(time);
            long diff = cur.getTime() - chatTime.getTime();
            if (diff / (1000 * 60 * 60 * 24) >= 1) {
                return dfDay.format(chatTime);
            } else {
                if (Integer.parseInt(oldDay.format(chatTime)) < Integer.parseInt(newDay.format(new Date()))) {
                    return dfDay.format(chatTime);
                } else {
                    return dfHour.format(chatTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return dfHour.format(new Date());
        }
    }

    public static String getDuration(Context context, String rel_time, String now_time) {

        if (TextUtils.isEmpty(now_time)) {
            if (!TextUtils.isEmpty(rel_time)) {
                String showTime = rel_time.substring(0, rel_time.lastIndexOf(":"));

                return showTime;
            }

            return "时间错误";
        }

        String backStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(rel_time);
            d2 = format.parse(now_time);
            // 毫秒ms
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays != 0) {
                if (diffDays < 30) {
                    if (1 < diffDays && diffDays < 2) {
                        backStr = context.getString(R.string.yesterday);
                    } else if (1 < diffDays && diffDays < 2) {
                        backStr = context.getString(R.string.The_day_before_yesterday);
                    } else {
                        backStr = String.valueOf(diffDays) + context.getString(R.string.Days_ago);
                    }
                } else {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date chatTime = null;
                    try {
                        chatTime = df.parse(rel_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    backStr = df.format(chatTime);
//                    backStr = context.getString(R.string.long_long_ago);
                }
            } else if (diffHours != 0) {
                backStr = String.valueOf(diffHours) + context.getString(R.string.An_hour_ago);

            } else if (diffMinutes != 0) {
                backStr = String.valueOf(diffMinutes) + context.getString(R.string.minutes_ago);

            } else {
                backStr = context.getString(R.string.just);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return backStr;

    }

    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 提示用户去开启定位服务
     **/
    public static void toOpenGPS(final Activity activity, final int requestCode) {
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage("手机定位服务未开启，无法获取到您的准确位置信息，是否前往开启？")
                .setNegativeButton("取消", null)
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivityForResult(intent, requestCode);
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

//    /**
//     * wifi是否连接
//     *
//     * @return
//     */
//    public static boolean isWifiEnabled(Context context) {
//        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
//            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            return wifiInfo.isConnected();
//        } else {
//            return false;
//        }
//    }

    /**
     * 获取wifi信息
     */
    @SuppressLint("MissingPermission")
    public static String obtainWifiInfo(Context context) {
        //显示扫描到的所有wifi信息
        WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();

        int strength = wi.getRssi();
        int speed = wi.getLinkSpeed();
        String designation = wi.getSSID();

        String addr = wi.getBSSID();
        String unit = WifiInfo.LINK_SPEED_UNITS;

        if (wm.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            StringBuilder listinfo = new StringBuilder();
            //搜索到的wifi列表信息
            List<ScanResult> scanResults = wm.getScanResults();
//            LogUtil.i(TAG, "obtainListInfo: scanResults.size() " + scanResults.size());
            for (ScanResult sr : scanResults) {
                listinfo.append("wifi网络ID：");
                listinfo.append(sr.SSID);
                listinfo.append("\nwifi MAC地址：");
                listinfo.append(sr.BSSID);
                listinfo.append("\nwifi信号强度：");
                listinfo.append(sr.level + "\n\n");
            }

            String curr_connected_wifi = "wifi网络ID ：" + designation +
                    "\nwifi信号强度: " + strength +
                    "\nwifi MAC地址: " + addr +
                    "\nwifi网速: " + speed + " " + unit;
            return designation;
//            getWifiListener(designation);
//            wifiInfo.setText("当前wifi信息:\n\n" + curr_connected_wifi + "\n\n\nwifiList:\n\n" + listinfo.toString());
        }
        return null;
    }

    public static void hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        IBinder b = null;
        if (view != null) b = view.getWindowToken();
        imm.hideSoftInputFromWindow(b, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftInput(Activity activity, EditText mEditText) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText.clearFocus();
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static void showSoftInput(Activity activity, EditText mEditText) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText.requestFocus();
        inputMethodManager.showSoftInput(mEditText, 0);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourcesId);
        return height;
    }

    public static boolean isBlank(String s) {
        return (s == null || s.equals("") || s.equals("null"));
    }

    public static boolean isBlank(Object o) {
        return (o == null);
    }

    //解决小米手机上获取图片路径为null的情况
    public static Uri getPictureUri(android.content.Intent intent, Activity activity) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = activity.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static class Formatter {

        public static String formatTime(int ms) {
            int totalSeconds = (ms + 500) / 1000;//四舍五入
            int seconds = totalSeconds % 60;
            int minutes = totalSeconds / 60 % 60;
            int hours = totalSeconds / 60 / 60;
            String timeStr = "";
            if (hours > 9) {
                timeStr += hours + ":";
            } else if (hours > 0) {
                timeStr += "0" + hours + ":";
            }
            if (minutes > 9) {
                timeStr += minutes + ":";
            } else if (minutes > 0) {
                timeStr += "0" + minutes + ":";
            } else {
                timeStr += "00:";
            }
            if (seconds > 9) {
                timeStr += seconds;
            } else if (seconds > 0) {
                timeStr += "0" + seconds;
            } else {
                timeStr += "00";
            }

            return timeStr;
        }


        public String formatDate(long seconds) {
            String finalStr = "";
            long mills = seconds * 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mills);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            finalStr += (hour < 10 ? "0" + hour : hour) + ":";
            int minute = calendar.get(Calendar.MINUTE);
            finalStr += (minute < 10 ? "0" + minute : minute) + ":";
            int second = calendar.get(Calendar.SECOND);
            finalStr += (second < 10 ? "0" + second : second);

            return finalStr;

        }
    }

}
