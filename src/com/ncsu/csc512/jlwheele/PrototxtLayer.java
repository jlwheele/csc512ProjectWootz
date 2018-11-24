package com.ncsu.csc512.jlwheele;

public class PrototxtLayer {

    private String name;
    private String type;
    private String bottom;
    private String bottom2;
    private String bottom3;
    private String bottom4;
    private String top;
    private PrototxtLayerParam param;
    private PrototxtLayerParam param2;

    private final String TYPE_SOFTMAX = "Softmax";
    private final String TYPE_RELU = "ReLU";
    private final String TYPE_CONCAT = "Concat";
    private final String TYPE_CONVOLUTION = "Convolution";
    private final String TYPE_BATCHNORM = "BatchNorm";
    private final String TYPE_SCALE = "Scale";
    private final String TYPE_POOLING = "Pooling";
    private final String TYPE_DROPOUT = "Dropout";
    private final String TYPE_RESHAPE = "Reshape";

    public PrototxtLayer() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getBottom2() {
        return bottom2;
    }

    public void setBottom2(String bottom2) {
        this.bottom2 = bottom2;
    }

    public String getBottom3() {
        return bottom3;
    }

    public void setBottom3(String bottom3) {
        this.bottom3 = bottom3;
    }

    public String getBottom4() {
        return bottom4;
    }

    public void setBottom4(String bottom4) {
        this.bottom4 = bottom4;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public PrototxtLayerParam getParam() {
        return param;
    }

    public void setParam(PrototxtLayerParam param) {
        this.param = param;
    }

    public PrototxtLayerParam getParam2() {
        return param2;
    }

    public void setParam2(PrototxtLayerParam param2) {
        this.param2 = param2;
    }
}
