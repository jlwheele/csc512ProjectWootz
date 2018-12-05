public class PoolingParam implements PrototxtLayerParam {

    private String pool;
    private int kernelSize;
    private int stride;
    private int pad;
    private boolean globalPooling;

    public PoolingParam() {
        pool = "";
        kernelSize = 0;
        stride = 1;
        pad = 0;
        globalPooling = false;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
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

    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public boolean isGlobalPooling() {
        return globalPooling;
    }

    public void setGlobalPooling(boolean globalPooling) {
        this.globalPooling = globalPooling;
    }
}
