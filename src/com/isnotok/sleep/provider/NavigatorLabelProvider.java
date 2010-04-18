package com.isnotok.sleep.provider;

import java.io.File;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;

import com.isnotok.sleep.Activator;
import com.isnotok.sleep.IImageKeys;
import com.isnotok.sleep.model.FileModel;

public class NavigatorLabelProvider extends LabelProvider implements
		ILabelProvider, IDescriptionProvider {

	@Override
	public String getText(Object element) {
		FileModel fileModel = (FileModel) element;
		
		// TODO Auto-generated method stub
		return fileModel.getName();
	}
	
	@Override
	public Image getImage(Object element) {
		File fileModel = (File) element;
		if(!fileModel.isDirectory()){
			return null;
		}
		// TODO Auto-generated method stub
		return Activator.getImageDescriptor(
				fileModel.isDirectory() ?
				IImageKeys.FOLDER : IImageKeys.FILE).createImage();//super.getImage(element);
	}
	
	public String getDescription(Object anElement) {
		// TODO Auto-generated method stub
		return null;
	}

}
