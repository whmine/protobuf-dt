/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.ui.preferences.compiler;

import static com.google.eclipse.protobuf.ui.preferences.compiler.Messages.*;
import static java.util.Collections.unmodifiableList;
import static org.eclipse.jface.window.Window.OK;

import java.util.*;
import java.util.List;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.xtext.ui.PluginImageHelper;

import com.google.eclipse.protobuf.ui.preferences.DataChangedListener;

/**
 * Editor where users can specify which are the target languages for protoc and the location of the output folders for
 * each language.
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class CodeGenerationEditor extends Composite {

  private final Table tblCodeGenerationOptions;
  private final TableViewer tblVwrCodeGenerationOptions;
  private final Button btnEdit;

  private final List<CodeGeneration> options = new ArrayList<CodeGeneration>();

  private DataChangedListener dataChangedListener;

  public CodeGenerationEditor(Composite parent, final PluginImageHelper imageHelper) {
    super(parent, SWT.NONE);
    setLayout(new GridLayout(1, false));

    tblVwrCodeGenerationOptions = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);

    tblCodeGenerationOptions = tblVwrCodeGenerationOptions.getTable();
    tblCodeGenerationOptions.setHeaderVisible(true);
    tblCodeGenerationOptions.setLinesVisible(true);
    tblCodeGenerationOptions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    TableViewerColumn tblclmnVwrEnabled = new TableViewerColumn(tblVwrCodeGenerationOptions, SWT.NONE);
    TableColumn tblclmnEnabled = tblclmnVwrEnabled.getColumn();
    tblclmnEnabled.setResizable(false);
    tblclmnEnabled.setWidth(27);
    tblclmnVwrEnabled.setLabelProvider(new ColumnLabelProvider() {
      @Override public String getText(Object element) {
        return ""; //$NON-NLS-1$
      }

      @Override public Image getImage(Object element) {
        boolean optionEnabled = ((CodeGeneration)element).isEnabled();
        return imageHelper.getImage(optionEnabled ? "checked.gif" : "unchecked.gif"); //$NON-NLS-1$ //$NON-NLS-2$
      }
    });

    TableViewerColumn tblclmnVwrLanguage = new TableViewerColumn(tblVwrCodeGenerationOptions, SWT.NONE);
    TableColumn tblclmnLanguage = tblclmnVwrLanguage.getColumn();
    tblclmnLanguage.setResizable(false);
    tblclmnLanguage.setWidth(100);
    tblclmnLanguage.setText(language);
    tblclmnVwrLanguage.setLabelProvider(new ColumnLabelProvider() {
      @Override public String getText(Object element) {
        return ((CodeGeneration)element).language().name();
      }
    });

    TableViewerColumn tblclmnVwrOutputDirectory = new TableViewerColumn(tblVwrCodeGenerationOptions, SWT.NONE);
    TableColumn tblclmnOutputDirectory = tblclmnVwrOutputDirectory.getColumn();
    tblclmnOutputDirectory.setResizable(false);
    tblclmnOutputDirectory.setWidth(100);
    tblclmnOutputDirectory.setText(outputDirectory);
    tblclmnVwrOutputDirectory.setLabelProvider(new ColumnLabelProvider() {
      @Override public String getText(Object element) {
        return ((CodeGeneration)element).outputDirectory();
      }
    });

    btnEdit = new Button(this, SWT.NONE);
    btnEdit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    btnEdit.setText(editSelected);

    tblVwrCodeGenerationOptions.setContentProvider(ArrayContentProvider.getInstance());

    addEventListeners();
    updateTable();
  }

  private void addEventListeners() {
    tblCodeGenerationOptions.addSelectionListener(new SelectionAdapter() {
      @Override public void widgetSelected(SelectionEvent e) {
        btnEdit.setEnabled(tblCodeGenerationOptions.getSelectionIndex() > -1);
      }
    });
    btnEdit.addSelectionListener(new SelectionAdapter() {
      @Override public void widgetSelected(SelectionEvent e) {
        int selectionIndex = tblCodeGenerationOptions.getSelectionIndex();
        CodeGeneration option = options.get(selectionIndex);
        EditCodeGenerationDialog dialog = new EditCodeGenerationDialog(getShell(), option);
        if (dialog.open() == OK) {
          tblVwrCodeGenerationOptions.refresh();
          if (dataChangedListener != null) dataChangedListener.dataChanged();
        }
      }
    });
  }

  public List<CodeGeneration> codeGenerationOptions() {
    return unmodifiableList(options);
  }

  public void codeGenerationOptions(List<CodeGeneration> newOptions) {
    options.clear();
    options.addAll(newOptions);
    updateTable();
  }

  private void updateTable() {
    tblVwrCodeGenerationOptions.setInput(options);
    if (!options.isEmpty()) tblCodeGenerationOptions.setSelection(0);
  }

  /** {@inheritDoc} */
  @Override public void setEnabled(boolean enabled) {
    tblCodeGenerationOptions.setEnabled(enabled);
    btnEdit.setEnabled(enabled);
    super.setEnabled(enabled);
  }

  public void setDataChangedListener(DataChangedListener listener) {
    dataChangedListener = listener;
  }
}
