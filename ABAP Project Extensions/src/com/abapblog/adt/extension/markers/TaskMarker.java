package com.abapblog.adt.extension.markers;

import org.eclipse.core.resources.IFile;

public class TaskMarker extends AbstractAdtMarker {

	public void create(IFile file, String message, int lineNumber, int priority) {
		try {
			super.create(file, message, lineNumber, MarkerCreatorJob.TASK_MARKER_ID, priority);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
