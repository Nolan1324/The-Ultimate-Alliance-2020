package com.nolankuza.theultimatealliance.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Binary {
    /* Data codes

    0x01 - Game data
    0x02 - Pit data
    0x03 - Event info / schedule
    0x04 - Team data
    0x05 - Settings

    */

    public static void writeBytes(OutputStream os, byte[] bytes) throws IOException {
        /*
        for(int i = 1; true; i++) {
            if(i * 1024 > bytes.length) {
                is.write(bytes, (i - 1) * 1024, bytes.length % 1024);
                break;
            } else {
                is.write(bytes, (i - 1) * 1024, 1024);
            }
        }
        */
        os.write(bytes);
    }

    public static byte[] readBytes(InputStream is, int length) throws IOException {
        byte[] buffer = new byte[length];
        int bytes = 0;

        do {
            try {
                if(bytes + 1024 >= length) {
                    if(bytes != 0) {
                        bytes += is.read(buffer, bytes, length % bytes);
                    } else {
                        bytes += is.read(buffer, bytes, length);
                    }
                } else {
                    bytes += is.read(buffer, bytes, 1024);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while(bytes < length);

        return buffer;
    }

    public static <T extends Parcelable> byte[] createSendData(List<T> objects, boolean isMore, int type) {
        byte[] listBytes = marshall(objects);
        ByteBuffer buffer = ByteBuffer.allocate(6 + listBytes.length);
        buffer.put((byte) (isMore ? 1 : 0));
        buffer.put((byte) type);
        buffer.putInt(listBytes.length);
        buffer.put(listBytes);

        return buffer.array();
    }

    public static byte[] marshall(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static <T extends Parcelable> byte[] marshall(List<T> parcelables) {
        Parcel parcel = Parcel.obtain();
        parcel.writeTypedList(parcelables);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return parcel;
    }

    public static <T extends Parcelable> List<T> unmarshall(byte[] bytes, Class<T> type, Parcelable.Creator creator) {
        Parcel parcel = unmarshall(bytes);
        List<T> result = new ArrayList<>();
        parcel.readTypedList(result, creator);
        parcel.recycle();
        return result;
    }

    public static byte[] toBytes(short s) {
        return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
