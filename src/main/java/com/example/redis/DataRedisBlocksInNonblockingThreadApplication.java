package com.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class DataRedisBlocksInNonblockingThreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataRedisBlocksInNonblockingThreadApplication.class, args);
	}

}

@RestController
class Ctrl {

	@Autowired
	ReactiveRedisTemplate<Object, Object> template;

	AtomicInteger counter = new AtomicInteger();

	@GetMapping(path = "test", produces = MediaType.APPLICATION_JSON_VALUE)
	Mono<Holder> has() {
		return Mono.just(new Holder(counter.getAndIncrement()))
				.flatMap(it -> template.hasKey("key")
						.thenReturn(it)
				)
				.log("beforeTimeOut")
				.timeout(Duration.ofMillis(500))
//				.timeout(Duration.ofMillis(500), Schedulers.elastic())//WA
				.log("afterTimeOut")
				;
	}
}

class Holder {
	int id;

	public Holder(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Holder{" + id + "}";
	}
}
