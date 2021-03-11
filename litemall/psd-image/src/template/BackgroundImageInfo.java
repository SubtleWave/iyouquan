package template;

/**
 * height* 100
 * opacity* 1
 * transform* ...
 * width* 100
 */
public class BackgroundImageInfo {
    private int height;
    private double opacity = 1;
    private int width;
    private Transform transform = new Transform();

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
