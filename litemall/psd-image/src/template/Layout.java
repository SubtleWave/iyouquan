package template;

import java.util.ArrayList;
import java.util.List;

/**
 * backgroundColor* "#ffffffff"
 * backgroundImage* "https
 * backgroundImageInfo* ...
 * backgroundRepeat* "no-repeat"
 * backgroundSize* null
 * className* null
 * effectImage* "https
 * elements* ...
 * height* 2208
 * repeatGroup* null
 * repeatId* null
 * title* ""
 * top* 0
 * width* 1242
 */
public class Layout {
    private int height;
    private int top;
    private int width;
    private String title;

    private String backgroundColor = "#ffffffff";
    private String backgroundImage;
    private String backgroundRepeat = "no-repeat";
    private String backgroundSize;
    private String className;
    private String effectImage;
    private String repeatGroup;
    private String repeatId;
    private BackgroundImageInfo backgroundImageInfo;
    private List<Element> elements = new ArrayList<Element>();

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundRepeat() {
        return backgroundRepeat;
    }

    public void setBackgroundRepeat(String backgroundRepeat) {
        this.backgroundRepeat = backgroundRepeat;
    }

    public String getBackgroundSize() {
        return backgroundSize;
    }

    public void setBackgroundSize(String backgroundSize) {
        this.backgroundSize = backgroundSize;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEffectImage() {
        return effectImage;
    }

    public void setEffectImage(String effectImage) {
        this.effectImage = effectImage;
    }

    public String getRepeatGroup() {
        return repeatGroup;
    }

    public void setRepeatGroup(String repeatGroup) {
        this.repeatGroup = repeatGroup;
    }

    public String getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(String repeatId) {
        this.repeatId = repeatId;
    }

    public BackgroundImageInfo getBackgroundImageInfo() {
        return backgroundImageInfo;
    }

    public void setBackgroundImageInfo(BackgroundImageInfo backgroundImageInfo) {
        this.backgroundImageInfo = backgroundImageInfo;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
