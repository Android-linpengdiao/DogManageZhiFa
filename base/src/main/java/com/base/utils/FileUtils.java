package com.base.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import android.util.Log;

import com.base.BaseApplication;
import com.base.R;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FileUtils {

    public static String getPath() {
        return BaseApplication.getInstance().getExternalFilesDir(null) + File.separator;
    }

    public static String getChatPath() {
        new File(getPath() + "chat").mkdirs();
        return getPath() + "chat" + File.separator;
    }

    public static String getTempPath() {
        new File(getPath() + "temp").mkdirs();
        return getPath() + "temp" + File.separator;
    }

    public static String getMediaPath() {
        new File(getPath() + "media").mkdirs();
        return getPath() + "media" + File.separator;
    }

    public static String getAppPath() {
        return Environment.getExternalStorageDirectory() + File.separator +
                BaseApplication.getInstance().getResources().getString(R.string.app_name) + File.separator;
    }

    // 创建一个附件文件
    public static File createMediaFile(String mediaPath) {
        File mediaFile = new File(mediaPath);
        File directory = mediaFile.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
        }
        try {
            if (!mediaFile.exists()) {
                mediaFile.createNewFile();
            } else {
                mediaFile.delete();
                mediaFile.createNewFile();
            }
            return mediaFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File createTempFile(String fileName) {
        if (!CommonUtil.isBlank(fileName)) {
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(getTempPath() + fileName);
                    file.getParentFile().mkdirs();
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    return file;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    public static String DownLoadSound(Context context, String url, String fileName, String soundName) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 创建文件
            File soundFile = new File(fileName);
            File directory = soundFile.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
            }
            try {
                if (!soundFile.exists()) {
                    soundFile.createNewFile();
                } else {
                    soundFile.delete();
                    soundFile.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort(context, "下载失败");
                return null;
            }
            // 下载文件
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15 * 1000);
            conn.setReadTimeout(30 * 1000);

            is = conn.getInputStream();
            os = new FileOutputStream(soundFile);

            byte[] bs = new byte[1024];
            int len;
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 插入图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), soundFile.getAbsolutePath(), soundName, null);
            // 发送广播，通知刷新图库的显示
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
            ToastUtils.showShort(context, "图片已保存至" + fileName + "文件夹");

            return soundFile.getPath();

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(context, "下载失败");
            return null;
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static boolean DownLoadFile(Context context, String url, String fileName, DownloadListener downloadListener) {
        InputStream is = null;
        OutputStream os = null;
        try {
            File soundFile = new File(fileName);
            File directory = soundFile.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
            }
            try {
                if (!soundFile.exists()) {
                    soundFile.createNewFile();
                } else {
                    soundFile.delete();
                    soundFile.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                ToastUtils.showShort(context, "下载失败");
                downloadListener.onDownloadFailed();
                return false;
            }
            // 下载文件
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15 * 1000);
            conn.setReadTimeout(30 * 1000);

            is = conn.getInputStream();
            os = new FileOutputStream(soundFile);

            byte[] bs = new byte[1024];
            int len;
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
                downloadListener.onDownloading();
            }
            // 插入图库 8-26 注释掉.原因:保存在本地库了.没不要在 媒体库再存一次消息
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), soundFile.getAbsolutePath(), soundName, null);
            // 发送广播，通知刷新图库的显示
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
            downloadListener.onDownloadSuccess(fileName);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
