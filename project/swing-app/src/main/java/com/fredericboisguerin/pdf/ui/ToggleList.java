package com.fredericboisguerin.pdf.ui;

import javax.swing.*;
import java.util.Enumeration;
import java.util.List;

public class ToggleList<T> {
    private final DefaultListModel<T> sourceListModel;
    private final DefaultListModel<T> destListModel;
    private JList<T> sourceList;
    private JList<T> destList;
    private JButton addButton;
    private JButton removeButton;
    private JPanel mainPanel;

    public ToggleList() {
        sourceListModel = new DefaultListModel<>();
        sourceList.setModel(sourceListModel);

        destListModel = new DefaultListModel<>();
        destList.setModel(destListModel);

        addButton.addActionListener(e -> ToggleList.this.exchangeElements(sourceList
                        .getSelectedValuesList(),
                sourceListModel, destListModel));
        removeButton.addActionListener(e -> ToggleList.this.exchangeElements(destList
                        .getSelectedValuesList(), destListModel,
                sourceListModel));
    }

    private void exchangeElements(List<T> elements, DefaultListModel<T> sourceListModel,
            DefaultListModel<T> destListModel) {
        for (T element : elements) {
            sourceListModel.removeElement(element);
            destListModel.addElement(element);
        }
    }

    public void setElements(List<T> elements) {
        sourceListModel.removeAllElements();
        destListModel.removeAllElements();
        for (T element : elements) {
            sourceListModel.addElement(element);
        }
    }

    public Enumeration<T> getSelectedElements() {
        return destListModel.elements();
    }

}
