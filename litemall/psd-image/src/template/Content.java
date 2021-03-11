package template;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private String version = "5.7.0";
    private String type = "poster";//类型 海报
    private Global global;
    private Layout layout = new Layout();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
