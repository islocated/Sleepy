package com.isnotok.sleep.provider;

import java.io.File;
import java.util.HashMap;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.IDescriptionProvider;

import com.isnotok.sleep.model.ModelObject;

public class CacheLabelProvider extends LabelProvider implements ILabelProvider, IDescriptionProvider {

	private HashMap<File, Image> images = new HashMap<File, Image>();

	public CacheLabelProvider(){
		
	}
	
	public String getDescription(Object element) {
		if(element instanceof ModelObject){
			ModelObject model = (ModelObject) element;
			return model.getText();
		}
		return null;
	}
	
	@Override
	public Image getImage(Object element) {
		if(element instanceof ModelObject){
			//Image image = new Image(Display.getCurrent());
			ModelObject model = (ModelObject) element;
			Image image = images.get(model.getFile());
			if(image == null){
				image = model.getImage();
				if(image == null){
					return null;
					//return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
				}
				
				images.put((File)model.getFile(), image);
			}
			return image;
		}
		
		// TODO Auto-generated method stub
		return super.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		if(element instanceof ModelObject){
			ModelObject model = (ModelObject) element;
			return model.getText();
		}
		// TODO Auto-generated method stub
		return super.getText(element);
	}
	
	@Override
	public void dispose() {
		for(Image image : images.values()){
			if(image != null && !image.isDisposed()){
				image.dispose();
			}
		}
		// TODO Auto-generated method stub
		super.dispose();
	}

}
