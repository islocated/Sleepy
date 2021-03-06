package com.isnotok.sleep.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.graphics.ImageData;

public class Resource {
	protected File file;
	protected String type;
	protected byte [] data;
	protected String name;
	protected int nameOffset;
	protected boolean isDirty;
	
	protected ImageData imageData;
	
	public Resource(File file){
		this.file = file;
	}
	
	public File getFile(){
		return file;
	}
	
	public byte [] getData(){
		if(!isDirty){
			return data;
		}
		//recompute data -- this is just adding the name back into the data
		
		byte [] bytes = new byte [nameOffset + name.length() + 1];
		System.arraycopy(data, 0, bytes, 0, nameOffset);
		for(int i = 0; i < name.length(); i++){
			bytes[nameOffset + i] = (byte) name.charAt(i);
		}
		data = bytes;
		isDirty = false;
		
		return data;
	}
	
	public String getType(){
		return null;
	}
	
	public String getResourceName(){
		if(name == null){
			name = getNameFromData();
		}
		
		return name;
	}
	
	protected String getNameFromData(){
		StringBuffer sb = new StringBuffer();
		for(int i = nameOffset; i < data.length-1; i++){
			sb.append((char)data[i]);
		}
		return sb.toString();
	}
	
	protected void load() {
		// TODO Auto-generated method stub
		try {
			FileInputStream fis = new FileInputStream(file);
			long fileSize = file.length();
			
			if(fileSize > Integer.MAX_VALUE){
				throw new IOException();
			}
			
			System.out.println("reading file: " + file.getAbsolutePath());
			
			data = new byte[(int)fileSize];
			
			int byteRead = fis.read(data);
			
			fis.close();
			
			System.out.println("done reading resource file: " + byteRead);
			
			isDirty = false;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Pixel getPixel(int index){
		if(index < 0 || index >= nameOffset){
			return null;
		}
		
		return new Pixel(data, index * 4);
	}
	
	public void saveToFile(){
		UniqueId id = UniqueId.computeFromData(data);
		File out = new File(file.getParentFile(), id.toHexString());
		
		try {
			FileOutputStream fos = new FileOutputStream(out);
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageData getImageData(){
		if(imageData == null){
			imageData = calculateImageData();
		}
		
		return imageData;
	}
	
	protected ImageData calculateImageData() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		Resource resourceFile = new Resource(file);
		resourceFile.load();
		//resourceFile.saveToFile();
	}

	public void renameTo(String text) {
		if(name.equals(text)){
			return;
		}
		
		// TODO Auto-generated method stub
		name = text;
		isDirty = true;
	}
}
