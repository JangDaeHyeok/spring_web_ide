package ms.infotech.compiler.execute;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MethodExecutation {
	private final static long TIMEOUT_LONG = 15000; // 15초
	
	public static Boolean timeOutCall(Object obj, String methodName, Object[] params, Class<? extends Object> arguments[]) throws Exception {
		// Source를 만들때 지정한 Method
		Method objMethod = obj.getClass().getMethod(methodName, arguments);
		
		Boolean result = false;
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Callable<Boolean> task = new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					// 아래 주석 해제시 timeout 테스트 가능
					// Thread.sleep(4000);
					
					// Method 실행
					objMethod.invoke(obj, new Object[] {params});
					return true;
				}
			};
			
		Future<Boolean> future = executorService.submit(task);
		try {
			// 타임아웃 감시할 작업 실행
			result = (Boolean) future.get(TIMEOUT_LONG, TimeUnit.MILLISECONDS); // timeout을 설정
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// e.printStackTrace();
			result = false;
		} finally {
			executorService.shutdown();
		}
		
		return result;
	}
}
