package com.labs.game.service;

import com.labs.game.event.Event;

public interface Observer {
    void notify(Event event);
}
