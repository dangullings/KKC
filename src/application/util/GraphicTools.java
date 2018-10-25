package application.util;

import application.controller.RootLayoutController;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;

public class GraphicTools {

    GraphicTools(){

    }

    public static void setGraphicEffectOnRootView(){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-.4);
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(3.0);
        gaussianBlur.setInput(colorAdjust);
        RootLayoutController.getInstance().borderPane.setEffect(gaussianBlur);
    }

    public static void removeGraphicEffectOnRootView(){
        RootLayoutController.getInstance().borderPane.setEffect(null);
    }
}
