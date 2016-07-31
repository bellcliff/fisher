package org.free.config;

public interface Conf {
	int interval = 22;
	
	boolean DEBUG = false;

	int scanLeft = 0;
	int scanTop = -10;
	int scanWidth = 40;
	int scanHeight = 40;
	int scanLight = 20;
	int scanBlock = 4;
	int scanInterval = 100;
	EditableConf EDITABLE_CONF = new EditableConf();
}
