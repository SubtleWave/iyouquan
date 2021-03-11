package psd.parser.layer;

public class Text {
    private String content;
    private String fontFamily;
    private double fontSize;
    private int fontWeight;
    private String color;
    private String writingDirection;//文字方向

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(int fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWritingDirection() {
        return writingDirection;
    }

    public void setWritingDirection(String writingDirection) {
        this.writingDirection = writingDirection;
    }
}
