package be.veterinarysolutions.vsol.interfaces;

import java.nio.file.Path;

public interface Pollable {
    void fileModified(Path path);
}
