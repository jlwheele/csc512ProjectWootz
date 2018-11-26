package com.ncsu.csc512.jlwheele;

public class ConvolutionParam implements PrototxtLayerParam {

    private boolean biasTerm;
    private int numOutputs;
    private int pad;
    private int kernelSize;
    private int stride;
    private String weightFillerType;
    private double weightFillerStd;

    public ConvolutionParam() {
        biasTerm = false;
        numOutputs = 0;
        pad = 0;
        kernelSize = 0;
        stride = 1;
        weightFillerType = "";
        weightFillerStd = 0;
    }

    public boolean isBiasTerm() {
        return biasTerm;
    }

    public void setBiasTerm(boolean biasTerm) {
        this.biasTerm = biasTerm;
    }

    public int getNumOutputs() {
        return numOutputs;
    }

    public void setNumOutputs(int numOutputs) {
        this.numOutputs = numOutputs;
    }

    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public int getKernelSize() {
        return kernelSize;
    }

    public void setKernelSize(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public int getStride() {
        return stride;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }

    public String getWeightFillerType() {
        return weightFillerType;
    }

    public void setWeightFillerType(String weightFillerType) {
        this.weightFillerType = weightFillerType;
    }

    public double getWeightFillerStd() {
        return weightFillerStd;
    }

    public void setWeightFillerStd(double weightFillerStd) {
        this.weightFillerStd = weightFillerStd;
    }
}