//            ToastUtils.showShort(context, "下载失败");
            downloadListener.onDownloadFailed();
            return false;
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public interface DownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);

        /**
         *
         */
        void onDownloading();

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

    public static String saveBitmap(Bitmap b) {

        File file = null;
        try {

            String jpegName = System.currentTimeMillis() + "_cover.jpg";
            file = new File(getAppPath() + jpegName);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static String saveFirstFrameBitmap(Bitmap bitmap) {
        File file = null;
        try {
            String jpegName = "first_frame_" + System.currentTimeMillis() + ".jpg";
            file = new File(getTempPath() + jpegName);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static String saveBitmap(Bitmap bitmap, int width, int height, int option) {

        String jpegName = System.currentTimeMillis() + "_cover.jpg";
        File jpegPath = createTempFile(jpegName);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = BitmapUtils.calculateInSampleSize(bitmap, width, height);
            options.inJustDecodeBounds = false;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            int inSampleSize = 100;//个人喜欢从80开始,
            bitmap.compress(Bitmap.CompressFormat.JPEG, inSampleSize, baos);
            while (baos.toByteArray().length / 1024 > option) {
                baos.reset();
                inSampleSize -= 5;
                if (inSampleSize < 0) {
                    inSampleSize = 5;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, inSampleSize, baos);
                    break;
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, inSampleSize, baos);
            }

            FileOutputStream fos = new FileOutputStream(jpegPath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.flush();
            baos.close();
            return jpegPath.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void compressBmpToFile(Bitmap bmp, File file, int option) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > option) {
            baos.reset();
            options -= 5;
            if (options < 0) {
                options = 5;
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            bmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建本地视频缩列图
    public static File createVideoThumbnailFile(File file) {
        String fileName = file.getName().split("\\.")[0] + "_Thumbnail.jpg";
        File compressFile = createAttachmentFile(fileName);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
        try {
            FileOutputStream fos = new FileOutputStream(compressFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressFile;
    }

    // 创建一个附件文件
    public static File createAttachmentFile(String fileName) {
        String path = getChatPath() + fileName;
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new File("");
        }
        return file;
    }

    //创建网络视频缩列图
    public static String createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        try {
            String videoName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            String videoPath = getPath() + videoName + ".jpg";
            File file = new File(videoPath);
            file.getParentFile().mkdirs();
            try {
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    file.delete();
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(videoPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                bitmap.recycle();
                return videoPath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getTempFileName(String picPath) {
        String fileName = picPath.substring(picPath.lastIndexOf("/") + 1);
        File file = FileUtils.createTempFile(fileName);
        return file;
    }

    public static Bitmap getLoacalBitmap(String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean copyFolder(String oldPath, String newPath) {
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                if (!newFile.mkdirs()) {
                    Log.e("--Method--", "copyFolder: cannot create directory.");
                    return false;
                }
            }
            File oldFile = new File(oldPath);
            String[] files = oldFile.list();
            File temp;
            for (String file : files) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file);
                } else {
                    temp = new File(oldPath + File.separator + file);
                }

                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (!temp.exists()) {
                    Log.e("--Method--", "copyFolder:  oldFile not exist.");
                    return false;
                } else if (!temp.isFile()) {
                    Log.e("--Method--", "copyFolder:  oldFile not file.");
                    return false;
                } else if (!temp.canRead()) {
                    Log.e("--Method--", "copyFolder:  oldFile cannot read.");
                    return false;
                } else {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }

            /* 如果不需要打log，可以使用下面的语句
            if (temp.isDirectory()) {   //如果是子文件夹
                copyFolder(oldPath + "/" + file, newPath + "/" + file);
            } else if (temp.exists() && temp.isFile() && temp.canRead()) {
                FileInputStream fileInputStream = new FileInputStream(temp);
                FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                byte[] buffer = new byte[1024];
                int byteRead;
                while ((byteRead = fileInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, byteRead);
                }
                fileInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            */
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 生成文件夹
    public static File makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        } catch (Exception e) {
            return new File("");
        }
    }

    public static void clearDownloadWidgetFile(String path) {
        if (clearIsFileExists(path)) {
            File file = new File(path);
            delete(file);
        } else {
        }
    }

    public static void clearFile() {
        if (clearIsFileExists(getPath())) {
            File file = new File(getPath());
            delete(file);
        } else {
        }
    }

    public static boolean clearIsFileExists(String path) {
        if (CommonUtil.isBlank(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    //分享多张图片
    public static void shareMultiImg(Activity activity, String dlgTitle, String filePath) {
        if (filePath == null) {
            return;
        }
        //分享单张图片
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", new File(filePath));
        } else {
            fileUri = Uri.fromFile(new File(filePath));
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setType("image/*");
        activity.startActivity(Intent.createChooser(shareIntent, dlgTitle));

//        //分享多张图片
//        ArrayList<Uri> imageUris = new ArrayList<>();
//        imageUris.add(uri);
//        imageUris.add(uri);
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//        shareIntent.setType("image/*");
//        startActivity(Intent.createChooser(shareIntent, dlgTitle));
    }

    public static void openFile(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.intent.action.VIEW");
            String type = getMIMEType(file);
            if (Build.VERSION.SDK_INT >= 24) {
                Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                intent.setDataAndType(fileUri, type);
                grantUriPermission(context, fileUri, intent);
            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }

            context.startActivity(intent);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    private static void grantUriPermission(Context context, Uri fileUri, Intent intent) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        Iterator var4 = resInfoList.iterator();

        while (var4.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo) var4.next();
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

    }

    private static final String[][] MIME_MapTable = new String[][]{{".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, {".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"}, {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"}, {"", "*/*"}};

    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        } else {
            String end = fName.substring(dotIndex, fName.length()).toLowerCase();
            if (end == "") {
                return type;
            } else {
                for (int i = 0; i < MIME_MapTable.length; ++i) {
                    if (end.equals(MIME_MapTable[i][0])) {
                        type = MIME_MapTable[i][1];
                    }
                }

                return type;
            }
        }
    }

}
