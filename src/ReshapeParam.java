public class ReshapeParam implements PrototxtLayerParam {

    private int[] shapeDims;

    public ReshapeParam() {
        shapeDims = new int[2];
    }

    public int[] getShapeDims() {
        return shapeDims;
    }

    public void setShapeDims(int dim1, int dim2) {
        shapeDims[0] = dim1;
        shapeDims[1] = dim2;
    }
}
