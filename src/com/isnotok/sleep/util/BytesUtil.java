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
	
	public static byte [] writeInt(int value) {
		byte [] bytes = new byte [4];
		
		for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            bytes[i] = (byte) ((value >> shift) & 0xFF);
        }

		return bytes;
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
	
	public static byte [] writeString(String value){
		byte [] bytes = new byte[value.length() + 1];
		
		for(int i = 0; i < value.length(); i++){
			bytes[i] = (byte) (value.charAt(i) & 0xFF);
		}
		
		bytes[value.length()] = 0;
		
		return bytes;
	}
	
	//Copying blocks
	public static void copyBlock(byte [] src, int srcWidth, int srcHeight, byte [] dest, int destWidth, int destHeight, int offset){
		for(int y = 0; y < srcHeight; y++){
			int srcPos = y * srcWidth;
			int desPos = offset + (y * destWidth);
			
			if(desPos < 0){
				System.out.println("under pixel");
				continue;
			}
			
			if(desPos >= destWidth * destHeight){
				System.out.println("out of bounds pixel copy");
				return;
			}
			
			if(desPos + srcWidth > destWidth * destHeight){
				srcWidth = destWidth * destHeight - desPos;
				System.out.println("lowering the srcwidth");
			}
			
			//Copy from source to dest srcWidth wide at offset
			System.arraycopy(src, srcPos, dest, desPos, srcWidth);
		}
	}
	
	//Copying blocks
	public static void copyBytesTrans(byte [] src, int srcWidth, int srcHeight, byte [] dest, int destWidth, int destHeight, int offset, byte [] alpha){
		int alphaIndex = 0;
		for(int y = 0; y < srcHeight; y++){
			for(int x = 0; x < srcWidth; x++){
				int srcPos = y * srcWidth + x;
				
				int desPos = offset + (y * destWidth + x);
				
				if((alpha[alphaIndex++] & 0xFF) == 0){
					//System.arraycopy(src, srcPos, dest, desPos, 4);
					continue;
				}
				
				if(x > destWidth){
					System.out.println("x is greater than dest width" + x + " :" + destWidth);
					continue;
				}
				
				if(desPos < 0){
					System.out.println("under pixel");
					continue;
				}
				
				if(desPos >= destWidth * destHeight){
					System.out.println("out of bounds pixel copy");
					return;
				}
				
				
				
				/*
				if(desPos + srcWidth > destWidth * destHeight){
					srcWidth = destWidth * destHeight - desPos;
					System.out.println("lowering the srcwidth");
				}
				*/
				
				//Copy from source to dest 4 bytes wide at offset
				System.arraycopy(src, srcPos*4, dest, desPos*4, 4);
			}
		}
	}
}
