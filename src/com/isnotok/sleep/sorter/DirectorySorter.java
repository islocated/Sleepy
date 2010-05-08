package com.isnotok.sleep.sorter;

import java.io.File;
import java.text.Collator;

import org.eclipse.jface.viewers.ViewerSorter;

import com.isnotok.sleep.model.ModelObject;

public class DirectorySorter extends ViewerSorter {

	public DirectorySorter() {
		// TODO Auto-generated constructor stub
	}

	public DirectorySorter(Collator collator) {
		super(collator);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int category(Object element) {
		if(element instanceof ModelObject){
			ModelObject model = (ModelObject) element;
			return model.getFile().isDirectory() ? 0 : 1;
		}
		else if(element instanceof File){
			return ((File)element).isDirectory() ? 0 : 1;
		}
		
		// TODO Auto-generated method stub
		return super.category(element);
	}

}
