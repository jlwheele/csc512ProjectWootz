import java.util.HashMap;

public class PrototxtData {

    private String name;
    private String input;
    private int[] input_shape;
    private HashMap<String, PrototxtLayer> layersMap;
    private PrototxtLayer[] layers;


    public PrototxtData() {
        name = null;
        input = null;
        input_shape = new int[0];
        layersMap = new HashMap<String, PrototxtLayer>();
        layers = new PrototxtLayer[0];
    }

    public void addInputShapeDim(int d) {
        int length = input_shape.length;
        int[] tmp = new int[length + 1];

        for (int i = 0; i < length; i++) {
            tmp[i] = input_shape[i];
        }

        tmp[length] = d;
        input_shape = tmp;
    }

    public void addLayer(PrototxtLayer layer) {
        layersMap.put(layer.getName(), layer);

        int length = layers.length;
        PrototxtLayer[] tmp = new PrototxtLayer[length + 1];

        for (int i = 0; i < length; i++) {
            tmp[i] = layers[i];
        }

        tmp[length] = layer;
        layers = tmp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public int[] getInputShape() {
        return input_shape;
    }

    public HashMap getLayersMap() {
        return layersMap;
    }

    public PrototxtLayer[] getLayers() {
        return layers;
    }
}
