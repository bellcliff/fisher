package org.free.config;

public interface Conf {
	int interval = 22;

	int POT_COLOR_DIFF = 50;
	int POT_COLOR_SIZE = 5;

	boolean DEBUG = false;

	int scanLeft = 5;
	int scanTop = -15;
	int scanWidth = 50;
	int scanHeight = 30;
	int scanLight = 25;
	int scanInterval = 100;
	EditableConf EDITABLE_CONF = new EditableConf();
}
