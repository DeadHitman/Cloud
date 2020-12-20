package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class FinalHandler extends ChannelOutboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(FinalHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String message = (String) msg;
        LOG.debug("finalMessage = {}", message);
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeCharSequence(message, StandardCharsets.UTF_8);
        buf.retain();
        ctx.writeAndFlush(buf);
    }
}
