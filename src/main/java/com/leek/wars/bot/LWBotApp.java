package com.leek.wars.bot;

import fr.lewon.bot.errors.BotRunnerException;
import fr.lewon.bot.runner.BotRunner;
import fr.lewon.client.AbstractParameterizedApp;
import fr.lewon.client.exceptions.ActionException;
import fr.lewon.client.exceptions.ParameterizedAppException;
import fr.lewon.client.util.parameters.Parameter;
import fr.lewon.client.util.parameters.impl.SimpleParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LWBotApp extends AbstractParameterizedApp {

    private static final Parameter LOGIN_PARAMETER = new SimpleParameter("login", true);
    private static final Parameter PASSWORD_PARAMETER = new SimpleParameter("password", true);

    public static void main(String[] args) throws ParameterizedAppException {
        new LWBotApp().launch(args);
    }

    @Override
    protected List<Parameter> getParamsToInit() {
        List<Parameter> params = new ArrayList<>();
        params.add(LOGIN_PARAMETER);
        params.add(PASSWORD_PARAMETER);
        return params;
    }

    @Override
    protected void run(String[] args) throws ParameterizedAppException {
        try {
            BotRunner runner = new LWBotRunnerBuilder().buildRunner(LOGIN_PARAMETER.getValue(), PASSWORD_PARAMETER.getValue(), new HashMap<>());
            runner.start();
        } catch (BotRunnerException e) {
            throw new ActionException(e);
        }
    }


}
