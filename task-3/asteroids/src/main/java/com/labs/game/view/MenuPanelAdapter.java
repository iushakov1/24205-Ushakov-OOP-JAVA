package com.labs.game.view;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MenuPanelAdapter extends ComponentAdapter {
    private MenuPanel menu;
    public MenuPanelAdapter(MenuPanel menu){
        this.menu = menu;
    }


    @Override
    public void componentResized(ComponentEvent e){
        Component c = e.getComponent();
        menu.updateBounds(c.getWidth(), c.getHeight());
    }
}
