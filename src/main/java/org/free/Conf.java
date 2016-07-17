package org.free;

public class Conf {

	static boolean isDouble = true;

	/**
	 * 鱼饵
	 */
	static boolean enBait = true;
	
	/**
	 * 鱼饵持续时间，单位分
	 */
	static int inBait = 10;
	
	/**
	 * 时间间隔，单位秒
	 */
	static final int interval = 22;
	
	static final boolean DEBUG = true;
	
	static CHECKING_COLOR checkColor = CHECKING_COLOR.RED;
	
	enum CHECKING_COLOR {
		BLUE, RED;
	}
}
