package com.example.splitza.controller;

import javafx.event.ActionEvent;

import java.io.IOException;

public abstract class ControllerAbstrato {
    public abstract void initialize();
    protected abstract void redirectWindow(ActionEvent event, String path) throws IOException;
}