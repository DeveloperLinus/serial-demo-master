package com.linus.common.utils;

import java.lang.Integer;
import java.nio.ByteBuffer;

public class Tools {
    public static int getInteger(byte[] buffer, int start, int len) {
        return Integer.valueOf(new String(buffer, start, len), 16);
    }

    public static void append(ByteBuffer buffer, int value, int len) {
        final String f = String.format("%s0%dx", "%", len);
        final String d = String.format(f, value);
        buffer.put(d.getBytes());
    }

    public static byte computer(byte[] d, int start, int length) throws Exception {
        if (start >= 0 && d != null && d.length >= start + length) {
            byte r = d[start];
            final int end = start + length;

            for (int i = start + 1; i < end; i++) {
                r ^= d[i];
            }
            return r;
        } else {
            throw new Exception("data error");
        }
    }
}
