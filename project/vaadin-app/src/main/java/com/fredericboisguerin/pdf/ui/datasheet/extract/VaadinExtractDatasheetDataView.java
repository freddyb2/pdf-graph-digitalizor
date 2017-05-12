package com.fredericboisguerin.pdf.ui.datasheet.extract;

import java.util.Collection;
import java.util.List;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class VaadinExtractDatasheetDataView extends VerticalLayout
        implements View, ExtractDatasheetDataView {

    private static final String INFO_LABEL_FORMAT = "Extraction of data from %s";
    private static final String AXIS_X_CAPTION = "Axis X";
    private static final String AXIS_Y_CAPTION = "Axis Y";

    private final Label title;
    private final VaadinAxisView xAxisView = new VaadinAxisView();
    private final VaadinAxisView yAxisView = new VaadinAxisView();
    private final TwinColSelect<SerieViewModel> twinColSelect = new TwinColSelect<>();
    private final Button exportButton;

    private ExtractDatasheetDataViewListener listener;

    public VaadinExtractDatasheetDataView() {
        title = new Label();
        title.addStyleName(ValoTheme.LABEL_HUGE);

        VerticalLayout xAxisLayout = buildAxisLayout(AXIS_X_CAPTION, xAxisView);
        VerticalLayout yAxisLayout = buildAxisLayout(AXIS_Y_CAPTION, yAxisView);
        HorizontalLayout axesLayout = new HorizontalLayout(xAxisLayout, yAxisLayout);
        axesLayout.setComponentAlignment(xAxisLayout, Alignment.MIDDLE_RIGHT);
        axesLayout.setComponentAlignment(yAxisLayout, Alignment.MIDDLE_LEFT);

        exportButton = buildExportButton();

        axesLayout.setWidth(67, Unit.PERCENTAGE);
        twinColSelect.setWidth(67, Unit.PERCENTAGE);

        addComponents(title, axesLayout, twinColSelect, exportButton);

        setComponentAlignment(axesLayout, Alignment.MIDDLE_CENTER);
        setComponentAlignment(twinColSelect, Alignment.MIDDLE_CENTER);
        setComponentAlignment(exportButton, Alignment.MIDDLE_CENTER);

        xAxisView.getFocus();
    }

    private Button buildExportButton() {
        Button export = new Button("Export");
        StreamResource resource = new StreamResource(
                (StreamSource) () -> listener.getExportInputStream(), "report.xls");
        FileDownloader fileDownloader = new FileDownloader(resource);
        fileDownloader.extend(export);
        return export;
    }

    private void onStatusChanged() {
        boolean formIsValid = xAxisView.isValid() && yAxisView.isValid();
        exportButton.setEnabled(formIsValid);
    }

    private VerticalLayout buildAxisLayout(String caption, VaadinAxisView axisView) {
        Label label = new Label(caption);
        axisView.addStatusChangeListener(event -> onStatusChanged());
        VerticalLayout layout = new VerticalLayout(label, axisView);
        layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        layout.setSizeUndefined();
        return layout;
    }

    @Override
    public void setListener(ExtractDatasheetDataViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void notifyError(String message) {
        Notification.show(message, Notification.Type.ERROR_MESSAGE);
    }

    @Override
    public void setSeries(List<SerieViewModel> serieViewModels) {
        ListDataProvider<SerieViewModel> dataProvider = new ListDataProvider<>(serieViewModels);
        twinColSelect.setDataProvider(dataProvider);
    }

    @Override
    public void setDatasheetInfo(String datasheetInfo) {
        title.setValue(String.format(INFO_LABEL_FORMAT, datasheetInfo));
    }

    @Override
    public void setAxesViewModels(AxesViewModel axesViewModel) {
        AxisViewModel xAxisModel = axesViewModel.getAxisX();
        xAxisView.setModel(xAxisModel);
        AxisViewModel yAxisModel = axesViewModel.getAxisY();
        yAxisView.setModel(yAxisModel);
    }

    @Override
    public Collection<SerieViewModel> getSelectedSeries() {
        return twinColSelect.getSelectedItems();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        listener.onViewEntered(event.getParameters());
    }
}
