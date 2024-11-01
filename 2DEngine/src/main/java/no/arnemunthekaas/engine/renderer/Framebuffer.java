package no.arnemunthekaas.engine.renderer;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {

    private int fboID = 0;
    private Texture texture;
    public int width, height;

    /**
     *
     * @param width
     * @param height
     */
    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        // Generate framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // Create texture to render data and attach to framebuffer
        this.texture = new Texture(width, height);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getID(), 0);

        // Create renderbuffer
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            assert false : "Error: Framebuffer is not complete.";

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Bind framebuffer
     */
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    /**
     * Unbind framebuffer
     */
    public void unBind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Get framebuffer object ID
     * @return fboID
     */
    public int getFboID() {
        return fboID;
    }

    /**
     * Get texture ID
     * @return
     */
    public int getTextureID() {
        return texture.getID();
    }

}
