package com.ncsu.csc512.jlwheele;

public class Param implements PrototxtLayerParam {

    private int lr_mult;
    private int decay_mult;

    public Param() {
        lr_mult = 1;
        decay_mult = 1;
    }

    public int getLr_mult() {
        return lr_mult;
    }

    public void setLr_mult(int lr_mult) {
        this.lr_mult = lr_mult;
    }

    public int getDecay_mult() {
        return decay_mult;
    }

    public void setDecay_mult(int decay_mult) {
        this.decay_mult = decay_mult;
    }
}
