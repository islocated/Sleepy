package com.isnotok.sleep.util;

public class BytesUtil {
	public static int readInt(byte[] bytes, int offset) {
		int value = 0;
		
		for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i + offset] & 0x000000FF) << shift;
        }

		return value;
	}
	
	public static byte[] readBytes(byte[] bytes, int offset, int length) {
		byte [] data = new byte[length];
		
		for(int i = 0; i < length; i++){
			data[i] = bytes[i+offset];
		}
		
		return data;
	}

	public static String readString(byte[] bytes, int offset){
		StringBuffer sb = new StringBuffer();
		
		while(bytes[offset] != 0){
			sb.append((char)bytes[offset++]);
		}
		
		return sb.toString();
	}
}
