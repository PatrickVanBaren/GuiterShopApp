package com.example.simplemarketapplication;

import java.util.HashSet;
import java.util.Set;

public class AuthModule {

    private static AuthModule sInstance;
    private State mState = State.CLIENT_PRODUCTS_LIST;
    private final Set<Listener> mListeners = new HashSet<>();

    public static void createInstance() {
        sInstance = new AuthModule();
    }

    public static AuthModule getInstance() {
        return sInstance;
    }

    private AuthModule() {
        changeState(State.CLIENT_PRODUCTS_LIST);
    }

    void changeState(final State newState) {
        //if (mState == newState) throw new RuntimeException("New state is equals to old state! (Default: Client)");

        mState = newState;
        for (final Listener listener : mListeners) {
            listener.onStateChanged(mState);
        }
    }

    public void addListener(final Listener listener) {
        if (mListeners.contains(listener)) throw new RuntimeException();

        mListeners.add(listener);
        listener.onStateChanged(mState);
    }

    public void removeListener(final Listener listener) {
        if (!mListeners.contains(listener)) throw new RuntimeException();

        mListeners.remove(listener);
    }

    public State getState() {
        return mState;
    }

    public enum State {
        CLIENT_PRODUCTS_LIST, CLIENT_ORDER_LIST, ADMINISTRATOR_PRODUCTS_LIST,
        ADMINISTRATOR_ORDER_LIST, ADMINISTRATOR_USERS_LIST
    }

    public interface Listener {
        void onStateChanged(State state);
    }

    public void stateClientProduct() {
        changeState(State.CLIENT_PRODUCTS_LIST);
    }

    public void stateClientBasket() {
        changeState(State.CLIENT_ORDER_LIST);
    }

    public void stateAdminProduct() {
        changeState(State.ADMINISTRATOR_PRODUCTS_LIST);
    }

    public void stateAdminBasket() {
        changeState(State.ADMINISTRATOR_ORDER_LIST);
    }

    public void stateAdminUsers() {
        changeState(State.ADMINISTRATOR_USERS_LIST);
    }
}
