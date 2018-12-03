package domain.network;

import domain.Packet;
import domain.enums.Action;
import domain.enums.PlayerNumber;
import domain.interfaces.IJsonHandler;
import domain.interfaces.IPacketHandler;
import domain.game.Game;
import domain.game.GameContainer;

import java.io.IOException;

import domain.game.Player;
import domain.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class PacketManager implements IPacketHandler {
    private Map<Action, IJsonHandler> registeredPackets;
    private SocketConnection socketConnection;

    private GameContainer gameContainer;
    private Game game;

    public PacketManager(SocketConnection socketConnection, GameContainer gameContainer)
    {
        this.socketConnection = socketConnection;
        this.gameContainer = gameContainer;
        registeredPackets = new HashMap<>();

        registerPackets();
    }

    @Override
    public void handle(Packet packet) throws IOException
    {
        if (registeredPackets.containsKey(packet.getAction()))
        {
            registeredPackets.get(packet.getAction()).handle(packet.getParameters());
        }
    }

    private void registerPackets()
    {
        packetMoveChecker();
        packetNewGame();
    }

    private void packetMoveChecker()
    {
        IJsonHandler moveCheckerPacket = new IJsonHandler() {
            @Override
            public void handle(String[] parameters) throws IOException {
//                if (game.moveChecker((PlayerNumber)JsonUtil.getObjectFromArray(parameters[0], PlayerNumber.class),
//                        (Integer) JsonUtil.getObjectFromArray(parameters[1], Integer.class),
//                        (Integer) JsonUtil.getObjectFromArray(parameters[2], Integer.class)))
//                {
//                    Packet packet = new Packet(Action.MOVECHECKER, new String[] { game.toJson() });
//                    socketConnection.sendPacket(packet);
//                    socketConnection.sendPacketToOpponent(packet);
//                }
//                else {
//                    socketConnection.sendPacket(new Packet(Action.ERRORMESSAGE, new String[] { "Invalid Move." }));
//                }

                game.moveChecker((PlayerNumber)JsonUtil.getObjectFromArray(parameters[0], PlayerNumber.class),
                        (Integer) JsonUtil.getObjectFromArray(parameters[1], Integer.class),
                        (Integer) JsonUtil.getObjectFromArray(parameters[2], Integer.class));
                Packet packet = new Packet(Action.MOVECHECKER, new String[] { game.toJson() });
                socketConnection.sendPacket(packet);
                socketConnection.sendPacketToOpponent(packet);
            }
        };

        registeredPackets.put(Action.MOVECHECKER, moveCheckerPacket);
    }

    private void packetNewGame()
    {
        IJsonHandler newGameObject = new IJsonHandler() {
            @Override
            public void handle(String[] parameterArray) throws IOException {
                game = gameContainer.newGame((Player)JsonUtil.getObjectFromArray(parameterArray[0], Player.class), socketConnection);

                if (game.isGameStarted())
                {
                    socketConnection.sendPacket(new Packet(
                            Action.STARTGAME, new String[] { game.toJson(), PlayerNumber.ONE.toJson() }));
                    socketConnection.sendPacketToOpponent(new Packet(
                            Action.STARTGAME, new String[] { game.toJson(), PlayerNumber.TWO.toJson() }));
                }
            }
        };

        registeredPackets.put(Action.NEWGAME, newGameObject);
    }
}
