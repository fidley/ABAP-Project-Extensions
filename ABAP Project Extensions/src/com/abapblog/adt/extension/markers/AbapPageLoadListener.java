package com.abapblog.adt.extension.markers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import com.sap.adt.tools.abapsource.ui.sources.editors.AbstractAbapSourcePageExtensionProcessor;
import com.sap.adt.tools.abapsource.ui.sources.editors.IAbapSourcePage;

@SuppressWarnings("restriction")
public class AbapPageLoadListener extends AbstractAbapSourcePageExtensionProcessor {
	// Map of document listeners and their corresponding files
	private static final Map<IFile, ABAPDocumentListener> listeners = new HashMap<>();

	@Override
	public void processOnDocumentLoaded(IAbapSourcePage sourcePage) {
		IFile file = sourcePage.getFile();
		if (listeners.containsKey(file)) {
			return;
		}
		ABAPDocumentListener listener = new ABAPDocumentListener(file);
		listeners.put(file, listener);
		sourcePage.getDocument().addDocumentListener(listener);
		MarkerCreatorJob.createMarkers(file, sourcePage.getDocument());
	}
}
