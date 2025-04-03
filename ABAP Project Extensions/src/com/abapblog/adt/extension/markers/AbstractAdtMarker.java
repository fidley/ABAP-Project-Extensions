package com.abapblog.adt.extension.markers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

public class AbstractAdtMarker {

	public void create(IFile file, String message, int lineNumber, String markerId, int priority) {
		try {
			IMarker marker = file.createMarker(markerId);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, priority);
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
