/*
 * This file is part of java-psd-library.
 * 
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package psd;

import psd.parser.BlendMode;
import psd.parser.layer.*;
import psd.parser.layer.additional.*;
import psd.parser.layer.additional.effects.PSDEffect;
import psd.parser.object.PsdDescriptor;
import psd.parser.object.PsdObject;
import psd.parser.object.PsdTextData;
import psd.util.BufferedImageBuilder;

import java.awt.image.*;
import java.util.*;

public class Layer implements LayersContainer {
    private int top = 0;
    private int left = 0;
    private int bottom = 0;
    private int right = 0;

    private int alpha = 255;

    private boolean visible = true;
    private boolean clippingLoaded;

    private String name;

    private BufferedImage image;
    private LayerType type = LayerType.NORMAL;

    private BlendMode layerBlendMode;
    private BlendingRanges layerBlendingRanges;
    private Mask mask;
    private Text text;

    private ArrayList<Layer> layers = new ArrayList<Layer>();

    private ArrayList<PSDEffect> layerEffects = new ArrayList<PSDEffect>();

    public Layer(LayerParser parser) {
        parser.setHandler(new LayerHandler() {
            @Override
            public void boundsLoaded(int left, int top, int right, int bottom) {
                Layer.this.left = left;
                Layer.this.top = top;
                Layer.this.right = right;
                Layer.this.bottom = bottom;
            }

            @Override
            public void blendModeLoaded(BlendMode blendMode) {
                Layer.this.setLayerBlendMode(blendMode);
            }

            @Override
            public void blendingRangesLoaded(BlendingRanges ranges) {
                Layer.this.setLayerBlendingRanges(ranges);
            }

            @Override
            public void opacityLoaded(int opacity) {
                Layer.this.alpha = opacity;
            }

            @Override
            public void clippingLoaded(boolean clipping) {
                Layer.this.setClippingLoaded(clipping);
            }

            @Override
            public void flagsLoaded(boolean transparencyProtected, boolean visible, boolean obsolete, boolean isPixelDataIrrelevantValueUseful, boolean pixelDataIrrelevant) {
                Layer.this.visible = visible;
            }

            @Override
            public void nameLoaded(String name) {
                Layer.this.name = name;
            }

            @Override
            public void channelsLoaded(List<Channel> channels) {
                BufferedImageBuilder imageBuilder = new BufferedImageBuilder(channels, getWidth(), getHeight());
                image = imageBuilder.makeImage();
            }

            @Override
            public void maskLoaded(Mask mask) {
                Layer.this.setMask(mask);
            }

        });

        parser.putAdditionalInformationParser(LayerSectionDividerParser.TAG, new LayerSectionDividerParser(new LayerSectionDividerHandler() {
            @Override
            public void sectionDividerParsed(LayerType type) {
                Layer.this.type = type;
            }
        }));

        parser.putAdditionalInformationParser(LayerEffectsParser.TAG, new LayerEffectsParser(new LayerEffectsHandler() {
            @Override
            public void handleEffects(List<PSDEffect> effects) {
                layerEffects.addAll(effects);
            }
        }));

        parser.putAdditionalInformationParser(LayerUnicodeNameParser.TAG, new LayerUnicodeNameParser(new LayerUnicodeNameHandler() {
            @Override
            public void layerUnicodeNameParsed(String unicodeName) {
                name = unicodeName;
            }
        }));
        parser.putAdditionalInformationParser(LayerTypeToolParser.TAG, new LayerTypeToolParser(new LayerTypeToolHandler() {

            @Override
            public void typeToolTransformParsed(Matrix transform) {
            }

            @Override
            public void typeToolDescriptorParsed(int version, PsdDescriptor descriptor) {
                PsdObject txt = descriptor.get("Txt");
                if (!"".equals(txt.toString())){
                    text = new Text();
                    text.setContent(txt.toString());
                    PsdTextData engineData = (PsdTextData)descriptor.get("EngineData");
                    Map<String, Object> properties = engineData.getProperties();
                    //文字颜色 大小
                    Map engineDict = (Map)properties.get("EngineDict");
                    Map styleRun = (Map)engineDict.get("StyleRun");
                    List<HashMap> runArray = (List<HashMap>)styleRun.get("RunArray");
                    HashMap runArrayMap = runArray.get(0);
                    Map styleSheet = (Map)runArrayMap.get("StyleSheet");
                    Map styleSheetData = (Map)styleSheet.get("StyleSheetData");
                    Double  fontSize = (Double)styleSheetData.get("FontSize");
                    Map fillColor = (Map)styleSheetData.get("FillColor");
                    List<Double> values = (List<Double>)fillColor.get("Values");
                    Double r,g,b;
                    r = Math.floor(values.get(1)*255);
                    g = Math.floor(values.get(2)*255);
                    b = Math.floor(values.get(3)*255);
                    text.setFontSize(fontSize);
                    String color = String.format("#%02x%02x%02x", r.intValue(), g.intValue(), b.intValue());
                    text.setColor(color+"ff");

                    Map rendered = (Map)engineDict.get("Rendered");
                    Map shapes = (Map)rendered.get("Shapes");
                    Double writingDirection = (Double) shapes.get("WritingDirection"); //横向 0 竖向 2
                    text.setWritingDirection(0==writingDirection?"horizontal-tb":"vertical-rl");

                    Map documentResources = (Map)properties.get("DocumentResources");
                    List<HashMap> fontSet = (List<HashMap>)documentResources.get("FontSet");
                    HashMap hashMap = fontSet.get(0);
                    Object name = hashMap.get("Name");
                    text.setFontFamily(name.toString());
                    // System.out.println(JSON.toJSONString(descriptor));
                }
            }
        }));
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    @Override
    public Layer getLayer(int index) {
        return layers.get(index);
    }

    @Override
    public int indexOfLayer(Layer layer) {
        return layers.indexOf(layer);
    }

    @Override
    public int getLayersCount() {
        return layers.size();
    }

    @Override
    public List<Layer> getLayers() {
        return layers;
    }

    public BufferedImage getImage() {
        return image;
    }

    public List<PSDEffect> getEffectsList() {
        return layerEffects;
    }

    public int getX() {
        return left;
    }

    public int getY() {
        return top;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    public LayerType getType() {
        return type;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getAlpha() {
        return alpha;
    }

    public boolean isClippingLoaded() {
        return clippingLoaded;
    }

    public void setClippingLoaded(boolean clippingLoaded) {
        this.clippingLoaded = clippingLoaded;
    }

    public BlendMode getLayerBlendMode() {
        return layerBlendMode;
    }

    public void setLayerBlendMode(BlendMode layerBlendMode) {
        this.layerBlendMode = layerBlendMode;
    }

    public BlendingRanges getLayerBlendingRanges() {
        return layerBlendingRanges;
    }

    public void setLayerBlendingRanges(BlendingRanges layerBlendingRanges) {
        this.layerBlendingRanges = layerBlendingRanges;
    }

    public Mask getMask() {
        return mask;
    }

    public void setMask(Mask mask) {
        this.mask = mask;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
