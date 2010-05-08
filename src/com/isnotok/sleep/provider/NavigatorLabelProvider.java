package com.isnotok.sleep.provider;

import java.io.File;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.IDescriptionProvider;

import com.isnotok.sleep.model.ModelObject;

public class NavigatorLabelProvider extends LabelProvider implements
		ILabelProvider, IDescriptionProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof ModelObject){
			ModelObject model = (ModelObject) element;
			return model.getText();
		}
		
		File file = (File) element;
		
		if(file.getName().equals("")){
			return file.getPath();
		}
		
		// TODO Auto-generated method stub
		return file.getName();
	}
	
	@Override
	public Image getImage(Object element) {
		if(element instanceof ModelObject){
			ModelObject model = (ModelObject) element;
			return model.getImage();
		}
		
		File file = (File) element;
		
		//Use shared images
		return PlatformUI.getWorkbench().getSharedImages().getImage(file.isFile() ? ISharedImages.IMG_OBJ_FILE : ISharedImages.IMG_OBJ_FOLDER);
		
		/*
		// TODO Auto-generated method stub
		return Activator.getImageDescriptor(fileModel.isDirectory() ? IImageKeys.FOLDER : IImageKeys.FILE).createImage();
		*/
	}
	
	public String getDescription(Object anElement) {
		// TODO Auto-generated method stub
		return null;
	}

}
