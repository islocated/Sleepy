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
	HashMap<String, List<File>> filtered = new HashMap<String, List<File>>();
	
	private String filter = "";

	private Pattern p = Pattern.compile(".*", Pattern.CASE_INSENSITIVE);
	
	public PakManager(){
		
	}
	
	public void initDirectory(File root){
		cache.clear();
		
		File [] types = root.listFiles();
		if(types == null)
			return;
		
		for(File type : types){
			System.out.println("initing: " + type);
			File [] files = type.listFiles();
			if(files == null)
				continue;
			
			for(File file : files){
				addFile(type.getName(), file);
			}
		}
		
		setFilteredList();
	}
	
	private void setFilteredList() {
		//Clearing this list allows it to remove types which may not be present
		//This is important because otherwise, one pack might not have the types
		//of the previous and these would stay...
		filtered.clear();
		
		// TODO Auto-generated method stub
		for(String type : cache.keySet()){
			List<File> list = cache.get(type);
			if(list == null){
				continue;
			}
			
			List<File> flist = filtered.get(type);
			if(flist == null){
				flist = new ArrayList<File>();
				filtered.put(type, flist);
			}
			flist.clear();
			
			for(File file : list){
				Resource resource = CacheManager.getInstance().getResource(file);
				//Unsupported resource from cache manager
				if(resource == null){
					continue;
				}
				if(p.matcher(resource.getResourceName()).matches()){
					flist.add(file);
				}
			}
		}
	}

	public void addFile(String type, File file){
		List<File> list = cache.get(type);
		if(list == null){
			list = new ArrayList<File>();
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
	
	//Uses filtered list
	public int getFileCount(String type){
		//HashMap<String, List<File>> map = filter.equals("") ? cache : filtered;
		List<File> list = filtered.get(type);
		if(list == null){
			return 0;
		}
		
		return list.size();
	}
	
	//Uses filtered list
	public File getFilesByType(String type, int index){
		List<File> list = filtered.get(type);
		if(list == null){
			return null;
		}
		
		if(index < 0 || index >= list.size())
			return null;
		
		//Just return the items from the filtered list
		return list.get(index);
	}
	
	public void setFilter(String text){
		filter  = text;
		
		p = Pattern.compile(".*" + filter + ".*", Pattern.CASE_INSENSITIVE);
		
		setFilteredList();
	}
}
