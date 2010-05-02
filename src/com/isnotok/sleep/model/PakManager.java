package com.isnotok.sleep.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

//Cache Manager should probably not be singleton...
//We want to use this to manage other resources as well
public class PakManager {
	HashMap<String, List<File>> cache = new HashMap<String, List<File>>();
	private String filter = "";

	private Pattern p = Pattern.compile(".*", Pattern.CASE_INSENSITIVE);;
	
	public PakManager(){
		
	}
	
	public void initDirectory(File root){
		cache.clear();
		
		File [] types = root.listFiles();
		if(types == null)
			return;
		
		for(File type : types){
			File [] files = type.listFiles();
			if(files == null)
				continue;
			
			for(File file : files){
				addFile(type.getName(), file);
			}
		}
	}
	
	public void addFile(String type, File file){
		List<File> list = cache.get(type);
		if(list == null){
			list = new LinkedList<File>();
			cache.put(type, list);
		}
		
		if(!list.contains(file)){
			list.add(file);
		}
	}
	
	public void removeFile(String type, File file){
		List<File> list = cache.get(type);
		if(list == null){
			return;
		}
		
		list.remove(file);
	}
	
	public int getFileCount(String type){
		List<File> list = cache.get(type);
		if(list == null){
			return 0;
		}
		
		return list.size();
	}
	
	public File[] getFilesByType(String type, int index){
		List<File> list = cache.get(type);
		if(list == null){
			return new File[0];
		}
		
		List<File> files = new ArrayList<File>();
		for(File file : list){
			Resource resource = CacheManager.getInstance().getResource(file);
			if(p.matcher(resource.getResourceName()).matches()){
				files.add(file);
			}
		}
		
		return files.toArray(new File[0]);
	}
	
	public File[] getFilesByType(String type){
		List<File> list = cache.get(type);
		if(list == null){
			return new File[0];
		}
		
		List<File> files = new ArrayList<File>();
		for(File file : list){
			Resource resource = CacheManager.getInstance().getResource(file);
			if(p.matcher(resource.getResourceName()).matches()){
				files.add(file);
			}
		}
		
		return files.toArray(new File[0]);
	}
	
	public void setFilter(String text){
		filter  = text;
		
		p = Pattern.compile(".*" + filter + ".*", Pattern.CASE_INSENSITIVE);
	}
}
