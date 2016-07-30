package org.free.config;

public interface Conf {
	int interval = 22;
	
	boolean DEBUG = true;

	int scanLeft = 20;
	int scanTop = -10;
	int scanWidth = 30;
	int scanHeight = 30;
	int scanLight = 30;
	int scanBlock = 4;
	int scanInterval = 100;
	EditableConf EDITABLE_CONF = new EditableConf();
}
