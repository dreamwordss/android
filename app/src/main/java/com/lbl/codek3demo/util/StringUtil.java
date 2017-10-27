package com.lbl.codek3demo.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.text.TextPaint;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static final String EMPTY = "";

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATETIME_PATTERN_TWO = "yyyy.MM.dd HH:mm";

    /**
     * 用于生成文件
     */
    private static final String DEFAULT_FILE_PATTERN = "yyyy-MM-dd-HH-mm-ss";
    private static final double KB = 1024.0;
    private static final double MB = 1048576.0;
    private static final double GB = 1073741824.0;
    public static final SimpleDateFormat DATE_FORMAT_PART = new SimpleDateFormat(
            "HH:mm");

    private static final long MINUTE = 60;
    private static final long HOUR = 60 * 60;
    public static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 30 * DAY;
    private static final long YEAR = 365 * DAY;
    private static final long MIN_DURATION = 5 * MINUTE;

    public static String getDateString(long seconds) {
        long curSecond = System.currentTimeMillis() / 1000;
        long duration = curSecond - seconds;
        if (duration <= 0) {
            return "刚刚";
        } else if (duration < MIN_DURATION) {
            return "刚刚";
        } else if (duration < HOUR) {
            return duration / MINUTE + "分钟前";
        } else if (duration < DAY) {
            return duration / HOUR + "小时前";
        } else if (duration < WEEK) {
            return duration / DAY + "天前";
        } else if (duration < MONTH) {
            return duration / WEEK + "周前";
        } else if (duration < YEAR) {
            return duration / MONTH + "月前";
        } else {
            return duration / YEAR + "年前";
        }
    }


    public static String getDateString(String seconds) {
        if (seconds == null)
            return "";
        Long longSeconds = Long.valueOf(seconds);
        if (longSeconds == null)
            return "";
        return getDateString(longSeconds);
    }

    /**
     * 日期显示新规则：
     * 1.今天 只显示时间
     * 2.昨天 显示昨天 + 时间
     * 3.其他 只显示日期
     *
     * @return
     */
    /**
     * 格式化时间
     *
     * @param times
     * @return
     */
    public static String getDate(long times) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = format.format(times * 1000);
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天 " + time.split(" ")[1];
        } else {
            return time.split(" ")[0];
        }
    }


    /**
     * 判断字符串是否为空或空串
     *
     * @param str 待判断的字符串
     * @return true：字符串为空或空串
     */
    public static boolean isNull(final String str) {
        if (null == str || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取字符串长度（半角算1、全角算2）
     *
     * @param str 字符串
     * @return 字符串长度
     */
    public static int getLength(final String str) {
        if (isNull(str)) {
            return 0;
        }
        int len = str.length();
        for (int i = 0; i < str.length(); i++) {
            if (isFullwidthCharacter(str.charAt(i))) {
                len = len + 1;
            }
        }
        return len;
    }

    /**
     * 获取字符串的全角字符数
     *
     * @param str 待计算的字符串
     * @return 字符串的全角字符数
     */
    public static int getFwCharNum(final String str) {
        if (isNull(str)) {
            return 0;
        }
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isFullwidthCharacter(str.charAt(i))) {
                num++;
            }
        }
        return num;
    }

    /**
     * 判断字符是否为全角字符
     *
     * @param ch 待判断的字符
     * @return true：全角； false：半角
     */
    public static boolean isFullwidthCharacter(final char ch) {
        if (ch >= 32 && ch <= 127) {
            // 基本拉丁字母（即键盘上可见的，空格、数字、字母、符号）
            return false;
        } else if (ch >= 65377 && ch <= 65439) {
            // 日文半角片假名和符号
            return false;
        } else {
            return true;
        }
    }

    // 判断电话号码格式
    public static boolean isPhoneNumber(String number) {
        if (number == null)
            return false;
        if (number.length() == 11) {
            if (number.startsWith("1")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] src;
        try {
            src = string.getBytes("UTF-8");
            return getMD5(src);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMD5(byte[] src) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(src);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    public static boolean isSameDay(String str1, String str2) {

        if (str1 == null || str2 == null || str1.isEmpty() || str2.isEmpty()) {
            return false;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getDefault());

        long time1, time2;
        try {
            time1 = Long.valueOf(str1) * 1000;
            time2 = Long.valueOf(str2) * 1000;
        } catch (Exception e) {
            return false;
        }

        Date date1 = new Date(time1);
        String date1Str = format.format(date1);

        Date date2 = new Date(time2);
        String date2Str = format.format(date2);

        if (date1Str != null && date2Str != null
                && date1Str.equalsIgnoreCase(date2Str)) {
            return true;
        }

        return false;
    }

    public static String currentTimeString() {
        return DATE_FORMAT_PART.format(Calendar.getInstance().getTime());
    }

    public static char chatAt(String pinyin, int index) {
        if (pinyin != null && pinyin.length() > 0)
            return pinyin.charAt(index);
        return ' ';
    }

    /**
     * 获取字符串宽度
     */
    public static float GetTextWidth(String Sentence, float Size) {
        if (isEmpty(Sentence))
            return 0;
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(Sentence.trim()) + (int) (Size * 0.1); // 留点余地
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String formatDate(long date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(date));
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @return 例如2011-3-24
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_PATTERN);
    }

    public static String formatDate(long date) {
        return formatDate(new Date(date), DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取当前时间 格式为yyyy-MM-dd 例如2011-07-08
     *
     * @return
     */
    public static String getDate() {
        return formatDate(new Date(), DEFAULT_DATE_PATTERN);
    }

    /**
     * 生成一个文件名，不含后缀
     */
    public static String createFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FILE_PATTERN);
        return format.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getDateTime() {
        return formatDate(new Date(), DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 格式化日期时间字符串
     *
     * @param date
     * @return 例如2011-11-30 16:06:54
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DEFAULT_DATETIME_PATTERN);
    }

    public static String formatDateTime(long date) {
        return formatDate(new Date(date), DEFAULT_DATETIME_PATTERN);
    }

    public static String formatDateTimeNew(long date) {
        return formatDate(new Date(date), DEFAULT_DATETIME_PATTERN_TWO);
    }

    /**
     * 格林威时间转换
     *
     * @param gmt
     * @return
     */
    public static String formatGMTDate(String gmt) {
        TimeZone timeZoneLondon = TimeZone.getTimeZone(gmt);
        return formatDate(Calendar.getInstance(timeZoneLondon)
                .getTimeInMillis());
    }

    /**
     * 拼接数组
     *
     * @param array
     * @param separator
     * @return
     */
    public static String join(final ArrayList<String> array,
                              final String separator) {
        StringBuffer result = new StringBuffer();
        if (array != null && array.size() > 0) {
            for (String str : array) {
                result.append(str);
                result.append(separator);
            }
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    public static String join(final Iterator<String> iter,
                              final String separator) {
        StringBuffer result = new StringBuffer();
        if (iter != null) {
            while (iter.hasNext()) {
                String key = iter.next();
                result.append(key);
                result.append(separator);
            }
            if (result.length() > 0)
                result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str == null ? EMPTY : str.trim();
    }

    /**
     * 转换时间显示
     *
     * @param time 毫秒
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes,
                seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    public static boolean isBlank(String s) {
        return TextUtils.isEmpty(s);
    }

    /**
     * 添加:如果多个空格也是空字符串
     *
     * @param s
     * @return
     */
    public static boolean isBlankCaseTrim(String s) {
        return !(!TextUtils.isEmpty(s) && !TextUtils.isEmpty(s.trim()));
    }

    /**
     * 根据秒速获取时间格式
     */
    public static String gennerTime(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 根据秒速获取时间格式
     */
    public static String gennerTime(double totalSeconds) {
        int total = (int) totalSeconds;
        int seconds = total % 60;
        int minutes = (total / 60) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


    /**
     * 转换文件大小
     *
     * @param size
     * @return
     */
    public static String generateFileSize(long size) {
        String fileSize;
        if (size < KB)
            fileSize = size + "B";
        else if (size < MB)
            fileSize = String.format("%.1f", size / KB) + "KB";
        else if (size < GB)
            fileSize = String.format("%.1f", size / MB) + "MB";
        else
            fileSize = String.format("%.1f", size / GB) + "GB";

        return fileSize;
    }

    /**
     * 查找字符串，找到返回，没找到返回空
     */
    public static String findString(String search, String start, String end) {
        int start_len = start.length();
        int start_pos = StringUtil.isEmpty(start) ? 0 : search.indexOf(start);
        if (start_pos > -1) {
            int end_pos = StringUtil.isEmpty(end) ? -1 : search.indexOf(end,
                    start_pos + start_len);
            if (end_pos > -1)
                return search.substring(start_pos + start.length(), end_pos);
        }
        return "";
    }

    /**
     * 截取字符串
     *
     * @param search       待搜索的字符串
     * @param start        起始字符串 例如：<title>
     * @param end          结束字符串 例如：</title>
     * @param defaultValue
     * @return
     */
    public static String substring(String search, String start, String end,
                                   String defaultValue) {
        int start_len = start.length();
        int start_pos = StringUtil.isEmpty(start) ? 0 : search.indexOf(start);
        if (start_pos > -1) {
            int end_pos = StringUtil.isEmpty(end) ? -1 : search.indexOf(end,
                    start_pos + start_len);
            if (end_pos > -1)
                return search.substring(start_pos + start.length(), end_pos);
            else
                return search.substring(start_pos + start.length());
        }
        return defaultValue;
    }

    /**
     * 截取字符串
     *
     * @param search 待搜索的字符串
     * @param start  起始字符串 例如：<title>
     * @param end    结束字符串 例如：</title>
     * @return
     */
    public static String substring(String search, String start, String end) {
        return substring(search, start, end, "");
    }

    /**
     * 拼接字符串
     *
     * @param strs
     * @return
     */
    public static String concat(String... strs) {
        StringBuffer result = new StringBuffer();
        if (strs != null) {
            for (String str : strs) {
                if (str != null)
                    result.append(str);
            }
        }
        return result.toString();
    }

    /**
     * Helper function for making null strings safe for comparisons, etc.
     *
     * @return (s == null) ? "" : s;
     */
    public static String makeSafe(String s) {
        return (s == null) ? "" : s;
    }


    public static String stringFromFile(String filePath) {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.append(readString);
                readString = buffreader.readLine();
            }

            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return datax.toString();

    }

    /**
     * @param original
     * @return false 不为空 true 为空
     * @Title: isStringEmpty
     * @Description: TODO 判断字符串是否为空
     */
    public static boolean isStringEmpty(String original) {
        if (original != null && !"".equals(original.trim())
                && !"null".equals(original.toLowerCase()))
            return false;
        else
            return true;
    }

    public static String getCorrectString(String inputString) {
        if (TextUtils.isEmpty(inputString)) {
            return "";
        }
        return inputString;
    }


    /**
     * 从字符串中截取连续6位数字
     * 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicCode(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 6) {
                dynamicPassword = m.group();
            }
        }
        return dynamicPassword;
    }


    /**
     * 超过5位数 显示万
     *
     * @param like_count    点赞数量
     * @param defaultString 默认小于0的时候 显示的文字
     * @return
     */
    public static String getFormatCount(int like_count, String defaultString) {
        if (like_count > 10000) {
            DecimalFormat decimalFormat = new DecimalFormat(".#");
            double c = Double.parseDouble(decimalFormat.format((double) like_count / 10000));
            return c + "万";
        } else if (like_count <= 0) {
            return defaultString;
        } else {
            return String.valueOf(like_count);
        }
    }

    /**
     * 描述 里边的标签跳转
     *
     * @param words
     * @return 不为空就可以跳转
     */


    /**
     * @param context
     * @param resId
     * @return
     */
    public static String[] getResStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }


    /**
     * 从一个字符串数组中随机取一个字符串
     *
     * @param array 字符串数组
     * @return 字符串
     */
    public static String getRandomString(String[] array) {
        if (array != null) {
            int rnd = new Random().nextInt(array.length);
            return array[rnd];
        }
        return "";
    }

    /**
     * 根据时间戳显示发布时间
     *
     * @param time 发布时间
     * @return
     */
    public static String getFormatTime(long time) {
        String dateStr;
        long currentTime = new Date().getTime();
        long dexTime = currentTime - time;
        Double min = dexTime * 0.001 / 60;
        if (min < 5) {
            dateStr = "刚刚";
        } else if (min < 60) {
            // 多少分前
            dateStr = min.intValue() + "分钟前";
        } else if (min < 60 * 24) {
            // 几小时前
            dateStr = (int) (min / 60) + "小时前";
        } else if (min < 60 * 24 * 7) {
            // 几天前
            dateStr = (int) (min / (60 * 24)) + "天前";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateStr = simpleDateFormat.format(new Date(time));
        }
        return dateStr;
    }


    /**
     * 转换时间格式(mm:ss) eg.120--02:00
     *
     * @param time 单位s
     * @return
     */
    public static String convertTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(new Date(time * 1000));
    }

    public static String getTrackUrl(String url, String trackInfo) {
        if (!TextUtils.isEmpty(trackInfo)) {
            if (url.contains("?")) {
                try {
                    url = url + "&track_info=" + URLEncoder.encode(trackInfo, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    url = url + "?track_info=" + URLEncoder.encode(trackInfo, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url;
    }

    /**
     * 格式化手机号
     *
     * @param inputPhone
     * @return
     */

    public static String formatPhoneStyle(String inputPhone) {
        if (!TextUtils.isEmpty(inputPhone)) {
            if (inputPhone.length() > 8) {
                StringBuilder phoneNumberStringBuilder = new StringBuilder(inputPhone).insert(3, " ").insert(8, " ");
                return phoneNumberStringBuilder.toString();
            } else {
                throw new RuntimeException("电话号码错误");
            }
        }
        return "";
    }

    /**
     * 去格式化
     *
     * @param inputPhone
     * @return
     */
    public static String delFormatPhoneStyle(String inputPhone) {
        if (!TextUtils.isEmpty(inputPhone)) {
            return inputPhone.replaceAll(" ", "");
        }
        return "";
    }

    /**
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return (str == null || str.equals("")) ? false : true;
    }

    /**
     * 获取颜色数组
     *
     * @param context
     * @param colorRes
     * @return
     */
    public static int[] getColorArray(Context context, @ArrayRes int colorRes) {
        TypedArray typedArray = context.getResources().obtainTypedArray(colorRes);
        int[] textColors = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            textColors[i] = typedArray.getColor(i, 0);
        }
        typedArray.recycle();
        return textColors;
    }

    /**
     * 获取名字数组
     *
     * @param context
     * @return
     */
    public static int[] getThemeNameArray(Context context, @ArrayRes int nameRes) {
        TypedArray typedArray = context.getResources().obtainTypedArray(nameRes);
        int[] textName = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            textName[i] = typedArray.getIndex(i);
        }
        typedArray.recycle();
        return textName;
    }

    //时间标记
    public static String getTimeFolderName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日hh:mm");
        long currentTimeMillis = System.currentTimeMillis();
        return sdf.format(new Date(currentTimeMillis));
    }

    //格式化价格
    public static String getFormatPrice(String originalPrice) {
        if (TextUtils.isEmpty(originalPrice)) {
            return "";
        }
        int afterPointIndex = originalPrice.indexOf(".");
        if (afterPointIndex == -1) {
            return String.valueOf(originalPrice);
        } else {
            int afterPointLength = originalPrice.length() - 1 - afterPointIndex;
            if (afterPointLength == 1) {
                Double aDouble = Double.valueOf(originalPrice);
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String format = decimalFormat.format(aDouble);
                if (format.endsWith(".0")) {
                    return String.valueOf(aDouble.intValue());
                }
                return format;
            }
        }
        Double aDouble = Double.valueOf(originalPrice);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String format = decimalFormat.format(aDouble);
        if (format.endsWith(".00")) {
            return String.valueOf(aDouble.intValue());
        }
        return format;
    }

}
