package com.isnotok.sleep.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.isnotok.sleep.util.BytesUtil;

public class PakFile {
	private File file;
	//private List<PakRecord> list;
	private HashMap<String, HashMap<byte[], PakRecord>> map;
	
	public PakFile(File file){
		this.file = file;
		
		//list = new ArrayList<PakRecord>();
		map = new HashMap<String, HashMap<byte[], PakRecord>>();
	}
	
	public PakRecord getTile(String name){
		HashMap<byte [], PakRecord> records = map.get("tile");
		Collection<PakRecord> collection = records.values();
		
		Iterator<PakRecord> itr = collection.iterator();
		
		while(itr.hasNext()){
			PakRecord record = itr.next();
			if(record.getWordString().equals(name)){
				return record;
			}
		}
		
		return null;
		//return map.get("tile").values().toArray(new PakRecord[0])[5];
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/4.pak");
		PakFile pakFile = new PakFile(file);
		
		try {
			pakFile.load();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load() throws IOException, DataFormatException{
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
					PakRecord pakrecord = new PakRecord(uncompressedData, offset);
					//list.add(pakrecord);
					
					HashMap<byte[], PakRecord> records = map.get(pakrecord.getType());
					if(records == null){
						records = new HashMap<byte[], PakRecord>();
						map.put(pakrecord.getType(), records);
					}
					records.put(pakrecord.getId(), pakrecord);
					
					offset += pakrecord.getUsedBytes();
				}
				
				System.out.println("Successful");
			}
		}
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
}
