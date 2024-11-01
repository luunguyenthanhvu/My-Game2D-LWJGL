package no.arnemunthekaas.editor;

import imgui.ImGui;
import no.arnemunthekaas.engine.events.observers.events.Event;
import no.arnemunthekaas.engine.events.observers.events.EventSystem;
import no.arnemunthekaas.engine.events.observers.events.EventType;

public class MenuBar {

    /**
     *
     */
    public void imgui() {
        ImGui.beginMenuBar();

        if(ImGui.beginMenu("File")) {
            if(ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if(ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }


            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}
