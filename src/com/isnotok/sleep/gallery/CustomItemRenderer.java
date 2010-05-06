package com.isnotok.sleep.gallery;

import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;

import com.isnotok.sleep.Activator;
import com.isnotok.sleep.IImageKeys;

public class CustomItemRenderer extends DefaultGalleryItemRenderer {
	@Override
	public void preDraw(GC gc) {
		// TODO Auto-generated method stub
		super.preDraw(gc);
		
		//Color c1 = new Color(gc.getDevice(), 0, 0, 0);
		//Color c2 = new Color(gc.getDevice(), 64, 64, 64);
		
		Image image = new Image(gc.getDevice(), (Activator.getImageDescriptor(IImageKeys.CHECKERS)).getImageData());
		
		Pattern pattern = new Pattern(gc.getDevice(), image);
		gc.setBackgroundPattern(pattern );
		gc.fillRectangle(gc.getClipping());
		
		pattern.dispose();
		image.dispose();
		
		//c1.dispose();
		//c2.dispose();
	}
}
