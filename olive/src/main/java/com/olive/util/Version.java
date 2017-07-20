package com.olive.util;


import com.biz.util.StringUtils;

public class Version implements Comparable<Version> {

    private Long[] versionArray;

    public Version(String version) {

        if (StringUtils.isBlank(version)) {
            throw new IllegalArgumentException("Argument version can not be blank.");
        }
        String[] stringVersionArrays = version.split("\\.");
        versionArray = new Long[stringVersionArrays.length];
        for (int i = 0; i < stringVersionArrays.length; i++) {
            versionArray[i] = Long.valueOf(stringVersionArrays[i]);
        }
    }

    /**
     * compareTo Version
     * 返回-1 就是Version大于本身
     * 返回0 就是相等
     * 返回1 就是本身大于Version
     *
     * @param other
     * @return
     */

    public int compareTo(Version other) {

        Long[] o2Versions = other.versionArray;
        int versionLength = versionArray.length;
        if (versionLength != o2Versions.length) {
            throw new IllegalArgumentException("o1 and o2 version pattern not matched.");
        }
        for (int i = 0; i < versionLength; i++) {
            if (versionArray[i].equals(o2Versions[i])) {
                continue;
            }
            return versionArray[i].compareTo(o2Versions[i]);
        }
        return 0;
    }

}
