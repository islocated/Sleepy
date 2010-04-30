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
	
	public Resource(File file){
		this.file = file;
	}
	
	public File getFile(){
		return file;
	}
	
	public byte [] getData(){
		return data;
	}
	
	public String getType(){
		return null;
	}
	
	public String getResourceName(){
		return null;
	}
	
	public void load() {
		// TODO Auto-generated method stub
		try {
			FileInputStream fis = new FileInputStream(file);
			long fileSize = file.length();
			
			if(fileSize > Integer.MAX_VALUE){
				throw new IOException();
			}
			
			data = new byte[(int)fileSize];
			
			int byteRead = fis.read(data);
			
			System.out.println("done reading resource file: " + byteRead);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToFile(){
		UniqueId id = new UniqueId(data);
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
		return null;
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		Resource resourceFile = new Resource(file);
		resourceFile.load();
		resourceFile.saveToFile();
	}
}
