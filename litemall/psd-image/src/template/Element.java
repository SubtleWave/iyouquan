package template;


import java.util.ArrayList;
import java.util.List;

/**
 * aggregatedColors * ...
 * backgroundColor* null
 * borderRadius* 0
 * boxShadow* null
 * category* "B1"
 * color* "#e55c7cff"
 * dragable* true
 * editable* true
 * effectScale* 1
 * filter* ...
 * frozen* false
 * height* 420
 * hidden* false
 * left* 147
 * lock* false
 * mainColor* null
 * opacity* 1
 * padding* ...
 * resize* 7
 * rotatable* true
 * top* 1179
 * transform* ...
 * type* "text"
 * version* "5.7.0"
 * width* 948
 * <p>
 * <p>
 * <p>
 * 文字专属
 * contents* null
 * autoScale* false
 * content* "最专业的微商作图服务"
 * fontFamily* "SourceHanSerifSC-Light"
 * fontSize* 47.999932489451474
 * fontStyle* "normal"
 * fontWeight* 400
 * letterSpacing* 0
 * lineHeight* 1.375
 * textAlign* "center"
 * textDecoration* "none"
 * textEffects* ...
 * textShadow* null
 * verticalAlign* "top"
 * writingMode* "lr－tb"
 * <p>
 * <p>
 * <p>
 * 图片专属
 * effectedImage*
 * effectedImageHeight *0
 * effectedImageOffsetLeft *0
 * effectedImageOffsetTop * 0
 * effectedImageWidth* 0
 * imageEffects * ...
 * imageEffectsHash * ""
 * originHeight* 0
 * originWidth* 1
 * url * http
 */
public class Element {
    private String backgroundColor;
    private int borderRadius = 0;
    private String boxShadow;
    private String category;
    private String color;
    private boolean dragable = true;
    private boolean editable = true;
    private double effectScale = 1;
    private boolean frozen = false;
    private boolean hidden = false;
    private int height;
    private int left;
    private boolean lock = false;
    private String mainColor;
    private double opacity = 1;
    private int resize;
    private boolean rotatable = true;
    private int top;
    private String type;
    private String version = "5.7.0";
    private int width;
    private String[] aggregatedColors;// 数组"rgb(229, 92, 124)"
    private int[] padding = {0,0,0,0};
    private Transform transform = new Transform();
    private Filter filter;

    //文字专有
    private String contents;
    private boolean autoScale = false;
    private String content;
    private String fontFamily;
    private double fontSize;
    private String fontStyle = "normal";
    private int fontWeight;
    private double letterSpacing = 0;
    private double lineHeight = 1;
    private String textAlign = "center";
    private String textDecoration = "none";
    private String textShadow;
    private String verticalAlign = "top";
    private String writingMode = "lr－tb";
    private List<TextEffect> textEffects = new ArrayList<TextEffect>();



    //图片专有
    private String effectedImage;
    private int effectedImageHeight = 0;
    private int effectedImageOffsetLeft = 0;
    private int effectedImageOffsetTop = 0;
    private int effectedImageWidth = 0;
    private String imageEffectsHash;
    private int originHeight = 0;
    private int originWidth = 1;
    private String url;
    private List<ImageEffect> imageEffects = new ArrayList<ImageEffect>();

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    public String getBoxShadow() {
        return boxShadow;
    }

    public void setBoxShadow(String boxShadow) {
        this.boxShadow = boxShadow;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDragable() {
        return dragable;
    }

    public void setDragable(boolean dragable) {
        this.dragable = dragable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public double getEffectScale() {
        return effectScale;
    }

    public void setEffectScale(double effectScale) {
        this.effectScale = effectScale;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public int getResize() {
        return resize;
    }

    public void setResize(int resize) {
        this.resize = resize;
    }

    public boolean isRotatable() {
        return rotatable;
    }

    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String[] getAggregatedColors() {
        return aggregatedColors;
    }

    public void setAggregatedColors(String[] aggregatedColors) {
        this.aggregatedColors = aggregatedColors;
    }

    public int[] getPadding() {
        return padding;
    }

    public void setPadding(int[] padding) {
        this.padding = padding;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }

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

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(int fontWeight) {
        this.fontWeight = fontWeight;
    }

    public double getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(double letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    public double getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(double lineHeight) {
        this.lineHeight = lineHeight;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public String getTextDecoration() {
        return textDecoration;
    }

    public void setTextDecoration(String textDecoration) {
        this.textDecoration = textDecoration;
    }

    public String getTextShadow() {
        return textShadow;
    }

    public void setTextShadow(String textShadow) {
        this.textShadow = textShadow;
    }

    public String getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public String getWritingMode() {
        return writingMode;
    }

    public void setWritingMode(String writingMode) {
        this.writingMode = writingMode;
    }

    public List<TextEffect> getTextEffects() {
        return textEffects;
    }

    public void setTextEffects(List<TextEffect> textEffects) {
        this.textEffects = textEffects;
    }

    public String getEffectedImage() {
        return effectedImage;
    }

    public void setEffectedImage(String effectedImage) {
        this.effectedImage = effectedImage;
    }

    public int getEffectedImageHeight() {
        return effectedImageHeight;
    }

    public void setEffectedImageHeight(int effectedImageHeight) {
        this.effectedImageHeight = effectedImageHeight;
    }

    public int getEffectedImageOffsetLeft() {
        return effectedImageOffsetLeft;
    }

    public void setEffectedImageOffsetLeft(int effectedImageOffsetLeft) {
        this.effectedImageOffsetLeft = effectedImageOffsetLeft;
    }

    public int getEffectedImageOffsetTop() {
        return effectedImageOffsetTop;
    }

    public void setEffectedImageOffsetTop(int effectedImageOffsetTop) {
        this.effectedImageOffsetTop = effectedImageOffsetTop;
    }

    public int getEffectedImageWidth() {
        return effectedImageWidth;
    }

    public void setEffectedImageWidth(int effectedImageWidth) {
        this.effectedImageWidth = effectedImageWidth;
    }

    public String getImageEffectsHash() {
        return imageEffectsHash;
    }

    public void setImageEffectsHash(String imageEffectsHash) {
        this.imageEffectsHash = imageEffectsHash;
    }

    public int getOriginHeight() {
        return originHeight;
    }

    public void setOriginHeight(int originHeight) {
        this.originHeight = originHeight;
    }

    public int getOriginWidth() {
        return originWidth;
    }

    public void setOriginWidth(int originWidth) {
        this.originWidth = originWidth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ImageEffect> getImageEffects() {
        return imageEffects;
    }

    public void setImageEffects(List<ImageEffect> imageEffects) {
        this.imageEffects = imageEffects;
    }
}
