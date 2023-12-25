package com.tribium.eventer.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ExceptionLocation {
    public String hostname;
    public String processId;

    public String file;

    @JsonProperty("class")
    public String clazz;

    public String function;

    public Integer line;

    @JsonProperty("native")
    public boolean isNative = false;

    public ExceptionLocation() {
    }

    public ExceptionLocation(StackTraceElement element) {
        file = element.getFileName();
        clazz = element.getClassName();
        function = element.getMethodName();
        line = element.getLineNumber();
        isNative = element.isNativeMethod();

        // Get the host name and the process name...
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Get the process ID...
        processId = ManagementFactory.getRuntimeMXBean().getName();

    }
}
