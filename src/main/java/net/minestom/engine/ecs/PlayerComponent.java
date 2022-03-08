package net.minestom.engine.ecs;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class PlayerComponent implements Component {
    public static final ComponentMapper<PlayerComponent> MAPPER = ComponentMapper.getFor(PlayerComponent.class);

    public final String username;

    public PlayerComponent(String username) {
        this.username = username;
    }
}
