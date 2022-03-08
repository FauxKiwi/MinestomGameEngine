package net.minestom.engine.ecs;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import java.util.UUID;

public class TagComponent implements Component {
    public static final ComponentMapper<TagComponent> MAPPER = ComponentMapper.getFor(TagComponent.class);

    public final UUID uuid;
    public String tag;

    public TagComponent(UUID uuid, String tag) {
        this.uuid = uuid;
        this.tag = tag;
    }
}
