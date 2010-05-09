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
			
			//System.out.println("des Pos:" + desPos);
			
			//Copy from source to dest srcWidth wide at offset
			System.arraycopy(src, srcPos, dest, desPos, srcWidth);
		}
		
		/*
		System.out.println("srcHeight in pixel not bytes:" + srcHeight);
		System.out.println("srcWidth in pixel :" + srcWidth/4);
		
		System.out.println("yoffset in pixel :" + (offset/destWidth)/4);
		System.out.println("xoffset in pixel :" + (offset%destWidth)/4);
		*/
	}
	
	//Copying blocks
	public static void copyBytesTrans(byte [] src, int srcWidth, int srcHeight, byte [] dest, int destWidth, int destHeight, int xoff, int yoff, byte [] alpha, byte[] destAlpha){
		int alphaIndex = -1;
		for(int y = 0; y < srcHeight; y++){
			for(int x = 0; x < srcWidth; x++){
				alphaIndex++;
				
				int srcPos = y * srcWidth + x;
				
				int desRow = yoff + y;
				int desCol = xoff + x;
				
				
				int desPos = desRow * destWidth + desCol;//offset + (y * destWidth + x);
				
				if((alpha[alphaIndex] & 0xFF) == 0){
					//System.arraycopy(src, srcPos, dest, desPos, 4);
					continue;
				}
				
				if(desRow < 0){
					continue;
				}
				
				if(desRow >= destHeight){
					return;
				}
				
				//If we go over the row boundary
				if(desCol >= destWidth){
					continue;
				}
				
				if(desCol < 0){
					continue;
				}
				
				//Copy color from source
				byte [] color = new byte[4];
				//System.arraycopy(src, srcPos*4, color, 0, 4);
				
				/*
				System.out.println("color1: " + color[0]);
				System.out.println("color2: " + color[1]);
				System.out.println("color3: " + color[2]);
				System.out.println("color4: " + color[3]);
				
				System.out.println("dest: " + dest[desPos*4]);
				System.out.println("dest2: " + dest[desPos*4+1]);
				System.out.println("dest3: " + dest[desPos*4+2]);
				System.out.println("dest4: " + dest[desPos*4+3]);
				
				System.out.println("alpha:" + (alpha[alphaIndex] & 0xff));
				*/
				//if((dest[desPos*4+3] & 0xFF )> 0){
					float trans = (alpha[alphaIndex] & 0xFF) / 255.0f;
					
					/*
					System.out.println((src[srcPos*4] & 0xFF) * trans);
					System.out.println((dest[desPos*4] & 0xFF) * (1-trans));
					System.out.println((byte) (((src[srcPos*4] & 0xFF) * trans) + ((dest[desPos*4] & 0xFF) * (1-trans)) * 255));
					*/
					
					float [] fcolor = new float[4];
					
					fcolor[0] = (((src[srcPos*4] & 0xFF)/255.0f * trans) + ((dest[desPos*4] & 0xFF)/255.0f * (1-trans)));
					fcolor[1] = (((src[srcPos*4+1] & 0xFF)/255.0f * trans) + ((dest[desPos*4+1] & 0xFF)/255.0f * (1-trans)));
					fcolor[2] = (((src[srcPos*4+2] & 0xFF)/255.0f * trans) + ((dest[desPos*4+2] & 0xFF)/255.0f * (1-trans)));
					fcolor[3] = dest[desPos*4+3];//alpha[alphaIndex];
					
					color[0] = (byte) (fcolor[0] * 255);
					color[1] = (byte) (fcolor[1] * 255);
					color[2] = (byte) (fcolor[2] * 255);
					color[3] = (byte) (fcolor[3]);
					
					
					if(color[0] < 10 && color[1] < 10 && color[2] < 10){
						System.out.println("here: " + color[3]);
						
						System.out.println("trans: " + trans);
						
						System.out.println("src: " + src[srcPos*4]);
						System.out.println("src: " + src[srcPos*4+1]);
						System.out.println("src: " + src[srcPos*4+2]);
						System.out.println("src: " + src[srcPos*4+3]);
						
						System.out.println("dest: " + dest[desPos*4]);
						System.out.println("dest2: " + dest[desPos*4+1]);
						System.out.println("dest3: " + dest[desPos*4+2]);
						System.out.println("dest4: " + dest[desPos*4+3]);
						
						System.out.println("fcolor: " + fcolor[0]);
						System.out.println("fcolor: " + fcolor[1]);
						System.out.println("fcolor: " + fcolor[2]);
						System.out.println("fcolor: " + fcolor[3]);
					}
				//}
				//else{
					
				//}
					
					/*
					System.out.println("after color1: " + color[0]);
					System.out.println("after color2: " + color[1]);
					System.out.println("after color3: " + color[2]);
					System.out.println("after color4: " + color[3]);
					*/
				
				//color[3] = alpha[alphaIndex];
				//destAlpha[alphaIndex] = color[3];
				
				/*
				if(x > destWidth){
					System.out.println("x is greater than dest width" + x + " :" + destWidth);
					continue;
				}*/
				
				
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
				//System.arraycopy(src, srcPos*4, dest, desPos*4, 4);
				
				System.arraycopy(color, 0, dest, desPos*4, 4);
			}
		}
	}
}
