package com.isnotok.sleep.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.Deflater;

import com.isnotok.sleep.util.BytesUtil;

public class PakManager {
	HashMap<String, List<File>> cache = new HashMap<String, List<File>>();
	HashMap<String, List<File>> filtered = new HashMap<String, List<File>>();
	
	private String filter = "";

	private Pattern p = Pattern.compile(".*", Pattern.CASE_INSENSITIVE);
	private FileFilter filterTypes = new FileFilter(){

		public boolean accept(File pathname) {
			// TODO Auto-generated method stub
			if(!pathname.isDirectory())
				return false;
			
			for(String name : ResourceTypes.TYPES){
				if(pathname.getName().equals(name)){
					return true;
				}
			}
			
			return false;
		}
	};
	
	private FileFilter filterResources = new FileFilter(){

		public boolean accept(File pathname) {
			// TODO Auto-generated method stub
			if(pathname.isDirectory() || pathname.isHidden())
				return false;
			
			return true;
		}
	};
	
	public PakManager(){
		
	}
	
	
	
	public void initDirectory(File root){
		cache.clear();
		
		File [] types = root.listFiles(filterTypes);
		if(types == null)
			return;
		
		for(File type : types){
			System.out.println("initing: " + type);
			File [] files = type.listFiles(filterResources);
			if(files == null)
				continue;
			
			for(File file : files){
				addFile(type.getName(), file);
			}
		}
		
		setFilteredList();
	}
	
	public void clear() {
		// TODO Auto-generated method stub
		filtered.clear();
		cache.clear();
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

	public void save(String selected) {
		// TODO Auto-generated method stub
		File file = new File(selected);
		//PakFile pakFile = new PakFile(file);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		
		byte [] data;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			
			int count = 0;
			for(String type : cache.keySet()){
				System.out.println(type);
				List<File> list = cache.get(type);
				count += list.size();
			}
			
			//Write the count at top of pak
			stream.write(BytesUtil.writeInt(count));
			
			System.out.println("HHHHHHHHHHHNEW");
			for(String type : cache.keySet()){
				System.out.println(type);
				List<File> list = cache.get(type);
				for(File f : list){
					Resource resource = CacheManager.getInstance().getResource(f);
					stream.write(BytesUtil.writeString(resource.getType()));
					stream.write(UniqueId.computeFromData(resource.getData()).getBytes());
					stream.write(BytesUtil.writeString(resource.getResourceName()));
					stream.write(BytesUtil.writeInt(resource.getData().length));
					stream.write(resource.getData());
				}
			}
			
			data = stream.toByteArray();
			
			// Compress the bytes
			 byte[] output = new byte[data.length];
			 Deflater compresser = new Deflater();
			 compresser.setInput(data);
			 compresser.finish();
			 int compressedDataLength = compresser.deflate(output);

			fos.write(BytesUtil.writeInt(data.length));	//Write size of uncompressed data
			fos.write(output, 0, compressedDataLength);
			fos.close();
					
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
}
