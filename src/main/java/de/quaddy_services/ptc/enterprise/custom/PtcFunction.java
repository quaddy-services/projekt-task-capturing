package de.quaddy_services.ptc.enterprise.custom;

import java.util.List;

public interface PtcFunction {
	/**
	 * Get the Tasknames from booking system.
	 * 
	 * @param aUserId
	 * @return List<String> of Tasknames for aUserId
	 */
	public List<String> getBookableTaskNames(String aUserId);

	/**
	 * Save the tasks for the user.
	 * 
	 * @param aUserId
	 * @param aTasks
	 * @return String info for the user.
	 */
	public String saveReport(String aUserId, List<PtcTask> aTasks);
}
