package jtraynotes.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CheckRappelListener
  implements ActionListener
{
  private JNoteFrame frame;

  public CheckRappelListener(JNoteFrame noteFrame)
  {
    this.frame = noteFrame;
  }

  public void actionPerformed(ActionEvent arg0)
  {
    if (!this.frame.bRappel)
      this.frame.setRappel(true);
    else
      this.frame.setRappel(false);
  }
}