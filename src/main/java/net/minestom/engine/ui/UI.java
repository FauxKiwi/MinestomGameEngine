package net.minestom.engine.ui;

import net.minestom.engine.Editor;

import java.util.ArrayList;
import java.util.List;

public class UI {
    private final Editor editor;
    private final List<Element> elements = new ArrayList<>();

    public UI(Editor editor) {
        this.editor = editor;

        register(new MenuBar(editor));
        register(new ModeControlPanel(editor));
        register(new OpenTestConnectionsPanel(editor));
        var hierarchy = new HierarchyPanel(editor);
        register(hierarchy);
        register(new InspectorPanel(editor, hierarchy));
    }

    public void register(Element element) {
        elements.add(element);
    }

    public void render() {
        for (var e : elements) {
            e.render();
        }
    }

    public interface Element {
        void render();
    }
}
