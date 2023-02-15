package org.zhr.utils;

public class StringUtils {
    public String smallHumpToUnderline(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(s.charAt(0)).toLowerCase());
        for (int i = 1;i < s.length();i ++) {
            if (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') {
                stringBuilder.append('_');
                stringBuilder.append(String.valueOf(s.charAt(i)).toLowerCase());
            } else {
                stringBuilder.append(s.charAt(i));
            }
        }
        return stringBuilder.toString();
    }
    public String underLineToSmallHump(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(s.charAt(0)).toLowerCase());
        for (int i = 1;i < s.length();i ++) {
            if (s.charAt(i) == '_') {
               i ++;
               stringBuilder.append(String.valueOf(s.charAt(i)).toUpperCase());
            } else
                stringBuilder.append(s.charAt(i));
        }
        return stringBuilder.toString();
    }
}
