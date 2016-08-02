package org.free.config;

public interface Conf {
	int interval = 22;

	int POT_COLOR_DIFF = 50;
	int POT_COLOR_SIZE = 5;

	boolean DEBUG = false;

	int scanLeft = 10;
	int scanTop = -10;
	int scanWidth = 30;
	int scanHeight = 30;
	int scanLight = 25;
	int scanBlock = 4;
	int scanInterval = 100;
	EditableConf EDITABLE_CONF = new EditableConf();
}
