public class DropoutParam implements PrototxtLayerParam {

    private double dropout_ratio;

    public DropoutParam() {
        dropout_ratio = 0;
    }

    public double getDropoutRatio() {
        return dropout_ratio;
    }

    public void setDropoutRatio(double dropout_ratio) {
        this.dropout_ratio = dropout_ratio;
    }
}
