package de.quaddy_services.ptc.enterprise;

public class CommentDuration implements Comparable<CommentDuration> {
	private Long duration;
	private String comment;

	public CommentDuration(Long aDuration, String aComment) {
		super();
		duration = aDuration;
		comment = aComment.trim();
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long aDuration) {
		duration = aDuration;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String aComment) {
		comment = aComment;
	}

	@Override
	public int compareTo(CommentDuration aO) {
		int tempCompareTo = getDuration().compareTo(aO.getDuration());
		if (Integer.MIN_VALUE == tempCompareTo) {
			return 1;
		} else if (Integer.MAX_VALUE == tempCompareTo) {
			return -1;
		}
		return -tempCompareTo;
	}

}
