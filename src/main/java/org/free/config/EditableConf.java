package org.free.config;

/**
 * Created by byang1 on 7/30/16.
 */
public class EditableConf {
    private int scanLight = Conf.scanLight;
    public int getScanLight(){
        return this.scanLight;
    }
    public void updateScanLight(int scanLight){
        this.scanLight = scanLight;
    }

    private int minRed = Conf.MIN_RED;
    public int getMinRed(){return this.minRed;}
    public void setMinRed(int minRed){this.minRed = minRed;}
}
