package com.isnotok.sleep.gallery;

import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.isnotok.sleep.Activator;
import com.isnotok.sleep.IImageKeys;

public class CustomItemRenderer extends DefaultGalleryItemRenderer {
	protected Color color = new Color(Display.getCurrent(), 0, 0, 0);
	
	@Override
	public void draw(GC gc, GalleryItem item, int index, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		super.draw(gc, item, index, x, y, width, height);
		
		/*
		gc.setForeground(color);
		
		Rectangle rect = item.getImage().getBounds();
		rect.x = x + 10;
		rect.y = y + 10;
		
		gc.drawRectangle(rect);
		*/
	}
	
	@Override
	public void dispose() {
		color.dispose();
		// TODO Auto-generated method stub
		super.dispose();
	}
	
	/*
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
	*/
}
