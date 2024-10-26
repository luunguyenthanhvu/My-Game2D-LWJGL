package vuluu.nlu.engine;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import vuluu.nlu.constant.GameConstants;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Window implements Observer {

  double width, height;
  String title;

  static Window window = null;

  public Window() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = screenSize.getWidth();
    this.height = screenSize.getHeight();
    this.title = GameConstants.GAME_NAME;

    // EventSystem.addObserver(this);
  }


  public void run() {
    
  }

  @Override
  public void update(Observable o, Object arg) {

  }
}
