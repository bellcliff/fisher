package org.free.config;

public interface Conf {
	int interval = 22;

	int POT_COLOR_DIFF = 80;
	int POT_COLOR_SIZE = 5;

	int scanLeft = 0;
	int scanTop = 0;
	int scanLeftBlue = -20;
	int scanTopBlue = 0;
	int scanWidth = 40;
	int scanHeight = 40;
	int scanLight = 10;
	int scanInterval = 200;

	int MIN_RED = 150;
	EditableConf EDITABLE_CONF = new EditableConf();
}
