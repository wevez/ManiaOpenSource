package com.mania.management.event;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Hexeption on 18/12/2016.
 */
public abstract class Event {

    private boolean cancelled;

    public enum State {
        PRE("PRE", 0),

        POST("POST", 1);

        private State(final String string, final int number) {

        }
    }

    public void call() {
        this.cancelled = false;
        call(this);
    }

    public boolean isCancelled() {

        return cancelled;
    }

    public void cancell() {

        this.cancelled = true;
    }

    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;
    }

    private static final void call(final Event event) {

        final ArrayHelper<Data> dataList = EventManager.get(event.getClass());

        if (dataList != null) {
            for (final Data data : dataList) {

                try {
                    data.target.invoke(data.source, event);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
