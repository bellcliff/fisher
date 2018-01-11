package org.free.config;

public interface Conf {
	int interval = 22;

	int scanLeft = -15;
	int scanTop = 0;
	int scanWidth = 50;
	int scanHeight = 30;
	int scanLight = 20;
	int scanInterval = 100;
	int scanRange = 3;

	int screenTop = 400;
	int screenWidth = 400;
	int screenHeight = 120;

	int MIN_RED = 100;
	int RED_THRESHOLD = 80;
	EditableConf EDITABLE_CONF = new EditableConf();
}
