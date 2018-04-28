package renderEngine.rendererTypes;

public interface IDisplayManager {

    void createDisplay();

    void updateDisplay();

    void closeDisplay();

    long getWindow();

    double[] getCursorPos();

    int getWIDTH();

    int getHEIGHT();

}
