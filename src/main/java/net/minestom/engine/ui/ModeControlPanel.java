package net.minestom.engine.ui;

import imgui.ImGui;
import net.minestom.engine.Editor;
import net.minestom.server.MinecraftServer;

public class ModeControlPanel implements UI.Element {
    private final Editor editor;

    public ModeControlPanel(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void render() {
        ImGui.begin("Mode Control TODO");

        var mode = editor.getMode();
        boolean started = false;

        if (mode == Editor.Mode.EDIT) {
            if (ImGui.button("D")) { // Debug
                started = true;
                editor.setMode(Editor.Mode.DEBUG);
            }
            ImGui.sameLine();
            if (ImGui.button("L")) { // Test local
                started = true;
                editor.setMode(Editor.Mode.TEST_LOCAL);
            }
            ImGui.sameLine();
            if (ImGui.button("O")) { // Test open
                started = true;
                editor.setMode(Editor.Mode.TEST_OPEN);
            }
        } else {
            if (ImGui.button("S")) { // Stop
                editor.getEcs().onStop();
            }
            ImGui.sameLine();
            if (ImGui.button("P")) {} // Pause
            ImGui.sameLine();
            if (ImGui.button("R")) {} // Reload
        }

        if (started) {
            editor.getEcs().onStart();
        }

        ImGui.end();
    }
}
