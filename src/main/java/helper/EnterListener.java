package helper;

import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction;

public class EnterListener extends ShortcutListener {
  public EnterListener() {
    super("Enter key", ShortcutAction.KeyCode.ENTER, null);
  }

  public void handleAction(Object src, Object tgt) {
  }
}
