package com.abapblog.adt.extension.markers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobGroup;

public class MarkerDeletionJob extends Job {

	public static final String TASK_MARKER_ID = "com.abapblog.adt.extension.markers.task";
	public static final String PROBLEM_MARKER_ID = "com.abapblog.adt.extension.markers.problem";
	private IFile file;

	public static void deleteMarkers(IFile file, JobGroup jobGroup) {

		MarkerDeletionJob job = new MarkerDeletionJob(file);
		job.setJobGroup(jobGroup);
		job.schedule();

	}

	private MarkerDeletionJob(IFile file) {
		super("Delete ABAP Markers");
		this.file = file;
	}

	@Override
	protected Status run(IProgressMonitor monitor) {
		try {
			file.deleteMarkers(TASK_MARKER_ID, false, IFile.DEPTH_ZERO);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return new Status(Status.OK, "com.abapblog.adt.extensions", "Markers deleted");
	}

}
