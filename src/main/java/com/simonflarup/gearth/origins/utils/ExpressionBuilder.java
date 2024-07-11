package com.simonflarup.gearth.origins.utils;

import gearth.protocol.HMessage;

public class ExpressionBuilder {
    private final StringBuilder expression = new StringBuilder();

    private ExpressionBuilder(String header) {
        this.expression.append(header);
    }

    public static ExpressionBuilder start(HMessage.Direction direction, String headerName) {
        String header = String.format("{%s:%s}", direction == HMessage.Direction.TOSERVER ? "out" : "in", headerName);
        return new ExpressionBuilder(header);
    }

    private ExpressionBuilder append(String expression) {
        this.expression.append(expression);
        return this;
    }

    private ExpressionBuilder appendFormat(String format, Object... args) {
        this.expression.append(String.format(format, args));
        return this;
    }

    public ExpressionBuilder raw(String raw) {
        return append(raw);
    }

    public ExpressionBuilder space() {
        return append(" ");
    }

    public ExpressionBuilder integer(int value) {
        return appendFormat("{i:%d}", value);
    }

    public ExpressionBuilder string(String value) {
        return appendFormat("{s:\"%s\"}", value);
    }

    public ExpressionBuilder shockSeparator() {
        return append("[2]");
    }

    public String build() {
        return this.expression.toString();
    }
}
