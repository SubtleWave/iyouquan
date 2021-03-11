package psd;

import java.util.List;

public interface LayersContainer {
    public Layer getLayer(int index);
    public int indexOfLayer(Layer layer);
    public int getLayersCount();
    public List<Layer> getLayers();
}
