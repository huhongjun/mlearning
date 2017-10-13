package zhijin;
public class CourseProgress {

	private String memberId;
	private String progressJson;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getProgressJson() {
		return progressJson;
	}

	public void setProgressJson(String progressJson) {
		this.progressJson = progressJson;
	}

	@Override
	public String toString() {
		return "CourseProgress [memberId=" + memberId + ", progressJson="
				+ progressJson + "]";
	}

}
