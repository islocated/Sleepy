package com.isnotok.sleep.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
	private static final int MAX_CACHE = 10;

	List<PakFile> cache;
	
	private static ResourceManager instance;
	
	private ResourceManager(){
		cache = new ArrayList<PakFile>();
	}
	
	public static ResourceManager getInstance(){
		if(instance != null){
			return instance;
		}
		
		return instance = new ResourceManager();
	}
	
	public PakFile getPakFile(File file){
		for(PakFile pakfile : cache){
			if(pakfile.getFile().equals(file)){
				return pakfile;
			}
		}
		
		if(cache.size() >= MAX_CACHE){
			cache.remove(0);
		}
		
		PakFile pakfile = new PakFile(file);
		pakfile.load();
		cache.add(pakfile);
		
		return pakfile;
	}
}
