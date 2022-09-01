import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    // Почему NIO: данный вид взаимодействия более гибкий, чем IO,
    // не нужно ждать окончания выполнения работы на сервере, здесь мы можем получить и промежуточный результат

    private static final int PORT = 16457;
    private static final String HOST = "127.0.0.1";
    private static final int TIMEOUT = 2000;

    public static void main(String[] args) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 12);
            String msg;

            while (true) {
                System.out.println("Enter message for server...");
                msg = scanner.nextLine();
                if (msg.equals("end")) break;
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(TIMEOUT);
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }
}