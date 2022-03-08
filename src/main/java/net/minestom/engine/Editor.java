package net.minestom.engine;

import com.badlogic.ashley.core.Engine;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import net.minestom.engine.ecs.ECS;
import net.minestom.engine.ui.UI;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;

public class Editor extends Application {
    private Mode mode = Mode.EDIT;

    private Instance instance;
    private String testPlayerName;

    private ECS ecs;
    private UI ui;

    private boolean dockspaceOpen = true;
    private boolean optFullscreenPersistent = true;
    private int dockspaceFlags = ImGuiDockNodeFlags.None;

    private void init() {
        ecs = new ECS(this);
        ui = new UI(this);
    }

    @Override
    protected void configure(Configuration config) {
        ImGui.createContext();
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard | ImGuiConfigFlags.DockingEnable | ImGuiConfigFlags.ViewportsEnable);
    }

    @Override
    public void process() {
        var viewport = ImGui.getMainViewport();
        boolean optFullscreen = optFullscreenPersistent;
        int dockspaceWfs = ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.MenuBar;

        if (optFullscreen) {
            ImGui.setNextWindowPos(viewport.getPosX(), viewport.getPosY());
            ImGui.setNextWindowSize(viewport.getSizeX(), viewport.getSizeY());
            ImGui.setNextWindowViewport(viewport.getID());
            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0f);
            ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0f);
            dockspaceWfs |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove
                    | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;
        }

        if ((dockspaceFlags & ImGuiDockNodeFlags.PassthruCentralNode) != 0) dockspaceWfs |= ImGuiWindowFlags.NoBackground;

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0f, 0f);
        ImGui.begin("Dockspace", new ImBoolean(dockspaceOpen), dockspaceWfs);
        ImGui.popStyleVar();

        if (optFullscreen) ImGui.popStyleVar(2);

        //menuBar.showMenuBar();

        //var minWinSizeX = ImGui.getStyle().getWindowMinSizeX();
        //ImGui.getStyle().setWindowMinSize(370f, ImGui.getStyle().getWindowMinSizeY());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.DockingEnable)) {
            var dockspaceId = ImGui.getID("global-dock-space");
            ImGui.dockSpace(dockspaceId, 0, 0, dockspaceFlags);
        }
        //ImGui.getStyle().setWindowMinSize(minWinSizeX, ImGui.getStyle().getWindowMinSizeY());

        // Render here
        ui.render();

        ImGui.end();
    }

    @Override
    protected void postRun() {
        if (mode != Mode.EDIT) ecs.onStop();
        MinecraftServer.stopCleanly();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public String getTestPlayerName() {
        return testPlayerName;
    }

    public void setTestPlayerName(String testPlayerName) {
        this.testPlayerName = testPlayerName;
    }

    public ECS getEcs() {
        return ecs;
    }

    public Engine getAshley() {
        return ecs.getAshley();
    }

    public enum Mode {
        EDIT, DEBUG, TEST_LOCAL, TEST_OPEN
    }

    public static void main(String[] args) {
        var editor = new Editor();

        InternalServer.start(editor);

        editor.init();

        new Thread(() -> Application.launch(editor)).start();
    }
}
