package com.jason.TimeServerClientDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jc6t on 2015/6/25.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter{

/*    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        *//**
         * print received msg
         *//*
//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
        *//**
         * echo msg
         *//*
        ctx.write(msg);
        ctx.flush();
    }*/

    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        System.out.println("-----channelActive-----");
        System.out.println(new Date(System.currentTimeMillis()));

        for (int i = 0; i < 10; i++) {
            final ByteBuf time = ctx.alloc().buffer(4); // (2)
            long sysTime=System.currentTimeMillis() / 1000L ; // 目前看起來官網的demo，加上2208988800L，沒有什麼意義
            System.out.println("---------" + new Date(sysTime) + "---------");
            time.writeInt((int) sysTime);
            ChannelFuture f = ctx.writeAndFlush(time); // (3)
            f.addListener(new ChannelFutureListener() {
                private int count=0;
                @Override
                public void operationComplete(ChannelFuture future) {
//                    assert f == future;
                    count++;
                    System.out.println("count:"+count+"----operationComplete---------");
                }
            }); // (4)
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
