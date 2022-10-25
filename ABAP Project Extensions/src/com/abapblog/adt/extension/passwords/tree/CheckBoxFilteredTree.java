package com.abapblog.adt.extension.passwords.tree;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

public class CheckBoxFilteredTree extends FilteredTree {

	public CheckBoxFilteredTree(Composite parent, int treeStyle, PatternFilter filter, boolean useNewLook,
			boolean useFastHashLookup) {
		super(parent, treeStyle, filter, useNewLook, useFastHashLookup);
	}

	public CheckBoxFilteredTree(Composite parent, int treeStyle, PatternFilter filter, boolean useNewLook) {
		super(parent, treeStyle, filter, useNewLook);
	}

	@Override
	protected CheckboxTreeViewer doCreateTreeViewer(Composite parent, int style) {
		return new CheckboxTreeViewer(parent, style);
	}

}
