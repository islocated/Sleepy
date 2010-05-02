package com.isnotok.sleep.gallery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

public class GalleryViewer extends Gallery{

	private Provider provider;
	private StructuredSelection selection;
	
	private DefaultGalleryGroupRenderer gr;
	private DefaultGalleryItemRenderer ir;

	public GalleryViewer(Composite parent, int style) {
		super(parent, style);
		
		provider = new Provider();
		selection = new StructuredSelection();
	}
	
	public void setDefaultRenderers(){
		gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(64);
		gr.setItemWidth(84);
		gr.setAutoMargin(true);
		setGroupRenderer(gr);
		setAntialias(SWT.OFF);
		
		ir = new DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		ir.setDropShadows(true);
		ir.setDropShadowsSize(2);
		setItemRenderer(ir);
	}
	
	public void setAsSelectionProvider(IWorkbenchPartSite site) {
		// TODO Auto-generated method stub
		site.setSelectionProvider(provider);
		
		/*
		this.addMouseListener(new MouseListener(){

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("gallery generated mouse down event");
				provider.setSelection(getISelection());
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		*/
		
		addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("gallery generated default selection event");
				provider.setSelection(getISelection());
			}
			
			//Notify the site selection provider to fire selection event to listeners
			public void widgetSelected(SelectionEvent e) {
				System.out.println("gallery generated selection event");
				provider.setSelection(getISelection());
			}	
		});
	}
	
	public ISelectionProvider getProvider(){
		return provider;
	}

	public ISelection getISelection() {
		return selection;
	}

	private class StructuredSelection implements IStructuredSelection{

		public boolean isEmpty() {
			return getSelectionCount() == 0;
		}
		
		public List<GalleryItem> toList() {
			List<GalleryItem> list = new ArrayList<GalleryItem>();
			for(GalleryItem gi : getSelection()){
				list.add(gi);
			}
			// TODO Auto-generated method stub
			return list;
		}
		
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return getSelection();
		}
		
		public int size() {
			// TODO Auto-generated method stub
			return getSelectionCount();
		}
		
		public Iterator<GalleryItem> iterator() {
			// TODO Auto-generated method stub
			return toList().iterator();
		}
		
		public Object getFirstElement() {
			if(getSelectionCount() == 0)
				return null;
			// TODO Auto-generated method stub
			return getSelection()[0];
		}
	}
	
	private class Provider implements ISelectionProvider{
		private ListenerList listeners = new ListenerList();  
		
		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			// TODO Auto-generated method stub
			listeners.add(listener);
		}

		public ISelection getSelection() {
			// TODO Auto-generated method stub
			return selection;
		}

		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			// TODO Auto-generated method stub
			listeners.remove(listeners);
		}

		public void setSelection(ISelection selection) {
			// TODO Auto-generated method stub
			Object[] list = listeners.getListeners();  
			for (int i = 0; i < list.length; i++) {
				((ISelectionChangedListener) list[i])
					.selectionChanged(new SelectionChangedEvent(this, selection));  
			}
		}
	}
}
