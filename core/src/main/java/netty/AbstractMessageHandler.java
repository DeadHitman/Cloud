package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        LOG.info("message = {}", msg);
    }
}
