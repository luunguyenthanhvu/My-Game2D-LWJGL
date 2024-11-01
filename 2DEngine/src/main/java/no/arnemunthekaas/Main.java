package no.arnemunthekaas;

import no.arnemunthekaas.engine.Window;

/**
 * Only supports Windows for now... no mac m1 sadly, hopefully if ImGui gets updated, since ImGui doesn't like arm64 runtime, and lwjgl doesn't like x64 runtime...
 * https://github.com/h593267/2DEngine based on https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
 */
public class Main {

    public static void main(String[] args) {
        Window window = Window.get();
        window.run();
    }

}
