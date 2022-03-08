package net.minestom.engine.ecs;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import imgui.ImGui;
import net.minestom.server.coordinate.Pos;

public class TransformComponent implements Component {
    public static final ComponentMapper<TransformComponent> MAPPER = ComponentMapper.getFor(TransformComponent.class);

    public double x, y, z;
    public float yaw, pitch;

    public TransformComponent(Pos pos) {
        apply(pos);
    }

    public void apply(Pos pos) {
        x = pos.x();
        y = pos.y();
        z = pos.z();
        yaw = pos.yaw();
        pitch = pos.pitch();
    }

    public static void draw(Entity entity, TransformComponent transform) {
        net.minestom.server.entity.Entity minestomHandle = null;
        if (MinestomEntityComponent.MAPPER.has(entity)) {
            minestomHandle = MinestomEntityComponent.MAPPER.get(entity).entity;
            transform.apply(minestomHandle.getPosition());
        }

        float[] position = {(float) transform.x, (float) transform.y, (float) transform.z};
        float[] rotation = {transform.yaw, transform.pitch};
        ImGui.dragFloat3("Position", position);
        ImGui.dragFloat2("Rotation", rotation);

        if (position[0] != transform.x || position[1] != transform.y || position[1] != transform.z
            || rotation[0] != transform.yaw || rotation[1] != transform.pitch) {

            transform.x = position[0];
            transform.y = position[1];
            transform.z = position[2];
            transform.yaw = rotation[0];
            transform.pitch = rotation[1];

            if (minestomHandle != null) {
                minestomHandle.refreshPosition(new Pos(transform.x, transform.y, transform.z, transform.yaw, transform.pitch));
            }
        }
    }
}
