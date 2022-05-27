package ms.infotech.compiler.util.server;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServerOffUtil {
	// @Scheduled(cron = "0 0 */3 * * *")
	public void stopTomcatTest() throws Exception {
		log.info("[ServerOffUtil] 서버 종료");
		System.exit(1);
	}
}
