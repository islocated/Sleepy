package com.isnotok.sleep.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UniqueId {
	public final static int MAX_DIGITS = 6;
	
	private byte[] id;

	public UniqueId(){
		id = new byte[]{0,0,0,0,0,0};
	}
	
	public UniqueId(byte[] id){
		this.id = id;
	}
	
	/*
	public UniqueId(byte [] data){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(data);
			byte [] digest = md.digest();
			id = new byte [MAX_DIGITS];
			System.arraycopy(digest, 0, id, 0, MAX_DIGITS);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this){
			return true;
		}
		
		if(obj instanceof UniqueId){
			UniqueId o = (UniqueId) obj;
			for(int i = 0; i < MAX_DIGITS; i++){
				if(id[i] != o.id[i]){
					return false;
				}
			}
			
			return true;
		}
		
		// TODO Auto-generated method stub
		return false;//super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		
		// TODO Auto-generated method stub
		return toHexString().hashCode();//super.hashCode();
	}
	
	public String toHexString(){
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < MAX_DIGITS; i++){
			String hex = Integer.toHexString(id[i] & 0xFF);
			sb.append(hex.length() > 1 ? hex : "0" + hex);
		}
		
		return sb.toString().toUpperCase();
	}
	
	public static void main(String [] args){
		UniqueId uniqueId = new UniqueId(new byte[]{ (byte) 0xFF,(byte) 0xAA,(byte) 0xFA,(byte) 0xBB,(byte) 0x93,0x0E});
		UniqueId uniqueId2 = new UniqueId(new byte[]{ (byte) 0xFF,(byte) 0xAA,(byte) 0xFA,(byte) 0xBB,(byte) 0x93,0x0E});
		
		System.out.println(uniqueId.toHexString());
		
		System.out.println(uniqueId.equals(uniqueId2));
	}

	public static UniqueId computeFromData(byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(data);
			byte [] digest = md.digest();
			
			UniqueId uid = new UniqueId();
			uid.id = new byte [MAX_DIGITS];
			System.arraycopy(digest, 0, uid.id, 0, MAX_DIGITS);
			
			return uid;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
