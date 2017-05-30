package com.fmcg.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yy");
    public static SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat un_sdf_format = new SimpleDateFormat("dd-MM-yy");
    public static SimpleDateFormat un_sdf_format_s = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat un_sdf_format_month = new SimpleDateFormat("MMM yy");
    public static SimpleDateFormat un_sdf_format_time = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    //    public static SimpleDateFormat sdf_format_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String orderNUmberString;
    public static String orderNUm;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static ProgressDialog loadingProgressDialog;

    public static void toastMessage(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static boolean checkSpecialChars(String str) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.find();
    }


    public static String stringValidation(String text) {
        String result = null;
        if (text != null && !text.equalsIgnoreCase("") && !text.equalsIgnoreCase("null") && checkString(text).length() > 0)
            result = text;
        else
            result = "";
        return result;
    }

    public static String convertFormat(String date, SimpleDateFormat sdf1, SimpleDateFormat sdf2) throws Exception {
        return sdf2.format(sdf1.parse(date));
    }

    public static String checkString(String str) {
        return str.replaceAll("\\s", "");
    }

    public static Date string2Date(String s, SimpleDateFormat simpleDateFormat)
            throws Exception {
        return simpleDateFormat.parse(s);
    }

    public static String date2String(Date date, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(date);
    }

    public static int getYears(String birth, SimpleDateFormat sdf) throws ParseException {
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        Date birthDate = sdf.parse(birth);
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());
        int years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        return years;
    }

    public static long getDateDiff(final String date, final String currentDate, SimpleDateFormat simpleDateFormat) throws ParseException {
        long ONE_DAY = 1000 * 60 * 60 * 24;
//        DateFormat formatter ;
        Date oldDate;
        Date curDate;
//        formatter = new SimpleDateFormat("yyyy-MM-dd");
        oldDate = (Date) simpleDateFormat.parse(date);
        curDate = (Date) simpleDateFormat.parse(currentDate);
        long oldMillis = oldDate.getTime();
        long curMillis = curDate.getTime();
        long diff = Math.abs(curMillis - oldMillis);

        return Math.abs(diff / ONE_DAY);
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONArray(test);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return newBitmap;
    }


    public static void showDialog(Context context) {
        if (loadingProgressDialog != null)
            loadingProgressDialog.dismiss();
        loadingProgressDialog = ProgressDialog.show(context, "", "Loading ...", true, false);
    }

    public static void disMissDialog() {
        if (loadingProgressDialog != null) {
            loadingProgressDialog.dismiss();
            loadingProgressDialog = null;
        }
    }

    public static Bitmap getBitmap(Bitmap userBitmap, int size) {
        int targetWidth = size;
        int targetHeight = size;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        BitmapShader shader;
        shader = new BitmapShader(userBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setStyle(Paint.Style.STROKE);
        canvas.clipPath(path);
        Bitmap sourceBitmap = userBitmap;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public static StringBuilder timeAndDateDiff(String start, String end, SimpleDateFormat simpleDateFormat) {
        StringBuilder builder = new StringBuilder();
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = simpleDateFormat.parse(start);
            d2 = simpleDateFormat.parse(end);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            builder.append(diffDays + ",");
            builder.append(diffHours + ",");
            builder.append(diffMinutes + ",");
            builder.append(diffSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;

    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static long getHoursDiff(final String date, final String currentDate, SimpleDateFormat simpleDateFormat) throws ParseException {
//        long ONE_DAY = 1000 * 60 * 60 * 24;
        long ONE_DAY = (60 * 60 * 1000) % 24;
//        DateFormat formatter ;
        Date oldDate;
        Date curDate;
//        formatter = new SimpleDateFormat("yyyy-MM-dd");
        oldDate = (Date) simpleDateFormat.parse(date);
        curDate = (Date) simpleDateFormat.parse(currentDate);
        long oldMillis = oldDate.getTime();
        long curMillis = curDate.getTime();
        long diff = Math.abs(curMillis - oldMillis);

        return Math.abs(diff / ONE_DAY);
    }

    public static String calendar2String(Calendar calendar,
                                         SimpleDateFormat simpleDateFormat) {

        return simpleDateFormat.format(calendar.getTime());
    }


    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }

    public static byte[] getBytes(java.io.InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network Testing", "***Available***");
            return true;
        }
        Log.e("Network Testing", "***Not Available***");
        return false;
    }

    public static Calendar dateString2Calendar(String s, SimpleDateFormat simpleDateFormat) {
        Calendar cal = null;
        Date d1 = null;
        try {
            cal = Calendar.getInstance();
            d1 = simpleDateFormat.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cal.setTime(d1);
        return cal;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static String urlEncoder(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, "UTF-8");
    }

    public static String urlDecoder(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, "UTF-8");
    }
}
