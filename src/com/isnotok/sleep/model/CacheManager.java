package com.isnotok.sleep.model;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.regex.Pattern;

//Cache Manager should probably not be singleton...
//We want to use this to manage other resources as well
public class CacheManager {
	private File cacheDirectory;
	HashMap<File, Resource> cache = new HashMap<File, Resource>();

	private String filter = "";

	private FileFilter fileFilter;
	
	//private final static CacheManager instance = new CacheManager();
	
	public CacheManager(){
	}
	
	/*
	public static CacheManager getInstance(){
		return instance;
	}
	*/
	
	public void setCacheDirectory(File file){
		cacheDirectory = file;
		cache.clear();
		
		File [] types = cacheDirectory.listFiles();
		if(types == null)
			return;
		
		for(File type : types){
			File [] resources = type.listFiles();
			if(resources == null)
				continue;
			
			for(File resource : resources){
				getResource(resource);
			}
		}
	}
	
	public int getFileCount(String type){
		File resources = new File(cacheDirectory, type);
		
		File [] files = resources.listFiles(fileFilter);
		if(files == null)
			return 0;
		
		return files.length;
	}
	
	public File[] getFilesByType(String type){
		File resources = new File(cacheDirectory, type);
		
		return resources.listFiles(fileFilter);
	}
	
	public void setFilter(String text){
		filter  = text;
		
		fileFilter = new FileFilter(){
			Pattern p = Pattern.compile(".*" + filter + ".*", Pattern.CASE_INSENSITIVE);
			
			public boolean accept(File file) {
				Resource resource = getResource(file);
				
				return (p.matcher(resource.getResourceName()).matches());
			}
		};
	}
	
	public Resource getResource(File file){
		File parent = file.getParentFile();
		
		Resource resource = cache.get(file);
		
		if(resource != null){
			return resource;
		}
		
		String type = parent.getName();
		
		if(type.equals("tile")){
			resource = new TileResource(file);
		}
		else if(type.equals("sprite")){
			resource = new SpriteResource(file);
		}
		else if(type.equals("room")){
			resource = new RoomResource(file);
		}
		else if(type.equals("scale")){
			resource = new ScaleResource(file);
		}
		else if(type.equals("music")){
			resource = new MusicResource(file);
		}
		else if(type.equals("timbre")){
			resource = new TimbreResource(file);
		}
		
		cache.put(file, resource);
		
		return resource;
	}
}
