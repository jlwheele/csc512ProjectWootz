public class PrototxtLayer {

    private String name;
    private String type;
    private String bottom;
    private String bottom2;
    private String bottom3;
    private String bottom4;
    private String[] bottoms;
    private int bIdx;
    private String top;
    private PrototxtLayerParam param;
    private PrototxtLayerParam param2;

    public final static String TYPE_SOFTMAX = "Softmax";
    public final static String TYPE_RELU = "ReLU";
    public final static String TYPE_CONCAT = "Concat";
    public final static String TYPE_CONVOLUTION = "Convolution";
    public final static String TYPE_BATCHNORM = "BatchNorm";
    public final static String TYPE_SCALE = "Scale";
    public final static String TYPE_POOLING = "Pooling";
    public final static String TYPE_DROPOUT = "Dropout";
    public final static String TYPE_RESHAPE = "Reshape";

    public PrototxtLayer() {
        name = null;
        type = null;
        bottom = null;
        bottom2 = null;
        bottom3 = null;
        bottom4 = null;
        top = null;
        param = null;
        param2 = null;
        bottoms = new String[4];
        bIdx = 0;
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

    public String[] getBottoms() {
        return bottoms;
    }

    public void setBottom(String bottom) {
        if (this.bottom == null)
            this.bottom = bottom;
        else if (type.equals(TYPE_CONCAT)) {
            if (this.bottom2 == null)
                this.bottom2 = bottom;
            else if (this.bottom3 == null)
                this.bottom3 = bottom;
            else if (this.bottom4 == null)
                this.bottom4 = bottom;
        }

        bottoms[bIdx] = bottom;
        bIdx++;
    }

    public String getBottom2() {
        return bottom2;
    }

//    public void setBottom2(String bottom2) {
//        this.bottom2 = bottom2;
//    }

    public String getBottom3() {
        return bottom3;
    }

//    public void setBottom3(String bottom3) {
//        this.bottom3 = bottom3;
//    }

    public String getBottom4() {
        return bottom4;
    }

//    public void setBottom4(String bottom4) {
//        this.bottom4 = bottom4;
//    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public PrototxtLayerParam getParam() {
        return param;
    }

//    public void setParam(PrototxtLayerParam param) {
//        this.param = param;
//    }

    public PrototxtLayerParam getParam2() {
        return param2;
    }

//    public void setParam2(PrototxtLayerParam param2) {
//        this.param2 = param2;
//    }

    public void defineParam(String[] definitions) {
//        System.out.println("param: " + Arrays.toString(definitions));

        if (type.equals(TYPE_CONVOLUTION)) {
            if (param == null) {
                param = new Param();
                for (String definition : definitions) {
                    String[] def = definition.split(":");
                    String var = def[0];
                    if (var.equals("lr_mult")) {
                        ((Param) param).setLr_mult(Integer.valueOf(def[1]));
                    } else if (var.equals("decay_mult")) {
                        ((Param) param).setDecay_mult(Integer.valueOf(def[1]));
                    }
                }
            } else {
                param2 = new ConvolutionParam();
                for (String definition : definitions) {
                    if (definition.startsWith("weight_filler")) {
                        String[] wfs = definition.substring(definition.indexOf('{') + 1, definition.lastIndexOf('}')).split("&");
                        for (String wf : wfs) {
                            String[] d = wf.split(":");
                            String v = d[0];
                            if (v.equals("type")) {
                                ((ConvolutionParam) param2).setWeightFillerType(d[1]);
                            } else if (v.equals("std")) {
                                ((ConvolutionParam) param2).setWeightFillerStd(Double.valueOf(d[1]));
                            }
                        }
                    } else {
                        String[] def = definition.split(":");
                        String var = def[0];
                        if (var.equals("bias_term")) {
                            ((ConvolutionParam) param2).setBiasTerm(Boolean.valueOf(def[1]));
                        } else if (var.equals("num_output")) {
                            ((ConvolutionParam) param2).setNumOutputs(Integer.valueOf(def[1]));
                        } else if (var.equals("pad")) {
                            ((ConvolutionParam) param2).setPad(Integer.valueOf(def[1]));
                        } else if (var.equals("kernel_size")) {
                            ((ConvolutionParam) param2).setKernelSize(Integer.valueOf(def[1]));
                        } else if (var.equals("stride")) {
                            ((ConvolutionParam) param2).setStride(Integer.valueOf(def[1]));
                        }
                    }
                }
            }
        } else if (type.equals(TYPE_BATCHNORM)) {
            param = new BatchNormParam();
            for (String definition : definitions) {
                String[] def = definition.split(":");
                String var = def[0];
                if (var.equals("use_global_stats")) {
                    ((BatchNormParam) param).setUseGlobalStats(Boolean.valueOf(def[1]));
                } else if (var.equals("eps")) {
                    ((BatchNormParam) param).setEps(Double.valueOf(def[1]));
                }
            }
        } else if (type.equals(TYPE_SCALE)) {
            param = new ScaleParam();
            ((ScaleParam) param).setBiasTerm(Boolean.valueOf(definitions[0].split(":")[1]));
        } else if (type.equals(TYPE_POOLING)) {
            param = new PoolingParam();
            for (String definition : definitions) {
                String[] def = definition.split(":");
                String var = def[0];

                if (var.equals("pool")) {
                    ((PoolingParam) param).setPool(def[1]);
                } else if (var.equals("kernel_size")) {
                    ((PoolingParam) param).setKernelSize(Integer.valueOf(def[1]));
                } else if (var.equals("stride")) {
                    ((PoolingParam) param).setStride(Integer.valueOf(def[1]));
                } else if (var.equals("pad")) {
                    ((PoolingParam) param).setPad(Integer.valueOf(def[1]));
                } else if (var.equals("global_pooling")) {
                    ((PoolingParam) param).setGlobalPooling(Boolean.valueOf(def[1]));
                }
            }
        } else if (type.equals(TYPE_DROPOUT)) {
            param = new DropoutParam();
            ((DropoutParam) param).setDropoutRatio(Double.valueOf(definitions[0].split(":")[1]));
        } else if (type.equals(TYPE_RESHAPE)) {
            param = new ReshapeParam();
            String[] shape = definitions[0].substring( definitions[0].indexOf('{') + 1,  definitions[0].lastIndexOf('}')).split("&");
            int d1 = Integer.valueOf(shape[0].split(":")[1]);
            int d2 = Integer.valueOf(shape[1].split(":")[1]);
            ((ReshapeParam) param).setShapeDims(d1, d2);
        }

    }
}
