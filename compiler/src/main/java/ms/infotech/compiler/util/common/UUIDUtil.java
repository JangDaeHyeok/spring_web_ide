package ms.infotech.compiler.util.common;

import java.util.UUID;

public class UUIDUtil {
	/**
	 * @Developer 이강민
	 * @Description UUID 생성시 "-" 제거
	 */
	public static String createUUID() {
		String uuid = UUID.randomUUID().toString().replace("-","");
		return uuid;
	}
	
	/**
	 * @Developer 이강민
	 * @Description UUID 생성시 "-" 제거 및 16자리 숫자로 Cut
	 */
	public static String createUUID16() {
		String uuid = UUID.randomUUID().toString().replace("-","").substring(0, 16).toUpperCase();
		return uuid;
	}
}
