package com.jason.TimePOJODemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by jc6t on 2015/6/30.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UnixTime m=(UnixTime) msg;
        System.out.println(m);
        ctx.close();
    }
}
