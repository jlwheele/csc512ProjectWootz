package com.ncsu.csc512.jlwheele;

public class BatchNormParam implements PrototxtLayerParam {

    private boolean use_global_stats;
    private double eps;

    public BatchNormParam() {
        use_global_stats = true;
        eps = 0;
    }

    public boolean isUseGlobalStats() {
        return use_global_stats;
    }

    public void setUseGlobalStats(boolean use_global_stats) {
        this.use_global_stats = use_global_stats;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }
}
