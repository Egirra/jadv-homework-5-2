import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {

    private static final int PORT = 16457;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChanel = ServerSocketChannel.open();
        serverChanel.bind(new InetSocketAddress(HOST, PORT));

        while (true) {
            try (SocketChannel socketChannel = serverChanel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 12);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) break;
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    final String newMsg = msg.replaceAll("\\s", "");
                    inputBuffer.clear();
                    System.out.println(newMsg);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}