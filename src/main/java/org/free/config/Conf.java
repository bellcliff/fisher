package org.free.config;

public interface Conf {
	int interval = 17;

	int scanLeft = -15;
	int scanTop = 0;
	int scanWidth = 50;
	int scanHeight = 30;
	int scanLight = 20;
	int scanInterval = 20;
	int scanRange = 3;

	int screenTop = 400;
	int screenWidth = 400;
	int screenHeight = 120;

	int MIN_RED = 50;
	EditableConf EDITABLE_CONF = new EditableConf();
}
