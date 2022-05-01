package com.beibarys.comport;

import java.io.File;

public interface IBillValidator {
    void acceptNote();

    boolean connect();

    void disableBV();

    void disconnect();

    void enableBV();

    String getApplication();

    String getProtokol();

    void returnNote();

    void downloadBin(File f);
}