package com.abapblog.adt.extension.markers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.provisional.tasks.TaskTag;

import com.abapblog.adt.extension.preferences.PreferenceConstants;
import com.sap.adt.tools.abapsource.ui.AbapSourceUi;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices;

public class MarkerCreatorJob extends Job {

	public static final String TASK_MARKER_ID = "com.abapblog.adt.extension.markers.task";
	public static final String PROBLEM_MARKER_ID = "com.abapblog.adt.extension.markers.problem";
	private IFile file;
	private IDocument document;
	private static IAbapSourceScannerServices scannerServices = AbapSourceUi.getInstance().getSourceScannerServices();
	private TaskTag[] taskTags = null;
	private static final IPreferenceStore store = com.abapblog.adt.extension.Activator.getDefault()
			.getPreferenceStore();

	public static void createMarkers(IFile file, IDocument document) {
		try {
			file.deleteMarkers(TASK_MARKER_ID, false, IFile.DEPTH_ZERO);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (!store.getBoolean(PreferenceConstants.enableAbapTaskMining)) {
			return;
		}
		MarkerCreatorJob lintingJob = new MarkerCreatorJob(file, document);
		lintingJob.schedule();
	}

	private MarkerCreatorJob(IFile file, IDocument document) {
		super("Create ABAP Markers");
		this.file = file;
		this.document = document;
	}

	@Override
	protected Status run(IProgressMonitor monitor) {
		try {
			loadPreferenceValues();

			String code = document.get();
			for (TaskTag taskTag : taskTags) {
				List<Integer> offsets = findTag(code, taskTag.getTag());
				for (Integer offset : offsets) {
					if (!scannerServices.isComment(document, offset)) {
						continue;
					}
					String text = document.get().substring(offset,
							getLineEndOffset(document, document.getLineOfOffset(offset)));
					new TaskMarker().create(file, text, document.getLineOfOffset(offset) + 1, taskTag.getPriority());
				}
			}

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Status(Status.OK, "com.abapblog.adtlinter", "Linting completed");
	}

	private List<Integer> findTag(String input, String tag) {
		List<Integer> offsets = new ArrayList<>();
		String regex = "\\b" + Pattern.quote(tag) + "\\b";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			offsets.add(matcher.start());
		}
		return offsets;
	}

	private List<Integer> findTag2(String input, String tag) {
		List<Integer> offsets = new ArrayList<>();
		String code = input.toUpperCase();
		int index = code.indexOf(tag.toUpperCase());
		while (index >= 0) {
			offsets.add(index);
			index = code.indexOf(tag.toUpperCase(), index + 1);
		}
		return offsets;
	}

	private int getLineEndOffset(IDocument document, int lineNumber) throws BadLocationException {
		int lineOffset = document.getLineOffset(lineNumber);
		int lineLength = document.getLineLength(lineNumber);
		return lineOffset + lineLength;
	}

	private void loadPreferenceValues() {
		IPreferencesService preferencesService = null;
		IScopeContext[] preferencesLookupOrder = createPreferenceScopes();
		preferencesService = Platform.getPreferencesService();
		String tags = preferencesService.getString("org.eclipse.wst.sse.core/task-tags", "taskTags", "",
				preferencesLookupOrder);
		String priorities = preferencesService.getString("org.eclipse.wst.sse.core/task-tags", "taskPriorities", "",
				preferencesLookupOrder);
		loadTagsAndPrioritiesFrom(tags, priorities);
	}

	protected IScopeContext[] createPreferenceScopes() {
		IProject project = file.getProject();
		return project != null
				? new IScopeContext[] { new ProjectScope(project), InstanceScope.INSTANCE, DefaultScope.INSTANCE }
				: new IScopeContext[] { InstanceScope.INSTANCE, DefaultScope.INSTANCE };
	}

	private void loadTagsAndPrioritiesFrom(String tagString, String priorityString) {
		String[] tags = tagString.split(",");
		StringTokenizer toker = null;
		List list = new ArrayList();
		Integer number;
		for (toker = new StringTokenizer(priorityString, ","); toker.hasMoreTokens(); list.add(number)) {
			number = null;

			try {
				number = Integer.valueOf(toker.nextToken());
			} catch (NumberFormatException var8) {
				number = new Integer(1);
			}
		}

		Integer[] priorities = (Integer[]) list.toArray(new Integer[0]);
		taskTags = new TaskTag[Math.min(tags.length, priorities.length)];

		for (int i = 0; i < taskTags.length; ++i) {
			taskTags[i] = new TaskTag(tags[i], priorities[i]);
		}
	}
}
