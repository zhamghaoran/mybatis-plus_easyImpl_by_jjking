package org.zhr.utils;

public class StringUtils {
    public String smallHumpToUnderline(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i < name.length();i ++) {
            if (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z') {
                stringBuilder.append('_');
                stringBuilder.append(String.valueOf(name.charAt(i)).toLowerCase());
            } else {
                stringBuilder.append(name.charAt(i));
            }
        }
        return stringBuilder.toString();
    }
    public String underLineToSmallHump(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i < s.length();i ++) {
            if (s.charAt(i) == '_') {
               i ++;
               stringBuilder.append(String.valueOf(s.charAt(i)).toUpperCase());
            } else
                stringBuilder.append(s.charAt(i));
        }
        return stringBuilder.toString();
    }
}
