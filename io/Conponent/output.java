package io.Conponent;

import java.io.BufferedWriter;

public interface output {
    void init(String path, boolean control);
    BufferedWriter getOutputBuffer();
    void close();

    void print();
}
