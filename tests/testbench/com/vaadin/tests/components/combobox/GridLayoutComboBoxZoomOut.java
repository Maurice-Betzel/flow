package com.vaadin.tests.components.combobox;

import com.vaadin.tests.components.TestBase;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Select;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class GridLayoutComboBoxZoomOut extends TestBase {

    @Override
    public void setup() {
        Window mainWindow = new Window("Gridlayoutbug Application");
        setMainWindow(mainWindow);

        Label description = new Label(
                "Open this application in Chrome, zoom out (cmd + \"-\") and "
                        + "open the ComboBox for weird behaviour.");
        mainWindow.addComponent(description);

        Layout formLayout = new GridLayout(2, 1);
        // formLayout.setWidth("100%");
        formLayout.setWidth("1000px");

        Select countryField = new ComboBox();
        countryField.addItem("Finland");
        countryField.addItem("Sweden");
        countryField.addItem("Canada");
        countryField.addItem("USA");
        countryField.setCaption("Country");
        countryField.setWidth("100%");
        formLayout.addComponent(countryField);

        Select statusField = new ComboBox();
        statusField.addItem("Available");
        statusField.addItem("On vacation");
        statusField.addItem("Busy");
        statusField.addItem("Left the building");
        statusField.setCaption("Status");
        statusField.setWidth("100%");
        formLayout.addComponent(statusField);

        mainWindow.addComponent(formLayout);
    }

    @Override
    protected String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Integer getTicketNumber() {
        // TODO Auto-generated method stub
        return null;
    }

}
