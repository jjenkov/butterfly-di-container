package com.jenkov.container;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ContainerException extends RuntimeException {

    public static final long serialVersionUID = -1;

    protected final List<InfoItem> infoItems =
            new ArrayList<>();

    public ContainerException(String errorContext, String errorCode, String errorMessage) {
        super(errorMessage);
        addInfo(errorContext, errorCode, errorMessage);
    }


    public ContainerException(String errorContext, String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        addInfo(errorContext, errorCode, errorMessage);
    }

    public ContainerException addInfo(String errorContext, String errorCode, String errorText) {
        this.infoItems.add(
                new InfoItem(errorContext, errorCode, errorText));
        return this;
    }

    public List<InfoItem> getInfoItems() {
        return infoItems;
    }

    public String getCode() {
        StringBuilder builder = new StringBuilder();

        for (int i = this.infoItems.size() - 1; i >= 0; i--) {
            InfoItem info =
                    this.infoItems.get(i);
            builder.append('[');
            builder.append(info.errorContext);
            builder.append(':');
            builder.append(info.errorCode);
            builder.append(']');
        }

        return builder.toString();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Error Code  : ").append(getCode());
        builder.append('\n');


        //append additional context information.
        for (int i = this.infoItems.size() - 1; i >= 0; i--) {
            InfoItem info =
                    this.infoItems.get(i);
            builder.append("Context Info: ");
            builder.append('[');
            builder.append(info.errorContext);
            builder.append(':');
            builder.append(info.errorCode);
            builder.append(']');
            builder.append(" : ");
            builder.append(info.errorText);
            if (i > 0) builder.append('\n');
        }

        //append root causes and text from this exception first.
        if (getMessage() != null) {
            builder.append('\n');
            if (getCause() == null) {
                builder.append(getMessage());
            } else if (!getMessage().equals(getCause().toString())) {
                builder.append(getMessage());
            }
        }
        appendException(builder, getCause());

        return builder.toString();
    }

    private void appendException(
            StringBuilder builder, Throwable throwable) {
        if (throwable == null) return;
        appendException(builder, throwable.getCause());
        builder.append(throwable.toString());
        builder.append('\n');
    }

    public static class InfoItem {
        public String errorContext = null;
        public String errorCode = null;
        public String errorText = null;

        public InfoItem(String contextCode, String errorCode,
                        String errorText) {

            this.errorContext = contextCode;
            this.errorCode = errorCode;
            this.errorText = errorText;
        }
    }
}
