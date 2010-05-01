package com.isnotok.sleep.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.isnotok.sleep.util.BytesUtil;

public class PakFile {
	private File file;
	private HashMap<String, HashMap<String, List<PakRecord>>> mapByType;
	private HashMap<UniqueId, PakRecord> idMap;
	
	private String filter;
	
	public PakFile(File file){
		this.file = file;
		filter = "";
		
		mapByType = new HashMap<String, HashMap<String, List<PakRecord>>>();
		idMap = new HashMap<UniqueId, PakRecord>();
	}
	
	public File getFile(){
		return file;
	}
	
	public List<PakRecord> getResource(String type, String name){
		return mapByType.get(type).get(name);
	}
	
	public PakRecord getResourceById(UniqueId id){
		return idMap.get(id);
	}
	
	public Object [] getResourceType(String type){
		List<PakRecord> objs = new LinkedList<PakRecord>();
		if(mapByType == null || !mapByType.containsKey(type))
			return new Object[0];
		
		Pattern p = Pattern.compile(".*" + filter + ".*", Pattern.CASE_INSENSITIVE);
		
		for(List<PakRecord> records : mapByType.get(type).values()){
			for(PakRecord record : records){
				if(p.matcher(record.getWordString()).matches()){
					objs.add(record);
				}
			}
			//objs.addAll(records);
		}
		
		return objs.toArray();
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/4.pak");
		PakFile pakFile = new PakFile(file);
		pakFile.load();
	}
	
	public void load(){
		try {
			loadHelper();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadHelper() throws IOException, DataFormatException{
		if(file.getName().endsWith(".pak")){    
            // a pack file
			byte uncompressedData[] = getUncompressedData(file);
			
			if(uncompressedData == null){
				throw new DataFormatException();
			}
			
			int numChunks = BytesUtil.readInt(uncompressedData, 0);
			int offset = 4;		//Number of bytes we read to get num of chunks
			
			//This is not a direct translation, because we aren't getting numUsed...
			if(numChunks > 0){
				//First thing read is type
				//while(offset < uncompressedData.length){
				
				for(int i = 0; i < numChunks; i++){
					PakRecord pakrecord = new PakRecord(this, uncompressedData, offset);
					addRecord(pakrecord);
					
					offset += pakrecord.getUsedBytes();
				}
				
				System.out.println("Successful");
			}
		}
	}
	
	
	private void addRecord(PakRecord pakrecord){
		String type = pakrecord.getType();
		String name = pakrecord.getWordString();
		
		//Get name map of records
		HashMap<String, List<PakRecord>> nameMap = mapByType.get(type);
		if(nameMap == null){
			nameMap = new HashMap<String, List<PakRecord>>();
			mapByType.put(type, nameMap);
		}
		
		List<PakRecord> pakrecords = nameMap.get(name);
		if(pakrecords == null){
			pakrecords = new LinkedList<PakRecord>();
			nameMap.put(name, pakrecords);
		}
		
		pakrecords.add(pakrecord);
		
		//Also add by id
		idMap.put(pakrecord.getId(), pakrecord);
	}

	private static byte [] getUncompressedData(File file)
			throws FileNotFoundException, IOException, DataFormatException {
		FileInputStream fis = new FileInputStream(file);
		
		long fileSize = file.length();
		
		//Not expecting fileSize to be larger than Integer.MAX_VALUE
		//because we can't create an array that's too big
		if(fileSize > Integer.MAX_VALUE){
			throw new IOException();
		}
		
		byte compressedData[] = new byte[(int)fileSize];
		
		int byteRead = fis.read(compressedData);
		
		if(byteRead != -1){
			//Valid compressed Data
			
			//Read a leading int
			int bytesLeft = BytesUtil.readInt(compressedData, 0);
			
			//uncompress from compressData[4];
			byte uncompressedData[] = new byte[bytesLeft];
			
			Inflater inflater = new Inflater();
			inflater.setInput(compressedData, 4, (int)fileSize-4);
			
			if(inflater.inflate(uncompressedData) <= 0){
				//We did something wrong...;
			}
			else{
				//We have valid bytes
				System.out.println("we got the files in");
				return uncompressedData;
			}
		}
		
		return null;
	}

	public void setFilter(String filter) {
		// TODO Auto-generated method stub
		this.filter = filter;
	}
}
