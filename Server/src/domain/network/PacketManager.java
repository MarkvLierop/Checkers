package domain.network;

import com.google.gson.Gson;
import domain.Packet;
import domain.enums.Action;
import domain.enums.PlayerNumber;
import domain.interfaces.IJsonHandler;
import domain.interfaces.IPacketHandler;
import domain.game.Game;
import domain.game.GameContainer;

import java.io.IOException;

import domain.game.Player;
import domain.network.models.Result;
import domain.network.models.Score;
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
                game.moveChecker((Player) JsonUtil.getObjectFromArray(parameters[0], Player.class),
                        (Integer) JsonUtil.getObjectFromArray(parameters[1].substring(0, 1), Integer.class),
                        (Integer) JsonUtil.getObjectFromArray(parameters[1].substring(1, 2), Integer.class),
                        (Integer) JsonUtil.getObjectFromArray(parameters[2].substring(0, 1), Integer.class),
                        (Integer) JsonUtil.getObjectFromArray(parameters[2].substring(1, 2), Integer.class));
                Packet packet = new Packet(Action.MOVE_CHECKER, new String[] { new Gson().toJson(game) });
                socketConnection.sendPacket(packet);
                socketConnection.sendPacketToOpponent(packet);

                if (game.gameHasEnded())
                {
                    Packet gameEndedPacket = new Packet(Action.GAME_ENDED, new String[] { new Gson().toJson(game) });
                    socketConnection.sendPacket(gameEndedPacket);
                    socketConnection.sendPacketToOpponent(gameEndedPacket);
                    socketConnection.sendScore(
                            new Score("marky", Result.WIN, 5, 19, "Mark"));
                }
            }
        };

        registeredPackets.put(Action.MOVE_CHECKER, moveCheckerPacket);
    }

    private void packetNewGame()
    {
        IJsonHandler newGameObject = new IJsonHandler() {
            @Override
            public void handle(String[] parameterArray) throws IOException {
                game = gameContainer.findGame((Player)JsonUtil.getObjectFromArray(parameterArray[0], Player.class), socketConnection);

                if (game.isGameStarted())
                {
                    socketConnection.sendPacket(new Packet(
                            Action.START_GAME, new String[] { new Gson().toJson(game), PlayerNumber.ONE.toJson() }));
                    socketConnection.sendPacketToOpponent(new Packet(
                            Action.START_GAME, new String[] { new Gson().toJson(game), PlayerNumber.TWO.toJson() }));
                }
            }
        };

        registeredPackets.put(Action.NEW_GAME, newGameObject);
    }

    private void packetCloseConnection()
    {
        IJsonHandler closeConnection = new IJsonHandler() {
            @Override
            public void handle(String[] parameters) throws IOException {
//                if (gameContainer.removePlayer((Player)JsonUtil.getObjectFromArray(parameters[0], Player.class)))
//                {
//                    socketConnection.sendPacketToOpponent(new Packet(Action.CLOSE_CONNECTION, new String[]{}));
//                }
                socketConnection.closeConnection();
            }
        };

        registeredPackets.put(Action.CLOSE_CONNECTION, closeConnection);
    }
}
