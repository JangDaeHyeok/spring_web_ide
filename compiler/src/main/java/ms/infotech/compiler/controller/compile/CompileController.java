package ms.infotech.compiler.controller.compile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ms.infotech.compiler.builder.CompileBuilder;
import ms.infotech.compiler.model.result.ApiResponseResult;

@CrossOrigin
@RestController
public class CompileController {
	@Autowired CompileBuilder builder;
	
	@PostMapping(value="compile")
	public Map<String, Object> compileCode(@RequestBody Map<String, Object> input) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// compile input code
		Object obj = builder.compileCode(input.get("code").toString());
		
		// compile 결과 타입이 String일 경우 컴파일 실패 후 메시지 반환으로 판단하여 처리
		if(obj instanceof String) {
			returnMap.put("result", ApiResponseResult.FAIL.getText());
			returnMap.put("SystemOut", obj.toString());
			return returnMap;
		}
		
		// 실행 후 결과 전달 받음
		long beforeTime = System.currentTimeMillis();
		// 파라미터
		String params[] = new String[] {"marina", "josipa", "nikola", "vinko", "filipa"};
		// 코드 실행
		Map<String, Object> output = builder.runObject(obj, params);
		long afterTime = System.currentTimeMillis();
		
		// 코드 실행 결과 저장
		returnMap.putAll(output);
		// 소요시간
		returnMap.put("performance", (afterTime - beforeTime));
		
		return returnMap;
	}
	
	@PostMapping(value="stop")
	public void stopTomcatTest() throws Exception {
		System.exit(1);
	}
}
