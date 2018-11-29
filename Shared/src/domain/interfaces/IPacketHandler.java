package domain.interfaces;

import domain.Packet;

import java.io.IOException;

public interface IPacketHandler {
    void handle(Packet packet) throws IOException;
}
