package net.minestom.engine.ui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import imgui.ImGui;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import net.minestom.engine.Editor;
import net.minestom.engine.ecs.TagComponent;

public class HierarchyPanel implements UI.Element {
    private static final Family familyAll = Family.all(TagComponent.class).get();

    private final Editor editor;

    private Entity selectedEntity;

    public HierarchyPanel(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void render() {
        //var instance = editor.getInstance();

        ImGui.begin("Hierarchy");

        for (var e : editor.getAshley().getEntitiesFor(familyAll)) {
            drawEntity(e);
        }

        //ImGui.showDemoWindow();

        if (ImGui.beginPopupContextWindow(ImGuiPopupFlags.MouseButtonRight | ImGuiPopupFlags.NoOpenOverItems)) {
            if (ImGui.menuItem("Create Empty")) {
                selectedEntity = editor.getEcs().createEntity("Empty Entity");
            }
            /*if (ImGui.menuItem("Camera")) {
                selectionContext = context.createEntity("Camera").also {
                    it.addComponent(CameraComponent(SceneCamera()))
                }.entityHandle
            }
            if (ImGui.menuItem("2D Sprite")) {
                selectionContext = context.createEntity("Sprite").also {
                    it.addComponent(SpriteRendererComponent(floatArrayOf(1f, 1f, 1f, 1f)))
                }.entityHandle
            }*/
            ImGui.endPopup();
        }

        ImGui.end();
    }

    private void drawEntity(Entity entity) {
        var tag = TagComponent.MAPPER.get(entity);

        int flags = ImGuiTreeNodeFlags.Bullet;
        if (entity.equals(selectedEntity)) flags |= ImGuiTreeNodeFlags.Selected;
        boolean open = ImGui.treeNodeEx(tag.tag, flags);

        if (ImGui.isItemClicked()) {
            selectedEntity = entity;
        }

        if (open) {
            ImGui.treePop();
        }
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }
}
