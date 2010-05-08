package com.isnotok.sleep.model;

import java.io.File;

public class SpriteLayer {
	private UniqueId id;
	private byte[] offset = new byte[2];
	private byte trans = 0;
	private byte glow = 0;
	private File file;
	
	public SpriteLayer(File file){
		this.file = file;
	}
	
	public UniqueId getId() {
		return id;
	}
	public void setId(byte [] id) {
		this.id = new UniqueId(id);
	}
	public byte[] getOffset() {
		return offset;
	}
	public void setOffset(byte[] offset) {
		this.offset = offset;
	}
	public byte getTrans() {
		return trans;
	}
	public void setTrans(byte trans) {
		this.trans = trans;
	}
	public byte getGlow() {
		return glow;
	}
	public void setGlow(byte glow) {
		this.glow = glow;
	}
	public byte[] getData() {
		File parent = new File(file.getParentFile().getParentFile(), "sprite");
		File sprite = new File(parent, id.toHexString());
		
		if(!sprite.exists()){
			System.out.println("why is this not present?");
		}
		
		return CacheManager.getInstance().getResource(sprite).getData();
	}
}
