package com.isnotok.sleep.util;

import java.io.File;

public class FileUtil {
	public static boolean deleteFile(File root){
		if(root.exists()){
			if(root.isDirectory()){
				File [] files = root.listFiles();
				for(int i = 0; i < files.length; i++)
					deleteFile(files[i]);
			}
			
			return root.delete();
		}
		return false;
	}
}
