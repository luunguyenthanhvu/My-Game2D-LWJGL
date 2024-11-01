package no.arnemunthekaas.engine.entities.components.animation;

import imgui.ImGui;
import imgui.type.ImString;
import no.arnemunthekaas.editor.imgui.ImGuiUtils;
import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.entities.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StateMachine extends Component {

    /**
     *
     * @param from
     * @param to
     * @param onTrigger
     */
    public void addTrigger(String from, String to, String onTrigger) {
        this.stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    private class StateTrigger {
        public String state;
        public String trigger;

        /**
         *
         */
        public StateTrigger() {

        }

        /**
         *
         * @param state
         * @param trigger
         */
        public StateTrigger(String state, String trigger) {
            this.state = state;
            this.trigger = trigger;
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != StateTrigger.class) return false;
            StateTrigger t2 = (StateTrigger) o;
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(trigger, state);
        }
    }

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    /**
     *
     */
    public void refreshTextures() {
        for (AnimationState state : states)
            state.refreshTextures();

    }

    /**
     * Define a state transfer given a trigger
     * @param from
     * @param to
     * @param trigger
     */
    public void addStateTrigger(String from, String to, String trigger) {
        this.stateTransfers.put(new StateTrigger(from, trigger), to);
    }

    /**
     * Add animation state to state machine
     * @param state
     */
    public void addState(AnimationState state) {
        this.states.add(state);
    }

    /**
     * Sets default state
     * @param animationTitle
     */
    public void setDefaultState(String animationTitle) {
        for (AnimationState state : states) {
            if (state.title.equals(animationTitle)) {
                defaultStateTitle = animationTitle;
                if (currentState == null) {
                    currentState = state;
                    return;
                }
            }
        }
        System.out.println("Unable to find state: '" + animationTitle +"'");
    }

    /**
     *
     * @param trigger
     */
    public void trigger(String trigger) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.title) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    int newStateIndex = stateIndexOf(stateTransfers.get(state));

                    if (newStateIndex > -1) {
                        currentState = states.get(newStateIndex);
                    }
                }
                return;
            }
        }
    }

    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (AnimationState state : states) {
            if(state.title.equals(stateTitle))
                return index;

            index++;
        }
        return index;
    }

    @Override
    public void start() {
        for (AnimationState state : states) {
            if (state.title.equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (currentState != null) {
            currentState.update(dt);
            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
            if (sprite != null)
                sprite.setSprite(currentState.getCurrentSprite());
        }
    }

    @Override
    public void editorUpdate(float dt) {
//        if (currentState != null) {
//            currentState.update(dt);
//            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
//            if (sprite != null)
//                sprite.setSprite(currentState.getCurrentSprite());
//        }
    }

    @Override
    public void imgui() {
        int index = 0;

        for (AnimationState state : states) {
            ImString title = new ImString(state.title);
            ImGui.inputText("State: ", title);
            state.title = title.get();

            state.doesLoop = ImGuiUtils.checkBox("Does Loop", state.doesLoop);

            for (Frame frame : state.animationFrames) {
                float[] tmp = new float[1];
                tmp[0] = frame.frameTime;
                ImGui.dragFloat("Frame(" + index + ") Time: ", tmp, 0.01f);
                frame.frameTime = tmp[0];
                index++;
            }
        }
    }

}
