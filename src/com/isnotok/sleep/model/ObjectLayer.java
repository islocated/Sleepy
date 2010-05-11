package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;

public class ObjectLayer {
	private UniqueId id;
	private int[] offset = new int[2];
	private int[] speechOffset = new int[2];
	private byte flip;
	private byte box;
	private byte lock;
	private byte trans;
	
	private File file;
	
	public ObjectLayer(File file){
		this.file = file;
	}
	
	public UniqueId getId() {
		return id;
	}
	public void setId(byte [] id) {
		this.id = new UniqueId(id);
	}
	public int[] getOffset() {
		return offset;
	}
	public void setOffset(int[] offset) {
		this.offset = offset;
	}
	public byte getTrans() {
		return trans;
	}
	public void setTrans(byte trans) {
		this.trans = trans;
	}
	public byte[] getData() {
		File parent = new File(file.getParentFile().getParentFile(), "object");
		File object = new File(parent, id.toHexString());
		
		if(!object.exists()){
			System.out.println("why is this not present?");
		}
		
		return CacheManager.getInstance().getResource(object).getImageData().data;//getData();
	}
	
	

	public int[] getSoffset() {
		return speechOffset;
	}

	public void setSpeechOffset(int[] speechOffset) {
		this.speechOffset = speechOffset;
	}

	public byte getFlip() {
		return flip;
	}

	public void setFlip(byte flip) {
		this.flip = flip;
	}

	public byte getBox() {
		return box;
	}

	public void setBox(byte box) {
		this.box = box;
	}

	public byte getLock() {
		return lock;
	}

	public void setLock(byte lock) {
		this.lock = lock;
	}

	public ImageData getImageData() {
		File parent = new File(file.getParentFile().getParentFile(), "object");
		File object = new File(parent, id.toHexString());
		
		if(!object.exists()){
			System.out.println("why is this not present?");
			return null;
		}
		
		return ((ObjectResource) CacheManager.getInstance().getResource(object)).getFullImageData();
	}
	
	public byte[] getCenter() {
		File parent = new File(file.getParentFile().getParentFile(), "object");
		File object = new File(parent, id.toHexString());
		
		if(!object.exists()){
			System.out.println("why is this not present?");
		}
		
		ObjectResource o = (ObjectResource) CacheManager.getInstance().getResource(object);
		
		return null;
		//o.get
		
	}
	
	
}
