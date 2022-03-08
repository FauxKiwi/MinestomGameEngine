package net.minestom.engine.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import net.minestom.engine.Editor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.UUID;

public class ECS {
    private final Editor editor;

    private final Family familyMinestomEntities = Family.all(MinestomEntityComponent.class).get();
    private final Family familyPlayers = Family.all(PlayerComponent.class).get();

    private final Engine ashley = new Engine();
    //private Engine backup = null;

    public ECS(Editor editor) {
        this.editor = editor;

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            if (!event.isFirstSpawn()) return;
            if (editor.getMode() == Editor.Mode.EDIT) return;
            registerPlayer(event.getPlayer());
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            if (editor.getMode() == Editor.Mode.EDIT) return;
            unregisterPlayer(event.getPlayer().getUsername());
        });
    }

    public void onStart() {
        for (var p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (p.getUsername().equals(editor.getTestPlayerName())) editor.getEcs().registerPlayer(p);
        }
        //backup = ashley;
        //ashley = cloneAshley(ashley);
        for (var e : ashley.getEntitiesFor(familyMinestomEntities)) {
            var mec = MinestomEntityComponent.MAPPER.get(e);
            mec.entity.setNoGravity(mec.noGravity);
        }
    }

    public void onStop() {
        editor.setMode(Editor.Mode.EDIT);
        //ashley = backup; // TODO: real recreation
        //backup = null;
        for (var p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (!p.getUsername().equals(editor.getTestPlayerName())) p.kick("Test finished");
            else editor.getEcs().unregisterPlayer(p.getUsername());
        }
        for (var e : ashley.getEntitiesFor(familyMinestomEntities)) {
            MinestomEntityComponent.MAPPER.get(e).entity.setNoGravity(true);
        }
    }

    public void registerPlayer(Player player) {
        ashley.addEntity(new Entity()
                .add(new TagComponent(player.getUuid(), player.getUsername()))
                .add(new MinestomEntityComponent(player))
                .add(new PlayerComponent(player.getUsername()))
                .add(new TransformComponent(player.getPosition()))
        );
    }

    public void unregisterPlayer(String username) {
        Entity ashleyEntity = null;
        for (var p : ashley.getEntitiesFor(familyPlayers)) {
            if (username.equals(PlayerComponent.MAPPER.get(p).username)) {
                ashleyEntity = p;
                break;
            }
        }
        if (ashleyEntity == null) return;
        ashley.removeEntity(ashleyEntity);
    }

    public Entity createEntity(String tag) {
        var uuid = UUID.randomUUID();
        var ashleyEntity = new Entity();
        var minestomEntity = new net.minestom.server.entity.Entity(EntityType.ARMOR_STAND, uuid);

        minestomEntity.setNoGravity(true);

        return minestomEntity.setInstance(editor.getInstance(), new Pos(0, 42, 0)).thenApply(__ -> {
            ashleyEntity
                    .add(new TagComponent(uuid, tag))
                    .add(new MinestomEntityComponent(minestomEntity))
                    .add(new TransformComponent(minestomEntity.getPosition()));
            ashley.addEntity(ashleyEntity);
            return ashleyEntity;
        }).join(); // TODO
    }

    private static Engine cloneAshley(Engine ashley) {
        var engine = new Engine();
        for (var e : ashley.getEntities()) {
            engine.addEntity(e);
        }
        for (var s : ashley.getSystems()) {
            engine.addSystem(s);
        }
        return engine;
    }

    public Engine getAshley() {
        return ashley;
    }
}
