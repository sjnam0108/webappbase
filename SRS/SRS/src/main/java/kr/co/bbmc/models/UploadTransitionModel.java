package kr.co.bbmc.models;

public class UploadTransitionModel {
	
	private String type = "NO";
	private String message = "";
	
	private String saveUrl = "/common/uploadsave";

	private String allowedExtensions = "";
	private String code = "";

	
	public UploadTransitionModel() { }

	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAllowedExtensions() {
		return allowedExtensions;
	}

	public void setAllowedExtensions(String allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}
}
