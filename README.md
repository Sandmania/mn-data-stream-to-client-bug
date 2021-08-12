# Data streaming directly from client to response broken

This sample application tries to download Alphabet Inc's Form 10-K from `https://abc.xyz/investor/static/pdf/20210203_alphabet_10K.pdf`

## Micronaut 2.5.11 (does not work)

Run: `mvn clean mn:run`
Open browser: `http://localhost:8080/dl/10k`
Observe that a PDF file does not open and the following appears in the logs:

```
09:40:41.842 [default-nioEventLoopGroup-1-14] DEBUG i.m.h.s.netty.RoutingInBoundHandler - Request GET /dl/10k
09:40:41.886 [default-nioEventLoopGroup-1-16] DEBUG i.m.h.client.netty.DefaultHttpClient - Sending HTTP GET to https://abc.xyz/investor/static/pdf/20210203_alphabet_10K.pdf
09:40:41.946 [default-nioEventLoopGroup-1-16] DEBUG io.netty.handler.ssl.SslHandler - [id: 0x50a3c595, L:/192.168.10.101:57356 - R:abc.xyz/172.217.21.142:443] HANDSHAKEN: protocol:TLSv1.2 cipher suite:TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
09:40:42.001 [default-nioEventLoopGroup-1-14] WARN  io.netty.util.ReferenceCountUtil - Failed to release a message: PooledSlicedByteBuf(freed)
io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1
	at io.netty.util.internal.ReferenceCountUpdater.toLiveRealRefCnt(ReferenceCountUpdater.java:74)
	at io.netty.util.internal.ReferenceCountUpdater.release(ReferenceCountUpdater.java:138)
	at io.netty.buffer.AbstractReferenceCountedByteBuf.release(AbstractReferenceCountedByteBuf.java:100)
	at io.netty.util.ReferenceCountUtil.release(ReferenceCountUtil.java:88)
	at io.netty.util.ReferenceCountUtil.safeRelease(ReferenceCountUtil.java:113)
	at io.netty.channel.ChannelOutboundBuffer.remove0(ChannelOutboundBuffer.java:306)
	at io.netty.channel.ChannelOutboundBuffer.failFlushed(ChannelOutboundBuffer.java:660)
	at io.netty.channel.AbstractChannel$AbstractUnsafe.closeOutboundBufferForShutdown(AbstractChannel.java:690)
	at io.netty.channel.AbstractChannel$AbstractUnsafe.shutdownOutput(AbstractChannel.java:683)
	at io.netty.channel.AbstractChannel$AbstractUnsafe.handleWriteError(AbstractChannel.java:971)
	at io.netty.channel.AbstractChannel$AbstractUnsafe.flush0(AbstractChannel.java:951)
	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.flush0(AbstractNioChannel.java:354)
	at io.netty.channel.AbstractChannel$AbstractUnsafe.flush(AbstractChannel.java:913)
	at io.netty.channel.DefaultChannelPipeline$HeadContext.flush(DefaultChannelPipeline.java:1372)
	at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:750)
	at io.netty.channel.AbstractChannelHandlerContext.invokeFlush(AbstractChannelHandlerContext.java:742)
	at io.netty.channel.AbstractChannelHandlerContext.flush(AbstractChannelHandlerContext.java:728)
	at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.flush(CombinedChannelDuplexHandler.java:531)
	at io.netty.channel.ChannelOutboundHandlerAdapter.flush(ChannelOutboundHandlerAdapter.java:125)
	at io.netty.channel.CombinedChannelDuplexHandler.flush(CombinedChannelDuplexHandler.java:356)
	at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:750)
	at io.netty.channel.AbstractChannelHandlerContext.invokeFlush(AbstractChannelHandlerContext.java:742)
	at io.netty.channel.AbstractChannelHandlerContext.flush(AbstractChannelHandlerContext.java:728)
	at io.netty.channel.ChannelDuplexHandler.flush(ChannelDuplexHandler.java:127)
	at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:750)
	at io.netty.channel.AbstractChannelHandlerContext.invokeWriteAndFlush(AbstractChannelHandlerContext.java:765)
	at io.netty.channel.AbstractChannelHandlerContext$WriteTask.run(AbstractChannelHandlerContext.java:1071)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:469)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:834)
```

## With micronaut 2.2.0 (works)

Run: `mvn -f pom220.xml clean mn:run` (this pom has micronaut version set to 2.2.0)
Open browser: `http://localhost:8080/dl/10k`
Observe that a PDF file is opened and the logs seem clean:

```
09:38:59.719 [default-nioEventLoopGroup-1-2] DEBUG i.m.h.server.netty.NettyHttpServer - Server localhost:8080 Received Request: GET /dl/10k
09:38:59.719 [default-nioEventLoopGroup-1-2] DEBUG i.m.h.s.netty.RoutingInBoundHandler - Matching route GET - /dl/10k
09:38:59.719 [default-nioEventLoopGroup-1-2] DEBUG i.m.h.s.netty.RoutingInBoundHandler - Matched route GET - /dl/10k to controller class mn.issue.DownloadController
09:38:59.722 [default-nioEventLoopGroup-1-2] DEBUG i.m.c.e.ApplicationEventPublisher - Publishing event: io.micronaut.http.context.event.HttpRequestReceivedEvent[source=GET /dl/10k]
09:38:59.722 [default-nioEventLoopGroup-1-2] DEBUG i.m.context.DefaultBeanContext - Resolving beans for type: <HttpRequestReceivedEvent> io.micronaut.context.event.ApplicationEventListener
09:38:59.769 [default-nioEventLoopGroup-1-6] DEBUG i.m.h.client.netty.DefaultHttpClient - Sending HTTP Request: GET /investor/static/pdf/20210203_alphabet_10K.pdf
09:38:59.770 [default-nioEventLoopGroup-1-6] DEBUG i.m.h.client.netty.DefaultHttpClient - Chosen Server: abc.xyz(-1)
09:38:59.830 [default-nioEventLoopGroup-1-6] DEBUG io.netty.handler.ssl.SslHandler - [id: 0x1af8f7d9, L:/192.168.10.101:57310 - R:abc.xyz/172.217.21.142:443] HANDSHAKEN: protocol:TLSv1.2 cipher suite:TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
09:39:00.125 [default-nioEventLoopGroup-1-2] DEBUG i.m.c.e.ApplicationEventPublisher - Publishing event: io.micronaut.http.context.event.HttpRequestTerminatedEvent[source=GET /dl/10k]
09:39:00.126 [default-nioEventLoopGroup-1-2] DEBUG i.m.context.DefaultBeanContext - Resolving beans for type: <HttpRequestTerminatedEvent> io.micronaut.context.event.ApplicationEventListener
```