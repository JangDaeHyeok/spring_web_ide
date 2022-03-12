package ms.infotech.compiler.controller.compile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ms.infotech.compiler.builder.CompileBuilder;

@CrossOrigin
// @RestController
public class CompileController_bak {
	@Autowired CompileBuilder builder;
	
	@PostMapping(value="compile")
	public Map<String, Object> compileCode(@RequestBody Map<String, Object> input) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// byte[] rst = new byte[] {};
		
		Object obj = builder.compileCode(input.get("code").toString());
		/*
		if(obj == null) {
			// JVM 강제 종료(0: 정상 종료, 1: 비정상 종료)
			// System.exit(1);
			returnMap.put("result", "fail");
			returnMap.put("msg", "컴파일 에러 발생 - 코드를 확인해주세요.");
			returnMap.put("performance", "-");
			return returnMap;
		}
		*/
		
		// 실행할 Method에 전달할 파라미터 셋팅. 테스트 데이터
		// byte retult[] = new byte[] {'t', 'e', 's', 't'};
		
		// 실행 후 결과 전달 받음
		long beforeTime = System.currentTimeMillis();
		// rst = builder.runObject(obj, new byte[] {});
		String params[] = new String[] {"marina", "josipa", "nikola", "vinko", "filipa"};
		Map<String, Object> output = builder.runObject(obj, params);
		long afterTime = System.currentTimeMillis();
		
		/*
		// 결과 체크
		if(new String(rst).equals(new String(retult))) {
			returnMap.put("result", "success");
			returnMap.put("msg", "Success!");
		}else {
			returnMap.put("result", "fail");
			returnMap.put("msg", "Result Incorrect");
		}
		 */
		
		// reflection System out console 
		returnMap.put("output", output);
		// 소요시간
		returnMap.put("performance", (afterTime - beforeTime)/1000);
		
		return returnMap;
	}
	
	@PostMapping(value="stop")
	public void stopTomcatTest() throws Exception {
		System.exit(1);
	}
}
