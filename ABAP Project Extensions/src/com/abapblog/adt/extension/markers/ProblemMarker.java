package com.abapblog.adt.extension.markers;

import org.eclipse.core.resources.IFile;

public class ProblemMarker extends AbstractAdtMarker {
	public void create(IFile file, String message, int lineNumber) {
		try {
			super.create(file, message, lineNumber, MarkerCreatorJob.PROBLEM_MARKER_ID, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
