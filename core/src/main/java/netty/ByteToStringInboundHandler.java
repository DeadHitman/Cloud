package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteToStringInboundHandler extends ChannelInboundHandlerAdapter {

    // net ---->
    private static final Logger LOG = LoggerFactory.getLogger(ByteToStringInboundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        LOG.info("buf={}", buf);
        StringBuilder s = new StringBuilder();
        while (buf.isReadable()) {
            s.append((char) buf.readByte());
        }
        String message = s.toString();
        message = message.replaceAll("[\n\r]", "");
        LOG.info("message from client: {}", message);
        ctx.fireChannelRead(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("error=", cause);
    }
}
