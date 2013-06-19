/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringsHelper {
    public static String getMD5(String input) {
        byte[] bytesOfMessage = new byte[0];
        try {
            bytesOfMessage = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] thedigest = null;
        if (md != null) {
            thedigest = md.digest(bytesOfMessage);
        }

        return md == null ? null : toHex(thedigest);
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static String makeFirstCapital(String input) {
        String first = input.substring(0, 1);
        String rest = input.substring(1);
        return first.toUpperCase() + rest;
    }
}
