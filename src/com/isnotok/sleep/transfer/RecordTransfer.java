package com.isnotok.sleep.transfer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.isnotok.sleep.model.PakRecord;

public class RecordTransfer extends ByteArrayTransfer {
	private static final String NAME = "com.isnotok.sleep.transfer.RecordTransfer";
	private static final int ID = registerType(NAME);
	
	private final RecordTransfer instance = new RecordTransfer();
	
	private RecordTransfer(){
		
	}
	
	public RecordTransfer getInstance(){
		return instance;
	}
	
	@Override
	protected int[] getTypeIds() {
		// TODO Auto-generated method stub
		return new int [] { ID };
	}

	@Override
	protected String[] getTypeNames() {
		// TODO Auto-generated method stub
		return new String [] { NAME };
	}
	
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if(object == null || !(object instanceof GalleryItem))
			return;
		
		GalleryItem galleryItem = (GalleryItem) object;
		
		PakRecord record = (PakRecord) galleryItem.getData();
		
		if(isSupportedType(transferData)){
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				stream.write(record.getData());
				
				super.javaToNative(stream.toByteArray(), transferData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected Object nativeToJava(TransferData transferData) {
		if(isSupportedType(transferData)){
			//try{
				byte [] raw = (byte[]) super.nativeToJava(transferData);
				if(raw == null)
					return null;
				/*
				//PakRecord record = new PakRecord();
				byte [] data;
				
				ByteArrayInputStream stream = new ByteArrayInputStream(raw);
				stream.read(data);
				*/
				
			//}
		}
		// TODO Auto-generated method stub
		return super.nativeToJava(transferData);
	}

}
