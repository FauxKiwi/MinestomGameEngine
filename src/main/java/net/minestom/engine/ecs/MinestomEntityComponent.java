package net.minestom.engine.ecs;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import net.minestom.engine.ui.ImGuiUtils;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;

public class MinestomEntityComponent implements Component {
    public static final ComponentMapper<MinestomEntityComponent> MAPPER = ComponentMapper.getFor(MinestomEntityComponent.class);

    private static final String[] entityTypes = ImGuiUtils.enumLikeToStringArray("net.minestom.server.entity.EntityTypes");

    public final Entity entity;

    public int appearance = 1;
    public boolean noGravity;

    public MinestomEntityComponent(Entity entity) {
        this.entity = entity;
    }

    public static void draw(com.badlogic.ashley.core.Entity ae, MinestomEntityComponent handle) {
        var entity = handle.entity;

        var appearance = new ImInt(handle.appearance); // TODO: maybe separate renderer component
        ImGui.combo("Appearance", appearance, entityTypes);
        if (handle.appearance != (handle.appearance = appearance.get())) {
            entity.switchEntityType((EntityType) ImGuiUtils.getEnumLikeIndexed("net.minestom.server.entity.EntityTypes", handle.appearance));
        }

        var useGravity = new ImBoolean(!handle.noGravity); // TODO: create gravity component (including this checkbox)
        ImGui.checkbox("Use Gravity", useGravity);
        /*if (handle.noGravity != (handle.noGravity = !useGravity.get())) {
            entity.setNoGravity(eNoGravity);
        }*/handle.noGravity = !useGravity.get();

        // TODO: edit adventure components
        //var eCustomName = entity.getCustomName();
        //var customName = new ImString(eCustomName)

        var eAV = entity.isAutoViewable();
        var aV = new ImBoolean(eAV);
        ImGui.checkbox("Auto Viewable", aV);
        if (eAV != (eAV = aV.get())) entity.setAutoViewable(eAV);
        var eGlowing = entity.isGlowing();
        var glowing = new ImBoolean(eGlowing);
        ImGui.checkbox("Glowing", glowing);
        if (eGlowing != (eGlowing = glowing.get())) entity.setGlowing(eGlowing);
        var eCNV = entity.isCustomNameVisible();
        var cNV = new ImBoolean(eCNV);
        ImGui.checkbox("Custom Name Visible", cNV); // TODO: to custom name later and sort this
        if (eCNV != (eCNV = cNV.get())) entity.setCustomNameVisible(eCNV);
        var eInvisible = entity.isInvisible();
        var invisible = new ImBoolean(eInvisible);
        ImGui.checkbox("Invisible", invisible);
        if (eInvisible != (eInvisible = invisible.get())) entity.setInvisible(eInvisible);
        var eOnFire = entity.isOnFire();
        var onFire = new ImBoolean(eOnFire);
        ImGui.checkbox("Burning", onFire);
        if (eOnFire != (eOnFire = onFire.get())) entity.setOnFire(eOnFire);
        var eSilent = entity.isSilent();
        var silent = new ImBoolean(eSilent);
        ImGui.checkbox("Silent", silent);
        if (eSilent != (eSilent = silent.get())) entity.setSilent(eSilent);
    }
}
