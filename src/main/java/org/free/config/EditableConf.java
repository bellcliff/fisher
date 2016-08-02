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

    private int potColorDiff = Conf.POT_COLOR_DIFF;
    public int getPotColorDiff() {
        return potColorDiff;
    }
    public void updatePotColorDiff(int plus){
        this.potColorDiff += plus;
    }
    public void resetPotColorDiff(){
        this.potColorDiff = Conf.POT_COLOR_DIFF;
    }

    private int potColorSize = Conf.POT_COLOR_SIZE;
    public int getPotColorSize() {
        return potColorSize;
    }
    public void updatePotColorSize(int plus) {
        this.potColorSize += plus;
    }
    public void resetPotColorSize(){
        this.potColorSize = Conf.POT_COLOR_SIZE;
    }
}
