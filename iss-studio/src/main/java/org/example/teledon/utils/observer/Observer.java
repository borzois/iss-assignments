package org.example.teledon.utils.observer;

import org.example.teledon.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}