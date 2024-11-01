package no.arnemunthekaas.editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;

import java.util.List;

public class SceneHierarchyWindow {

    private static String payloadDragDropType = "SceneHierarchy";

    public void imgui() {
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for(GameObject obj : gameObjects) {

            if(!obj.doSerialization())
                continue;

            boolean treeNodeOpen = doTreeNode(obj, index);

            if(treeNodeOpen)
                ImGui.treePop();

            index++;
        }
        ImGui.end();
    }

    private boolean doTreeNode(GameObject obj, int index) {
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(obj.name,
                ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth,
                obj.name);

        ImGui.popID();

        if (ImGui.beginDragDropSource()) {
            //ImGui.setDragDropPayloadObject(payloadDragDropType, obj); // doesnt exist anymore?
            ImGui.setDragDropPayload(payloadDragDropType, obj);
            ImGui.text(obj.name);
            // TODO https://youtu.be/vt_KGvT525w?t=697
            ImGui.endDragDropSource();
        }

        if (ImGui.beginDragDropTarget()) {
            Object payloadObj = ImGui.acceptDragDropPayload(payloadDragDropType);

            if(payloadObj != null) {
                if(payloadObj.getClass().isAssignableFrom(GameObject.class)) {
                    GameObject playerGameObj = (GameObject) payloadObj;
                }
            }

            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
