package ms.infotech.compiler.model.result;

public enum ApiResponseResult {
	SUCEESS("성공"),
	FAIL("실패");
	
	public final String message;
	
	ApiResponseResult(String message) {
		this.message = message;
	}
	
	public String getId() {
		return this.name();
	}
	
	public String getText() {
		return this.message;
	}
}
