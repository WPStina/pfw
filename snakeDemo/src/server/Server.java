package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import shared.GameSocket;
import shared.MessageHandler;
import shared.Connection;

public class Server {
    private static Game game = new Game();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(3000);
        game.start();

        while (true) {
            GameSocket client = new GameSocket(server.accept());
            System.out.println("Client connected");
            Connection connection = new PlayerConnection(client, game);
            connection.putMessageHandler("dir", new DirMessageHandler());
            game.registerClient(connection);
            connection.start();
        }
    }

    public static class DirMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner, Connection connection) {
            Float x = scanner.nextFloat();
            Float y = scanner.nextFloat();
            game.setDirection(connection.getPlayerName(), x, y);
            game.broadcast("dir "
                    + connection.getPlayerName() + " "
                    + Float.toString(x) + " "
                    + Float.toString(y));
        }
    }
}
