####Spring Data redis could close connection in NonBlocking Thread
#####TC
1. start redis (docker-compose up -d) 
2. start application 
3. HTTP GET localhost:8080/test
ER=AR: data returned
4. stop redis (docker-compose down)
5. HTTP GET localhost:8080/test
ER: Get HTTP Error code by timeout
AR: response hangs indefinitely

```text
2019-05-09 18:40:43.146  INFO [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [ctor-http-nio-2] afterTimeOut                             : | onContextUpdate(Context1{class brave.Span=NoopSpan(5f69afad4908dd11/5f69afad4908dd11)})
2019-05-09 18:40:43.146  INFO [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [ctor-http-nio-2] afterTimeOut                             : | onSubscribe([Fuseable] FluxOnAssembly.OnAssemblySubscriber)
2019-05-09 18:40:43.146  INFO [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [ctor-http-nio-2] afterTimeOut                             : | request(unbounded)
2019-05-09 18:40:43.148  INFO [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [ctor-http-nio-2] beforeTimeOut                            : | onContextUpdate(Context1{class brave.Span=NoopSpan(5f69afad4908dd11/5f69afad4908dd11)})
2019-05-09 18:40:43.148  INFO [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [ctor-http-nio-2] beforeTimeOut                            : | onSubscribe([Fuseable] FluxOnAssembly.OnAssemblySubscriber)
2019-05-09 18:40:43.148  INFO [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [ctor-http-nio-2] beforeTimeOut                            : | request(unbounded)
2019-05-09 18:40:43.649  INFO [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [     parallel-2] beforeTimeOut                            : | cancel()
2019-05-09 18:40:43.653 ERROR [redis-test,5f69afad4908dd11,5f69afad4908dd11,false] 20644 --- [     parallel-2] reactor.core.publisher.Operators         : Operator called default onErrorDropped

java.lang.IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread parallel-2
	at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:77) ~[reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.Mono.block(Mono.java:1494) ~[reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.data.redis.connection.ReactiveRedisConnection.close(ReactiveRedisConnection.java:60) ~[spring-data-redis-2.1.6.RELEASE.jar:2.1.6.RELEASE]
	at org.springframework.data.redis.core.ReactiveRedisTemplate.lambda$doInConnection$3(ReactiveRedisTemplate.java:198) ~[spring-data-redis-2.1.6.RELEASE.jar:2.1.6.RELEASE]
	at reactor.core.publisher.FluxDoFinally$DoFinallySubscriber.runFinally(FluxDoFinally.java:156) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.FluxDoFinally$DoFinallySubscriber.cancel(FluxDoFinally.java:145) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.cancel(ScopePassingSpanSubscriber.java:87) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.cancel(FluxOnAssembly.java:443) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.MonoNext$NextSubscriber.cancel(MonoNext.java:108) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.cancel(ScopePassingSpanSubscriber.java:87) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.cancel(FluxOnAssembly.java:443) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.cancel(ScopePassingSpanSubscriber.java:87) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.cancel(FluxOnAssembly.java:443) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.Operators.terminate(Operators.java:1036) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreInner.cancel(MonoIgnoreThen.java:244) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.cancel(MonoIgnoreThen.java:184) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.cancel(ScopePassingSpanSubscriber.java:87) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.cancel(FluxOnAssembly.java:443) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.Operators.terminate(Operators.java:1036) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.MonoFlatMap$FlatMapInner.cancel(MonoFlatMap.java:264) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.MonoFlatMap$FlatMapMain.cancel(MonoFlatMap.java:181) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.cancel(ScopePassingSpanSubscriber.java:87) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.cancel(FluxOnAssembly.java:443) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.FluxPeekFuseable$PeekFuseableSubscriber.cancel(FluxPeekFuseable.java:153) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.cancel(ScopePassingSpanSubscriber.java:87) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.cancel(FluxOnAssembly.java:443) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.Operators$MultiSubscriptionSubscriber.drainLoop(Operators.java:1935) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.Operators$MultiSubscriptionSubscriber.drain(Operators.java:1904) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.Operators$MultiSubscriptionSubscriber.cancel(Operators.java:1716) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.FluxTimeout$TimeoutMainSubscriber.handleTimeout(FluxTimeout.java:287) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.FluxTimeout$TimeoutMainSubscriber.doTimeout(FluxTimeout.java:273) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.FluxTimeout$TimeoutTimeoutSubscriber.onNext(FluxTimeout.java:390) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.StrictSubscriber.onNext(StrictSubscriber.java:89) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onNext(FluxOnAssembly.java:353) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.onNext(ScopePassingSpanSubscriber.java:96) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:73) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onNext(FluxOnAssembly.java:353) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.reactor.ScopePassingSpanSubscriber.onNext(ScopePassingSpanSubscriber.java:96) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at reactor.core.publisher.MonoDelay$MonoDelayRunnable.run(MonoDelay.java:117) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.scheduler.SchedulerTask.call(SchedulerTask.java:50) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at reactor.core.scheduler.SchedulerTask.call(SchedulerTask.java:27) [reactor-core-3.2.8.RELEASE.jar:3.2.8.RELEASE]
	at org.springframework.cloud.sleuth.instrument.async.TraceCallable.call(TraceCallable.java:70) [spring-cloud-sleuth-core-2.1.1.RELEASE.jar:2.1.1.RELEASE]
	at java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:266) [na:1.8.0_191-1-ojdkbuild]
	at java.util.concurrent.FutureTask.run(FutureTask.java) [na:1.8.0_191-1-ojdkbuild]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$201(ScheduledThreadPoolExecutor.java:180) [na:1.8.0_191-1-ojdkbuild]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:293) [na:1.8.0_191-1-ojdkbuild]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_191-1-ojdkbuild]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_191-1-ojdkbuild]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_191-1-ojdkbuild]

2019-05-09 18:40:43.661 ERROR [redis-test,5f69afad4908dd11,76591d8cfd399979,false] 20644 --- [     parallel-2] reactor.core.scheduler.Schedulers        : Scheduler worker in group main failed with an uncaught exception
```
 
  
