package org.free.config;

public interface Conf {
	int interval = 22;

	int POT_COLOR_DIFF = -20;

	boolean DEBUG = false;

	int scanLeft = 0;
	int scanTop = -20;
	int scanWidth = 50;
	int scanHeight = 50;
	int scanLight = 10;
	int scanBlock = 4;
	int scanInterval = 100;
	EditableConf EDITABLE_CONF = new EditableConf();
}
