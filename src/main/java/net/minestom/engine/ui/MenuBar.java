package net.minestom.engine.ui;

import imgui.ImGui;
import net.minestom.engine.Editor;
import org.lwjgl.glfw.GLFW;

public class MenuBar implements UI.Element {
    private final Editor editor;

    public MenuBar(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void render() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Exit")) {
                GLFW.glfwSetWindowShouldClose(editor.getHandle(), true);
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}
