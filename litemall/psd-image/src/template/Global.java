package template;


/**
 * "zoom": 0.28306159420289856,
 *     "dpi": 72,
 *     "showWatermark": false,
 *     "source": "ttxs",
 *     "type": "poster",
 *     "effectImage":
 */
public class Global {
    private double zoom = 0.28306159420289856;
    private int dpi = 72;
    private boolean showWatermark = false;
    private String source = "ttxs";
    private String type = "poster";
    private String effectImage;

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public boolean isShowWatermark() {
        return showWatermark;
    }

    public void setShowWatermark(boolean showWatermark) {
        this.showWatermark = showWatermark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEffectImage() {
        return effectImage;
    }

    public void setEffectImage(String effectImage) {
        this.effectImage = effectImage;
    }
}
