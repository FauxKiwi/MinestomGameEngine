package net.minestom.engine.ui;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import net.minestom.engine.Editor;
import net.minestom.engine.ecs.MinestomEntityComponent;
import net.minestom.engine.ecs.TagComponent;
import net.minestom.engine.ecs.TransformComponent;

import java.util.function.BiConsumer;

public class InspectorPanel implements UI.Element {
    private final Editor editor;
    private final HierarchyPanel hierarchyPanel;

    public InspectorPanel(Editor editor, HierarchyPanel hierarchyPanel) {
        this.editor = editor;
        this.hierarchyPanel = hierarchyPanel;
    }

    @Override
    public void render() {
        ImGui.begin("Inspector");

        var entity = hierarchyPanel.getSelectedEntity();
        if (entity != null) {
            //ImGui.text(entity.getComponents().toString());
            drawComponents(entity);
        }

        ImGui.end();
    }

    private void drawComponents(Entity entity) {
        var tag = TagComponent.MAPPER.get(entity);
        var tagImString = new ImString(tag.tag);
        ImGui.inputText("##Tag", tagImString);
        tag.tag = tagImString.get();

        ImGui.sameLine();
        ImGui.pushItemWidth(-1f);
        ImGui.button("Add Component");
        /*if (ImGui.button("Add Component"))
            ImGui.openPopup("AddComponent");
        if (ImGui.beginPopup("AddComponent")) {
            if (!hierarchyPanel.registry.has<TransformComponent>(hierarchyPanel.selectionContext!!) && ImGui.menuItem("Transform")) {
                hierarchyPanel.registry.insert(hierarchyPanel.selectionContext!!, TransformComponent(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 1f, 1f, 1f)))
                ImGui.closeCurrentPopup()
            }
            if (!hierarchyPanel.registry.has<CameraComponent>(hierarchyPanel.selectionContext!!) && ImGui.menuItem("Camera")) {
                hierarchyPanel.registry.insert(hierarchyPanel.selectionContext!!, CameraComponent(SceneCamera()))
                ImGui.closeCurrentPopup()
            }
            if (!hierarchyPanel.registry.has<SpriteRendererComponent>(hierarchyPanel.selectionContext!!) && ImGui.menuItem("Sprite Renderer")) {
                hierarchyPanel.registry.insert(hierarchyPanel.selectionContext!!, SpriteRendererComponent(floatArrayOf(1f, 1f, 1f, 1f)))
                ImGui.closeCurrentPopup()
            }
            ImGui.endPopup()
        }*/
        ImGui.popItemWidth();

        //TransformComponent.draw(entity);
        drawComponent(TransformComponent.class, "Transform", entity, TransformComponent.MAPPER, TransformComponent::draw);
        drawComponent(MinestomEntityComponent.class, "Entity", entity, MinestomEntityComponent.MAPPER, MinestomEntityComponent::draw);
        /*drawComponent<TransformComponent>("Transform", entity) { transform ->
                val position = transform.position
            drawVec3Control("Position", position)
            transform.position = position
            val scale = transform.scale
            drawVec3Control("Scale", scale, resetValue = 1f)
            transform.scale = scale
            val rotation = transform.rotation
            drawVec3Control("Rotation", rotation)
            transform.rotation = rotation
        }

        drawComponent<CameraComponent>("Camera", entity) { camera ->
                editBoolean("Primary Camera", camera::primary)
            editEnum<ProjectionType>("Projection Type", camera.camera::projectionTypePtr)
            if (camera.camera.projectionType == ProjectionType.Orthographic) {
                editFloat("Orthographic Size", camera.camera::orthographicSize)
                editFloat("Near Clip", camera.camera::orthographicNear)
                editFloat("Far Clip", camera.camera::orthographicFar)
            } else {
                editFloat("Field of View", camera.camera::perspectiveFov)
                editFloat("Near Clip", camera.camera::perspectiveNear)
                editFloat("Far Clip", camera.camera::perspectiveFar)
            }
            editBoolean("Fixed Aspect Ratio", camera::fixedAspectRatio)
            editColor3("Clear Color", camera.camera.clearColor)
        }

        drawComponent<SpriteRendererComponent>("Sprite Renderer", entity) { spriteRenderer ->
                val color = spriteRenderer.color
            editColor4("Color", color)
            spriteRenderer.color = color
        }

        drawComponent<NativeScriptComponent>("Native Script", entity) { script ->
                ImGui.text(script.instance::class.qualifiedName ?: "")
        }*/
    }

    private <T extends Component> void drawComponent(Class<T> clazz, String name, Entity entity, ComponentMapper<T> mapper, BiConsumer<Entity, T> draw) {
        var treeNodeFlags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.Framed | ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.AllowItemOverlap | ImGuiTreeNodeFlags.FramePadding;

        if (!mapper.has(entity)) return;

        var component = mapper.get(entity);
        var contentRegionAvailable = new ImVec2(ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY());

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4f, 4f);
        float lineHeight = ImGui.getFont().getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        ImGui.separator();
        boolean open = ImGui.treeNodeEx(name, treeNodeFlags);
        ImGui.popStyleVar();
        ImGui.sameLine(contentRegionAvailable.x - lineHeight * 0.5f);
        //ImGui.pushFont(imGuiLayer.fonts[1])
        if (ImGui.button("+"/*"\uf141"*/, lineHeight, lineHeight)/*.also { ImGui.popFont() }*/) {
            ImGui.openPopup("ComponentSettings");
        }

        boolean removeComponent = false;
        if (ImGui.beginPopup("ComponentSettings")) {
            if (ImGui.menuItem("Remove component"))
                removeComponent = true;

            ImGui.endPopup();
        }

        if (open) {
            draw.accept(entity, component);
            ImGui.treePop();
        }

        if (removeComponent) entity.remove(clazz);
            //hierarchyPanel.registry.remove(clazz, entity)
    }
}
