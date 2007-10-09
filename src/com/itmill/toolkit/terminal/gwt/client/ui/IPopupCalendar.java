package com.itmill.toolkit.terminal.gwt.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.itmill.toolkit.terminal.gwt.client.ApplicationConnection;
import com.itmill.toolkit.terminal.gwt.client.Paintable;
import com.itmill.toolkit.terminal.gwt.client.UIDL;

public class IPopupCalendar extends ITextualDate implements Paintable,
		ClickListener, PopupListener {

	private IButton calendarToggle;

	private CalendarPanel calendar;

	private PopupPanel popup;

	public IPopupCalendar() {
		super();

		calendarToggle = new IButton();
		calendarToggle.setText("...");
		calendarToggle.addClickListener(this);
		add(calendarToggle);

		calendar = new CalendarPanel(this);
		popup = new PopupPanel(true);
		popup.setStyleName(IDateField.CLASSNAME + "-calendar");
		popup.setWidget(calendar);
		popup.addPopupListener(this);
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		if (date != null)
			calendar.updateCalendar();
		calendarToggle.setEnabled(enabled);
	}

	public void onClick(Widget sender) {
		if (sender == calendarToggle) {
			calendar.updateCalendar();
			popup.show();
			int w = calendar.getOffsetWidth();
			int h = calendar.getOffsetHeight();
			int t = calendarToggle.getAbsoluteTop();
			int l = calendarToggle.getAbsoluteLeft();
			if (l + w > Window.getClientWidth())
				l = Window.getClientWidth() - w;
			if (t + h > Window.getClientHeight())
				t = Window.getClientHeight() - h
						- calendarToggle.getOffsetHeight() - 2;
			popup.setPopupPosition(l, t + calendarToggle.getOffsetHeight() + 2);
			popup.setWidth(w + "px");
			popup.setHeight(h + "px");
		}
	}

	public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
		if (sender == popup)
			buildDate();
	}

}
