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
		String participant[] = new String[] {"marina", "josipa", "nikola", "vinko", "filipa"};
		String completion[] = new String[] {"josipa", "filipa", "marina", "nikola"};
		Object[] params = {participant, completion};
		
		// 코드 실행
		Map<String, Object> output = builder.runObject(obj, params);
		long afterTime = System.currentTimeMillis();
		
		// 코드 실행 결과 저장
		returnMap.putAll(output);
		// 소요시간
		returnMap.put("performance", (afterTime - beforeTime));
		
		// s :: 결과 체크 :: //
		// TODO 상황에 따른 결과 동적 체크 처리 필요
		try {
			if(returnMap.get("return") != null && !returnMap.get("return").equals("vinko")) {
				returnMap.put("result", ApiResponseResult.FAIL.getText());
				returnMap.put("SystemOut", returnMap.get("SystemOut").toString() + "\r\n결과 기대값과 일치하지 않습니다.");
			}
		}catch (Exception e) {
			returnMap.put("result", ApiResponseResult.FAIL.getText());
			returnMap.put("SystemOut", returnMap.get("SystemOut").toString() + "예상치 못한 오류로 검사에 실패했습니다.");
		}
		// e :: 결과 체크 :: //
		
		return returnMap;
	}
	
	@PostMapping(value="stop")
	public void stopTomcatTest() throws Exception {
		System.exit(1);
	}
}
