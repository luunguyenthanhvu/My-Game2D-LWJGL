package vuluu.nlu.engine;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.lwjgl.BufferUtils;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelEditorScene extends Scene {

  boolean changingScene = false;
  float timeTimeChangScene = 2;

  String vertexShaderSrc =
      "#version 330 core\n"
          + "layout (location = 0) in vec3 aPos;\n"
          + "layout (location = 1) in vec3 aColor;\n"
          + "\n"
          + "out vec3 fColor;\n"
          + "\n"
          + "uniform mat4 uView;\n"
          + "uniform mat4 uProjection;\n"
          + "\n"
          + "void main()\n"
          + "{\n"
          + "    fColor = aColor;\n"
          + "\n"
          + "    gl_Position = uProjection * uView * vec4(aPos, 1.0);\n"
          + "}";
  String fragmentShaderSrc =
      "#version 330 core\n"
          + "in vec3 fColor;\n"
          + "\n"
          + "out vec4 color;\n"
          + "\n"
          + "void main()\n"
          + "{\n"
          + "    color = vec4(fColor, 1.0);\n"
          + "}";

  int vertexID, fragmentID, shaderProgram;

  float[] vertexArray = {
      // position        // color
      0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
      -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
      0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
      -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
  };

  // Important must be in couter clockwise order
  int[] elementArray = {
      2, 1, 0, // top right
      0, 1, 3, //  bottom left

  };

  int vaoID, vboID, eboID;

  public LevelEditorScene() {

  }

  /**
   * Compile and link shaders
   */
  @Override
  public void init() {

    // First load and compile the vertex shader
    vertexID = glCreateShader(GL_VERTEX_SHADER);

    // pass the shader source to the GPU
    glShaderSource(vertexID, vertexShaderSrc);
    glCompileShader(vertexID);

    // check for errors in compilation
    int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
    if (success == GL_FALSE) {
      int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
      System.out.println("Error default shader");
      System.out.println(glGetShaderInfoLog(vertexID, len));
      assert false : "";
    }

    // Then load and compile the fragment shader
    fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

    // pass the shader source to the GPU
    glShaderSource(fragmentID, fragmentShaderSrc);
    glCompileShader(fragmentID);

    // check for errors in compilation
    success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
    if (success == GL_FALSE) {
      int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
      System.out.println("Error default shader");
      System.out.println(glGetShaderInfoLog(fragmentID, len));
      assert false : "";
    }

    // Link shaders and check for errors
    shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, vertexID);
    glAttachShader(shaderProgram, fragmentID);
    glLinkProgram(shaderProgram);

    success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
    if (success == GL_FALSE) {
      int len = glGetShaderi(shaderProgram, GL_INFO_LOG_LENGTH);
      System.out.println("Error linking shader");
      System.out.println(glGetProgramInfoLog(shaderProgram, len));
      assert false : "";
    }

    // Generate VAO VBO EBO buffer objects and send to GPU
    vaoID = glGenVertexArrays();
    glBindVertexArray(vaoID);

    // create a float buffer of vertices
    FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
    vertexBuffer.put(vertexArray).flip();

    // Create VBO upload the vertex buffer
    vboID = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

    // Create the indices and upload
    IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
    elementBuffer.put(elementBuffer).flip();

    eboID = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER_BINDING, eboID);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

    // add the vertex attribute pointers
    int positionSize = 3;
    int colorSize = 4;
    int floatSizeBytes = 4;
    int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
    glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
    glEnableVertexAttribArray(0);

    glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
        positionSize * floatSizeBytes);
    glEnableVertexAttribArray(1);
  }


  @Override
  public void update(float dt) {

    // Bind shader program
    glUseProgram(shaderProgram);

    // bind the VAO that we're using
    glBindVertexArray(vaoID);

    // Enable the vertex attribute pointers
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);

    glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

    // unbind everything
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);

    glBindVertexArray(0);
    glUseProgram(0);
  }


}
