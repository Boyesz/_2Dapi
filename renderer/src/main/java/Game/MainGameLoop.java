package Game;

import org.lwjgl.glfw.GLFW;
import renderEngine.DisplayManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class MainGameLoop {

    public static void main(String[] args){
        DisplayManager.createDisplay();

        while (!glfwWindowShouldClose(DisplayManager.window)){

            DisplayManager.updateDisplay();

        }

        DisplayManager.closeDisplay();

    }

}
