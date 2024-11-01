package no.arnemunthekaas.engine.entities.components;

import imgui.ImGui;
import imgui.type.ImInt;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.editor.imgui.ImGuiUtils;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    private static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject;

    /**
     * Update component with delta time
     *
     * @param dt Delta time
     */
    public void update(float dt) {

    }

    /**
     *
     * @param dt
     */
    public void editorUpdate(float dt) {
    }

    /**
     * Start component
     */
    public void start() {

    }

    /**
     *
     */
    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field f : fields) {
                boolean isTransient = Modifier.isTransient(f.getModifiers());
                if (isTransient)
                    continue;

                boolean isPrivate = Modifier.isPrivate(f.getModifiers());
                if (isPrivate)
                    f.setAccessible(true);


                Class type = f.getType();
                Object value = f.get(this);
                String name = f.getName();

                if (type == int.class) {
                    int val = (int) value;
                    f.set(this, ImGuiUtils.dragInt(name, val));

                } else if (type == float.class) {
                    float val = (float) value;
                    f.set(this, ImGuiUtils.dragFloat(name, val));

                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    f.set(this, ImGuiUtils.checkBox(name, val));
//                    if (ImGui.checkbox(name + ": ", val)) {
//                        f.set(this, !val);
//                    }

                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f) value;
                    ImGuiUtils.drawVec2Control(name, val);

                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    ImGuiUtils.drawVec3Control(name, val);

                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    ImGuiUtils.colorPicker4(name, val);
                } else if (type.isEnum()) {
                    String[] enumValues = getEnumValues(type);
                    String enumType = ((Enum)value).name();
                    ImInt index = new ImInt(indexOf(enumType, enumValues));

                    if (ImGui.combo(f.getName(), index, enumValues, enumValues.length))
                        f.set(this, type.getEnumConstants()[index.get()]);

                } else if (type == String.class) {
                    f.set(this, ImGuiUtils.inputText(f.getName() + ": ",
                            (String) value));
                }

                if (isPrivate)
                    f.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private int indexOf(String str, String[] arr) {
        for (int i=0; i < arr.length; i++) {
            if (str.equals(arr[i])) {
                return i;
            }
        }

        return -1;
    }

    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType) {
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T enumIntegerValue : enumType.getEnumConstants()) {
            enumValues[i] = enumIntegerValue.name();
            i++;
        }
        return enumValues;
    }

    /**
     * Give a game object a uid
     */
    public void generateID() {
        if(this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

    /**
     * Get unique ID
     * @return
     */
    public int getUid() {
        return uid;
    }

    /**
     *
     * @param maxID
     */
    public static void init(int maxID) {
        ID_COUNTER = maxID;
    }

    /**
     *
     */
    public void destroy() {

    }

    /**
     *
     * @param collidingObject
     * @param contact
     * @param contactNormal
     */
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {

    }

    /**
     *
     * @param collidingObject
     * @param contact
     * @param contactNormal
     */
    public void endContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {

    }

    /**
     *
     * @param collidingObject
     * @param contact
     * @param contactNormal
     */
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f contactNormal) {

    }

    /**
     *
     * @param collidingObject
     * @param contact
     * @param contactNormal
     */
    public void postSolve(GameObject collidingObject, Contact contact, Vector2f contactNormal) {

    }
}
