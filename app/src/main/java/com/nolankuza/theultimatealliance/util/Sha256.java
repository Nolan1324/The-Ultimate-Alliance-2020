package com.nolankuza.theultimatealliance.util;

import java.security.*;
public class Sha256 {
    public static byte[] hash256(String data, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(concatBytes(data.getBytes(), salt));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] generateSalt(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[byteLength];
        random.nextBytes(bytes);
        return bytes;
    }

    private static byte[] concatBytes(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}