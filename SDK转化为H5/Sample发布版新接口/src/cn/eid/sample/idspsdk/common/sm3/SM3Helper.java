package cn.eid.sample.idspsdk.common.sm3;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Arrays;



public class SM3Helper {
	
    public static char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                                   '9', 'A', 'B', 'C', 'D', 'E', 'F'
                                 };

    private static final String ivHexStr = "7380166f 4914b2b9 172442d7 da8a0600 a96f30bc 163138aa e38dee4d b0fb0e4e";
    public static final BigInteger IV = new BigInteger(
    		ivHexStr.replaceAll(" ",""), 
    		16);
    
    
    public static final Integer Tj15 = Integer.valueOf("79cc4519", 16);
    public static final Integer Tj63 = Integer.valueOf("7a879d8a", 16);
    public static final byte[] FirstPadding = { (byte) 0x80 };
    public static final byte[] ZeroPadding = { (byte) 0x00 };

    public static int T(int j) throws Exception {
        if (j >= 0 && j <= 15) {
            return Tj15.intValue();
        } else if (j >= 16 && j <= 63) {
            return Tj63.intValue();
        } else {
            throw new Exception();
        }
    }

    public static Integer FF(Integer x, Integer y, Integer z, int j)
    throws Exception {
        if (j >= 0 && j <= 15) {
            return Integer.valueOf(x.intValue() ^ y.intValue() ^ z.intValue());
        } else if (j >= 16 && j <= 63) {
            return Integer.valueOf((x.intValue() & y.intValue())
            | (x.intValue() & z.intValue())
            | (y.intValue() & z.intValue()));
        } else {
            throw new Exception();
        }
    }

    public static Integer GG(Integer x, Integer y, Integer z, int j)
    throws Exception {
        if (j >= 0 && j <= 15) {
            return Integer.valueOf(x.intValue() ^ y.intValue() ^ z.intValue());
        } else if (j >= 16 && j <= 63) {
            return Integer.valueOf((x.intValue() & y.intValue())
            | (~x.intValue() & z.intValue()));
        } else {
            throw new Exception();
        }
    }

    public static Integer P0(Integer x) throws Exception {
        return Integer.valueOf(x.intValue()
        ^ Integer.rotateLeft(x.intValue(), 9)
        ^ Integer.rotateLeft(x.intValue(), 17));
    }

    public static Integer P1(Integer x) throws Exception {
        return Integer.valueOf(x.intValue()
        ^ Integer.rotateLeft(x.intValue(), 15)
        ^ Integer.rotateLeft(x.intValue(), 23));
    }

    public static byte[] padding(byte[] source) throws Exception {
        if (source.length >= 0x2000000000000000l) {
            throw new Exception();
        }
        long l = source.length * 8;
        long k = 448 - (l + 1) % 512;
        if (k < 0) {
            k = k + 512;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(source);
        baos.write(FirstPadding);
        long i = k - 7;
        while (i > 0) {
            baos.write(ZeroPadding);
            i -= 8;
        }
        baos.write(long2bytes(l));
        return baos.toByteArray();
    }

    public static byte[] long2bytes(long l) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (l >>> ((7 - i) * 8));
        }
        return bytes;
    }

    public static byte[] hash(byte[] source) throws Exception {
    	
        byte[] m1 = padding(source);
        // printByteArray(m1, 16);
        int n = m1.length / (512 / 8);
        byte[] b = null;
        byte[] vi = IV.toByteArray();
        byte[] vi1 = null;
        for (int i = 0; i < n; i++) {
            b = Arrays.copyOfRange(m1, i * 64, (i + 1) * 64);
            vi1 = CF(vi, b);
            vi = vi1;
        }
        return vi1;
        
    }

    public static byte[] CF(byte[] vi, byte[] bi) throws Exception {
        int a, b, c, d, e, f, g, h;
        a = toInteger(vi, 0);
        b = toInteger(vi, 1);
        c = toInteger(vi, 2);
        d = toInteger(vi, 3);
        e = toInteger(vi, 4);
        f = toInteger(vi, 5);
        g = toInteger(vi, 6);
        h = toInteger(vi, 7);

        int[] w = new int[68];
        int[] w1 = new int[64];
        for (int i = 0; i < 16; i++) {
            w[i] = toInteger(bi, i);
        }
        for (int j = 16; j < 68; j++) {
            w[j] = P1(w[j - 16] ^ w[j - 9] ^ Integer.rotateLeft(w[j - 3], 15))
            ^ Integer.rotateLeft(w[j - 13], 7) ^ w[j - 6];
        }
        for (int j = 0; j < 64; j++) {
            w1[j] = w[j] ^ w[j + 4];
        }
        // printIntArray(w, 8);
        // printIntArray(w1, 8);
        int ss1, ss2, tt1, tt2;
        for (int j = 0; j < 64; j++) {
            ss1 = Integer
            .rotateLeft(
                Integer.rotateLeft(a, 12) + e
                + Integer.rotateLeft(T(j), j), 7);
            ss2 = ss1 ^ Integer.rotateLeft(a, 12);
            tt1 = FF(a, b, c, j) + d + ss2 + w1[j];
            tt2 = GG(e, f, g, j) + h + ss1 + w[j];
            d = c;
            c = Integer.rotateLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = Integer.rotateLeft(f, 19);
            f = e;
            e = P0(tt2);
        }
        byte[] v = toByteArray(a, b, c, d, e, f, g, h);
        for (int i = 0; i < v.length; i++) {
            v[i] = (byte) (v[i] ^ vi[i]);
        }
        return v;
    }

    public static int toInteger(byte[] source, int index) {
        StringBuilder valueStr = new StringBuilder("");
        for (int i = 0; i < 4; i++) {
            valueStr.append(chars[(byte) ((source[index * 4 + i] & 0xF0) >> 4)]);
            valueStr.append(chars[(byte) (source[index * 4 + i] & 0x0F)]);
        }
        return Long.valueOf(valueStr.toString(), 16).intValue();

    }

    public static byte[] toByteArray(int a, int b, int c, int d, int e, int f,
                                     int g, int h) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(32);
        baos.write(toByteArray(a));
        baos.write(toByteArray(b));
        baos.write(toByteArray(c));
        baos.write(toByteArray(d));
        baos.write(toByteArray(e));
        baos.write(toByteArray(f));
        baos.write(toByteArray(g));
        baos.write(toByteArray(h));
        return baos.toByteArray();
    }

    public static byte[] toByteArray(int i) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (i >>> 24);
        byteArray[1] = (byte) ((i & 0xFFFFFF) >>> 16);
        byteArray[2] = (byte) ((i & 0xFFFF) >>> 8);
        byteArray[3] = (byte) (i & 0xFF);
        return byteArray;
    }

    public static void printIntArray(int[] intArray, int lineSize) {
        for (int i = 0; i < intArray.length; i++) {
            byte[] byteArray = toByteArray(intArray[i]);
            int j = 0;
            while (j < byteArray.length) {
                System.out.print(chars[(byteArray[j] & 0xFF) >> 4]);
                System.out.print(chars[byteArray[j] & 0xF]);
                j++;
            }
            System.out.print(" ");
            if (i % lineSize == (lineSize - 1)) {
                System.out.println(" ");
            }
        }
    }

    public static void printByteArray(byte[] source, int lineLength) {
        System.out.println("start printByteArray()...");
        int i = 0;
        while (i < source.length) {
            System.out.print(chars[(source[i] & 0xFF) >> 4]);
            System.out.print(chars[source[i] & 0xF]);
            if (i % lineLength == 15) {
                System.out.println("");
            } else {
                System.out.print("-");
            }
            i++;
        }
        System.out.println("\nprintByteArray() end");
    }

}
