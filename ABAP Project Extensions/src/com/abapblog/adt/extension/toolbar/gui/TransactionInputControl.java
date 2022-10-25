package com.abapblog.adt.extension.toolbar.gui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import com.abapblog.adt.extension.Activator;
import com.abapblog.adt.extension.preferences.PreferenceConstants;
import com.sap.adt.sapgui.ui.editors.AdtSapGuiEditorUtilityFactory;

public class TransactionInputControl extends WorkbenchWindowControlContribution {
	public static Text transactionInput;
	public static Button useCurrentSystem;
	private static Composite Composite;

	public TransactionInputControl() {
		// TODO Auto-generated constructor stub
	}

	public TransactionInputControl(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected org.eclipse.swt.widgets.Control createControl(Composite parent) {
		Composite = new Composite(parent, SWT.FILL);
		GridLayout compositeLayout = new GridLayout(3, false);
		Composite.setLayout(compositeLayout);
		createTransactionIputField(Composite);
//		createCurrentSystemCheckbox(Composite);
//		setToolbarVisibility(parent, Preferences.showTransactionInputOnToolbar());
//		addVisibilityListener(parent);
		return Composite;
	}

	private void setToolbarVisibility(Composite parent, Boolean visible) {
		for (Control child : parent.getParent().getChildren()) {
			child.setVisible(visible);
		}
		;
		parent.getParent().setVisible(visible);
		parent.setVisible(visible);
		for (Control child : parent.getChildren()) {
			child.setVisible(visible);
		}
		;
		for (Control child : Composite.getChildren()) {
			child.setVisible(visible);
		}
		Composite.setVisible(visible);
	}

	private void addVisibilityListener(Composite parent) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty() == PreferenceConstants.ShowTransactionInputOnToolbar) {
					setToolbarVisibility(parent, (boolean) event.getNewValue());
				}
			}
		});
	}

	private void createCurrentSystemCheckbox(Composite composite) {
		useCurrentSystem = new Button(composite, SWT.CHECK);
		useCurrentSystem.setText("Curr");
	}

	private void createTransactionIputField(Composite composite) {
		transactionInput = new Text(composite, SWT.BORDER_DOT);
		transactionInput.setText("Enter T-Code");
		transactionInput.setToolTipText("Enter Transaction Code");
		transactionInput.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				transactionInput.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				transactionInput.setText("Enter T-Code");
			}
		});
		transactionInput.setTextLimit(40);
		transactionInput.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
					IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IEditorPart activeEditor = activePage.getActiveEditor();
					if (activeEditor != null) {
						IEditorInput input = activeEditor.getEditorInput();
						IProject project = input.getAdapter(IProject.class);
						if (project == null) {
							IResource resource = input.getAdapter(IResource.class);
							if (resource != null) {
								project = resource.getProject();
								runTransaction(transactionInput, project);
							}
						} else {
							runTransaction(transactionInput, project);
						}
					}

				}
			}

			private void runTransaction(Text text, IProject project) {
				AdtSapGuiEditorUtilityFactory.createSapGuiEditorUtility().openEditorAndStartTransaction(project,
						text.getText().toUpperCase(), false);
				text.setText("Enter T-Code");
				;
			}
		});
	}

}
