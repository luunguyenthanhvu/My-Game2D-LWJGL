package no.arnemunthekaas.engine.events.observers;

import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.events.observers.events.Event;

public interface Observer {


    void onNotify(GameObject object, Event event);
}
