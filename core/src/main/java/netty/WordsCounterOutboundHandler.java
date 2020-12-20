package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WordsCounterOutboundHandler extends ChannelOutboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(WordsCounterOutboundHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        int wordsCount = (int) msg;
        LOG.info("wordsCount: {}", wordsCount);
        ctx.write("Received " + wordsCount + " words.\n\r");
    }

}
