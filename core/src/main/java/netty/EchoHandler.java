package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class EchoHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOG = LoggerFactory.getLogger(EchoHandler.class);
    private static final ConcurrentLinkedQueue<ChannelHandlerContext> clients
            = new ConcurrentLinkedQueue<>();

    private static int counter = 0;
    private String userName;
    private String path = "server/serverFiles";
    private String varPath = path;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        counter++;
        clients.add(ctx);
        userName = "user" + counter;
        ctx.writeAndFlush("Welcome to MikeOS. Show commands with --list\r\n");
        LOG.info("Client with nick: {}, with ip: {} connected", userName, ctx.channel().localAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        counter--;
        clients.remove(ctx);
        LOG.info("Client with nick: {}, with ip: {} leave", userName, ctx.channel().localAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String s) throws Exception {
        for (ChannelHandlerContext client : clients) {
            client.writeAndFlush("[" + userName + "]: " + s);
        }
//        Netty считывает сообщения с добавлением "\r\n"
        if (s.equals("--list\r\n")) {
            context.writeAndFlush("echo\r\ncat\r\ncd\r\ntouch\r\nls\r\nmkdir\r\nwrite\r\n");
//            Считывание файла
        } else if (s.startsWith("cat")) {
            String fileName = s.replace("cat ", "");
            fileName = fileName.replace("\r\n", "");
            String catPath = varPath + "\\" + fileName;
            List<String> info = Files.readAllLines(Path.of(catPath));
            for (int i = 0; i < info.size(); i++) {
                info.set(i, info.get(i) + "\r\n");
                context.writeAndFlush(info.get(i));
            }
            // Задание корневой папки
        } else if (s.startsWith("cd")) {
            String newPath = s.replace("cd ", "");
            newPath = newPath.replace("\r\n", "");
            if (newPath.equals("") || newPath.equals("cd")) {
                newPath = getVarPath() + "\r\n";
                context.writeAndFlush(newPath);
            } else {
                if (Files.isDirectory(Path.of(newPath(newPath).toString()), LinkOption.NOFOLLOW_LINKS)) {
                    Path directory = Paths.get(newPath(newPath).toString());
                    setVarPath(directory.toString());
                    String crPath = getVarPath() + "\r\n";
                    context.writeAndFlush(crPath);
                } else if (newPath.equals("/")){
                    setVarPath(path);
                    String crPath = path + "\r\n";
                    context.writeAndFlush(crPath);
                } else {
                    String errorDirectory = newPath + " is not directory" + "\r\n";
                    context.writeAndFlush(errorDirectory);
                }
            }
            // Создание файла
        } else if (s.startsWith("touch")) {
            String fileName = s.replace("touch ", "");
            fileName = fileName.replace("\r\n", "");
            Files.createFile(Path.of(varPath + "\\" + fileName));
            // выдача списка файлов/папок в текущей директории
        } else if (s.startsWith("ls")) {
            String info = Files.list(Path.of(varPath))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.joining(", "));
            info += "\r\n";
            context.writeAndFlush(info);
            // создание папки
        } else if (s.startsWith("mkdir")) {
            String dirName = s.split(" +")[1];
            dirName = dirName.replace("\r\n", "");
            if (Files.notExists(Path.of(varPath, dirName))) {
                Files.createDirectory(Path.of(varPath, dirName));
            }
        } else {
            context.writeAndFlush("Unknown command!\r\n");
        }
    }
    public String getPath() {
        return path;
    }

    public String getVarPath() {
        return varPath;
    }

    public void setVarPath(String varPath) {
        this.varPath = varPath;
    }

    private Path newPath(String path){
        Path newPath = Path.of(getPath() + "\\" + path);
        return newPath;
    }
}
