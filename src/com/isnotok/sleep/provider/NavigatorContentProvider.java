package com.isnotok.sleep.provider;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.isnotok.sleep.model.PakFile;

public class NavigatorContentProvider implements ITreeContentProvider {
	private static FileFilter fileFilter = new FileFilter(){
		Pattern p = Pattern.compile(".*jpg|.*gif|.*png|.*bmp|.*pak", Pattern.CASE_INSENSITIVE);
		
		public boolean accept(File pathname) {
			return (pathname.isDirectory() 
				|| p.matcher(pathname.getName()).matches()) 
				&& !pathname.getName().equalsIgnoreCase(".")	//Ignore current directory
				&& !pathname.isHidden();						//Ignore hidden files
		}
	};
	
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof File){
			File file = (File) parentElement;
			
			if(file.getName().endsWith("pak")){
				//return new Object[0];
				PakFile pakfile = new PakFile(file);
				try {
					pakfile.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DataFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return pakfile.getTypes();
			}
			
			Object [] files = file.listFiles(fileFilter);
			
			return files == null ? new Object[0] : files;
		}
		else if(parentElement instanceof String){
			return new Object[0];
		}
		
		
		return new Object[0];
	}

	public Object getParent(Object element) {
		if(element instanceof File){
			return ((File)element).getParent();
		}
		
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof File){
			if(((File) element).getName().endsWith("pak")){
				return true;
			}
			return ((File) element).isDirectory() && getChildren(element).length > 0;
		}
		
		return false;
	}

	public Object[] getElements(Object inputElement) {
		//This is called when root is set as the input
		//TODO:What about when we select a new element?
		return getChildren(inputElement);
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		//Fire some event?
	}

}
