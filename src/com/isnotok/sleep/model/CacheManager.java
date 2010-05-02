package com.isnotok.sleep.model;

import java.io.File;
import java.util.HashMap;

//Cache Manager should probably not be singleton...
//We want to use this to manage other resources as well
public class CacheManager {
	private File cacheDirectory;
	HashMap<File, Resource> cache = new HashMap<File, Resource>();

	private final static CacheManager instance = new CacheManager();
	
	private CacheManager(){
	}
	
	public static CacheManager getInstance(){
		return instance;
	}
	
	//How do we prevent cache manager from growing too big?
	//Store a list of the last thousand or so
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
