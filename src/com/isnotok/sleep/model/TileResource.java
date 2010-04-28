package com.isnotok.sleep.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import com.isnotok.sleep.util.BytesUtil;

public class TileResource {
	private File file;
	
	private byte [] data;
	
	public TileResource(File file){
		this.file = file;
	}
	
	public File getFile(){
		return file;
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
	
	public ImageData getImageData(){
		if(data == null)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
	    return new ImageData(16,16,32,palette, 1, data);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		TileResource resourceFile = new TileResource(file);
		resourceFile.load();
	}
}
