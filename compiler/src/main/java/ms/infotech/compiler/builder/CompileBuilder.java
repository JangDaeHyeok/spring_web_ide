package ms.infotech.compiler.builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ms.infotech.compiler.execute.MethodExecutation;
import ms.infotech.compiler.model.result.ApiResponseResult;
import ms.infotech.compiler.util.common.UUIDUtil;

@Slf4j
@Component
public class CompileBuilder {
	// 프로젝트 home directory 경로
	// private final String path = CompilerApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private final String path = "C:/Users/sktel/Desktop/test/compile/";
	// private final String path = "/compile/";
	
	@SuppressWarnings({ "resource", "deprecation" })
	public Object compileCode(String body) throws Exception {
		String uuid = UUIDUtil.createUUID();
		String uuidPath = path + uuid + "/";
		
		// Source를 이용한 java file 생성
		File newFolder = new File(uuidPath);
		File sourceFile = new File(uuidPath + "DynamicClass.java");
		File classFile = new File(uuidPath + "DynamicClass.class");
		
		Class<?> cls = null;
		
		// compile System err console 조회용 변수
		ByteArrayOutputStream err = new ByteArrayOutputStream();
		PrintStream origErr = System.err;
		
		try {
			newFolder.mkdir();
			new FileWriter(sourceFile).append(body).close();
			
			// 만들어진 Java 파일을 컴파일
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			
			// System의 error outputStream을 ByteArrayOutputStream으로 받아오도록 설정
			System.setErr(new PrintStream(err));
			
			// compile 진행
			int compileResult = compiler.run(null, null, null, sourceFile.getPath());
			// compile 실패인 경우 에러 로그 반환
			if(compileResult == 1) {
				return err.toString();
			}
			
			// 컴파일된 Class를 Load
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {new File(uuidPath).toURI().toURL()});
			cls = Class.forName("DynamicClass", true, classLoader);
			
			// Load한 Class의 Instance를 생성
			return cls.newInstance();
		} catch (Exception e) {
			log.error("[CompileBuilder] 소스 컴파일 중 에러 발생 :: {}", e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			// Syetem error stream 원상태로 전환
			System.setErr(origErr);
			
			if(sourceFile.exists())
				sourceFile.delete();
			if(classFile.exists())
				classFile.delete();
			if(newFolder.exists())
				newFolder.delete();
		}
	}
	
	/*
	 * run method : parameter byte array, return byte array
	@SuppressWarnings("rawtypes")
	public byte[] runObject(Object obj, byte[] params) throws Exception {
		String methodName = "main";
		Class arguments[] = new Class[] {params.getClass()};
		
		// Source를 만들때 지정한 Method를 실행
		Method objMethod = obj.getClass().getMethod(methodName, arguments);
		Object result = objMethod.invoke(obj, params);
		return (byte[])result;
	}
	*/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> runObject(Object obj, Object[] params) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 실행할 메소드 명
		String methodName = "main";
		// 파라미터 타입
		Class arguments[] = new Class[] {params.getClass()};
		
		/*
		 * reflection method의 console output stream을 받아오기 위한 변수
		 * reflection method 실행 시 System의 out, error outputStream을 ByteArrayOutputStream으로 받아오도록 설정
		 * 실행 완료 후 다시 원래 System으로 전환
		 */
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream err = new ByteArrayOutputStream();
		PrintStream origOut = System.out;
		PrintStream origErr = System.err;
		try {
			// System의 out, error outputStream을 ByteArrayOutputStream으로 받아오도록 설정
			System.setOut(new PrintStream(out));
			System.setErr(new PrintStream(err));
			
			// 메소드 timeout을 체크하며 실행(15초 초과 시 강제종료)
			boolean result = false;
			result = MethodExecutation.timeOutCall(obj, methodName, params, arguments);
			
			// stream 정보 저장
			if(result) {
				returnMap.put("result", ApiResponseResult.SUCEESS.getText());
				if(err.toString() != null && !err.toString().equals("")) {
					returnMap.put("SystemOut", err.toString());
				}else {
					returnMap.put("SystemOut", out.toString());
				}
			}else {
				returnMap.put("result", ApiResponseResult.FAIL.getText());
				returnMap.put("SystemOut", "제한 시간 초과");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			// Syetem out, error stream 원상태로 전환
			System.setOut(origOut);
			System.setErr(origErr);
		}
		
		return returnMap;
	}
}
