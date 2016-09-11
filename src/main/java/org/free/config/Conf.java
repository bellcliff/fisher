package org.free.config;

public interface Conf {
	int interval = 22;

	int scanLeft = -25;
	int scanTop = 0;
	int scanWidth = 80;
	int scanHeight = 40;
	int scanLight = 10;
	int scanInterval = 100;
	int scanRange = 5;

	int MIN_RED = 50;
	EditableConf EDITABLE_CONF = new EditableConf();
}
