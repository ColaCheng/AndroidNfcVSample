package com.example.cola.nfcsample;

import java.io.IOException;
import java.util.Arrays;

import android.nfc.FormatException;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.util.Log;


/**
 * Created by cola on 15/5/5.
 */
public class NfcvFunction {

    static String TAG = "NfcV";
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static void write(Tag tag, byte[] data, int blockNum) throws IOException,
            FormatException, InterruptedException {
        if (tag == null) {
            return;
        }
        NfcV nfc = NfcV.get(tag);

        nfc.connect();

        Log.d(TAG, "Max Transceive Bytes: " + nfc.getMaxTransceiveLength());

        // NfcV Tag has 64 Blocks with 4 Byte
//        if ((data.length / 4) > 64) {
//            // ERROR HERE!
//            Log.d(TAG, "too much data...");
//        }

        if ((data.length % 4) != 0) {
            byte[] ndata = new byte[(data.length) + (4 - (data.length % 4))];
            Arrays.fill(ndata, (byte) 0x00);
            System.arraycopy(data, 0, ndata, 0, data.length);
            data = ndata;
        }

        byte[] arrByte = new byte[7];
        // Flags:0x02 with low data rate, 0x42 with high data rate
        arrByte[0] = 0x02;
        // ISO 15693 Single Block write command byte
        arrByte[1] = 0x21;
        // block number
        arrByte[2] = (byte) (blockNum);

        // data, DONT SEND LSB FIRST!
        arrByte[3] = data[0];
        arrByte[4] = data[1];
        arrByte[5] = data[2];
        arrByte[6] = data[3];

        try {
            Log.d(TAG, "Writing Data to block " + blockNum + " ["
                    + getHexString(arrByte) + "]");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            nfc.transceive(arrByte);
        } catch (IOException e) {
            if (e.getMessage().equals("Tag was lost.")) {
                // continue, because of Tag bug
                Log.d(TAG, e.toString());
            } else {
                throw e;
            }
        }

        nfc.close();
    }

    public static byte[] read(Tag tag, int blockNum){
        byte[] data = null;
        if(tag != null){
            // set up read command buffer
            byte blockNo = (byte)blockNum; // block address
            byte[] id = tag.getId();
            byte[] readCmd = new byte[3 + id.length];
            readCmd[0] = 0x20; // set "address" flag (only send command to this tag)
            readCmd[1] = 0x20; // ISO 15693 Single Block Read command byte

            System.arraycopy(id, 0, readCmd, 2, id.length); // copy ID
            readCmd[2 + id.length] = blockNo; // 1 byte payload: block address
            try {
                Log.d(TAG, "Reading block " + blockNum + ", cmd:["
                        + getHexString(readCmd) + "]");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            NfcV tech = NfcV.get(tag);
            if (tech != null) {
                // send read command
                try {
                    tech.connect();
                    data = tech.transceive(readCmd);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        tech.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return data;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String getHexString(byte[] bytesData) throws Exception {
        char[] hexChars = new char[bytesData.length * 2];
        for ( int j = 0; j < bytesData.length; j++ ) {
            int v = bytesData[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
