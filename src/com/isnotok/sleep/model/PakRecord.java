package com.isnotok.sleep.model;

import com.isnotok.sleep.util.BytesUtil;

public class PakRecord {
	private String type;
    private byte[] id;
    private String wordString;
    private int length;
    private byte[] data;
    
    public PakRecord(byte[] bytes, int offset) {
		// TODO Auto-generated constructor stub
		type = BytesUtil.readString(bytes, offset);
		offset += type.length() + 1;
		
		id = BytesUtil.readBytes(bytes, offset, 6);
		offset += 6;
		
		wordString = BytesUtil.readString(bytes, offset);
		offset += wordString.length() + 1;
		
		length = BytesUtil.readInt(bytes, offset);
		offset += 4;
		
		data = BytesUtil.readBytes(bytes, offset, length);
		offset += length;
	}
	
	public int getUsedBytes(){
		return type.length() + 1 + 6 + wordString.length() + 1 + 4 + length;
	}
	
	

	public String getType() {
		return type;
	}

	public byte[] getId() {
		return id;
	}

	public String getWordString() {
		return wordString;
	}

	public byte[] getData() {
		return data;
	}

	public int getLength() {
		return length;
	}
}
