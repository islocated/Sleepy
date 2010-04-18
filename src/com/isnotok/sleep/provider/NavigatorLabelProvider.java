package com.isnotok.sleep.provider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;

import com.isnotok.sleep.model.DirectoryObject;

public class NavigatorLabelProvider extends LabelProvider implements
		ILabelProvider, IDescriptionProvider {

	@Override
	public String getText(Object element) {
		DirectoryObject directoryObject = (DirectoryObject) element;
		
		// TODO Auto-generated method stub
		return directoryObject.getName();
	}
	
	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return super.getImage(element);
	}
	
	public String getDescription(Object anElement) {
		// TODO Auto-generated method stub
		return null;
	}

}
