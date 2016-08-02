package org.free.config;

public interface Conf {
	int interval = 22;

	int POT_COLOR_DIFF = 100;
	int POT_COLOR_SIZE = 5;

	boolean DEBUG = false;

	int scanLeft = 15;
	int scanTop = -15;
	int scanWidth = 30;
	int scanHeight = 30;
	int scanLight = 40;
	int scanInterval = 100;
	EditableConf EDITABLE_CONF = new EditableConf();
}
