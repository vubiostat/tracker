package edu.vanderbilt.mc.biostat.tracker;

import java.awt.Graphics;
import javax.swing.JViewport;
import org.jdesktop.swingx.JXTable;

public class ActivityTable extends JXTable {

  @Override
  public void paintComponent(Graphics g) {
    fitColumns();
    super.paintComponent(g);
  }

  @Override
  public boolean getScrollableTracksViewportWidth() {
    return true;
//    return getParent() instanceof JViewport
//            && getPreferredSize().width > getParent().getWidth();
  }

  private void fitColumns() {
    int count = getColumnCount();
    int width = 0;
    for (int i = 0; i < count; i++) {
      packColumn(i, 5);
      if (i != count - 2) {
        width += getColumnExt(i).getPreferredWidth();
      }
    }

    int tableWidth = Math.max(getPreferredSize().width, getParent().getWidth());
    int columnWidth = tableWidth - width;
    getColumnExt(count - 2).setPreferredWidth(columnWidth < 0 ? 0 : columnWidth);
  }
}
