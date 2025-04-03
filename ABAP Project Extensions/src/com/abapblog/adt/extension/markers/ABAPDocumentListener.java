package com.abapblog.adt.extension.markers;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

public class ABAPDocumentListener implements IDocumentListener {

	private IFile file;

	public ABAPDocumentListener(IFile file) {
		this.file = file;
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		MarkerCreatorJob.createMarkers(file, event.getDocument());
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// No action needed before the document changes
	}

}