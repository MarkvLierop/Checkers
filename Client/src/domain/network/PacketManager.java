package domain.network;


import domain.Packet;
import domain.game.ClientGame;
import domain.game.ClientGameContainer;

import domain.enums.Action;

import java.io.IOException;

import domain.enums.PlayerNumber;
import domain.interfaces.IJsonHandler;
import domain.interfaces.IPacketHandler;
import domain.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class PacketManager implements IPacketHandler {
    private Map<Action, IJsonHandler> registeredPackets;
    private ClientGameContainer gameContainer;

    public PacketManager(ClientGameContainer gameContainer)
    {
        this.gameContainer = gameContainer;
        registeredPackets = new HashMap<>();
        registerPackets();
    }

    @Override
    public void handle(Packet packet) throws IOException {
        if (registeredPackets.containsKey(packet.getAction()))
        {
            registeredPackets.get(packet.getAction()).handle(packet.getParameters());
        }
    }

    private void registerPackets() {
        startGamePacket();
        moveCheckerPacket();
        errorMessagePacket();
        gameEndedPacket();
    }

    private void startGamePacket()
    {
        IJsonHandler startGamePacket = new IJsonHandler() {
            @Override
            public void handle(String[] parameters) {
                ClientGame clientGame = (ClientGame)JsonUtil.getObjectFromArray(parameters[0], ClientGame.class);
                gameContainer.setGame(clientGame);
                gameContainer.setLocalPlayerNumber((PlayerNumber) JsonUtil.getObjectFromArray(parameters[1], PlayerNumber.class));
                System.out.println(parameters[0]);
            }
        };

        registeredPackets.put(Action.START_GAME, startGamePacket);
    }

    private void moveCheckerPacket()
    {
        IJsonHandler moveCheckerPacket = new IJsonHandler() {
            @Override
            public void handle(String[] parameters) {
                ClientGame clientGame = (ClientGame)JsonUtil.getObjectFromArray(parameters[0], ClientGame.class);
                gameContainer.setGame(clientGame);
                System.out.println(parameters[0]);
            }
        };

        registeredPackets.put(Action.MOVE_CHECKER, moveCheckerPacket);
    }

    private void gameEndedPacket()
    {
        IJsonHandler gameEndedPacket = new IJsonHandler() {
            @Override
            public void handle(String[] parameters) throws IOException {
                ClientGame clientGame = (ClientGame)JsonUtil.getObjectFromArray(parameters[0], ClientGame.class);
                gameContainer.setGame(clientGame);
            }
        };

        registeredPackets.put(Action.GAME_ENDED, gameEndedPacket);
    }
    private void errorMessagePacket()
    {
        IJsonHandler errorMessagePacket = new IJsonHandler() {
            @Override
            public void handle(String[] parameters) throws IOException {
                gameContainer.getGame().setErrorMessage(parameters[0]);
            }
        };

        registeredPackets.put(Action.ERROR_MESSAGE, errorMessagePacket);
    }
}
