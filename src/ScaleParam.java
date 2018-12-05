public class ScaleParam implements PrototxtLayerParam {

    private boolean bias_term;

    public ScaleParam() {
        bias_term = false;
    }

    public boolean isBiasTerm() {
        return bias_term;
    }

    public void setBiasTerm(boolean bias_term) {
        this.bias_term = bias_term;
    }
}
