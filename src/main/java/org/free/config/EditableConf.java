package org.free.config;

/**
 * Created by byang1 on 7/30/16.
 */
public class EditableConf {
    private int scanLight = 0;
    public int getScanLight(){
        return scanLight == 0 ? Conf.scanLight : this.scanLight;
    }

    public void updateScanLight(int scanLight){
        this.scanLight = scanLight;
    }
}
