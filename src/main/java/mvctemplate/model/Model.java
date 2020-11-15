package mvctemplate.model;

public class Model implements java.io.Serializable {
	
	/**
	 * Serialiserad data är:
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** Function called by the controller to check consistency when updating the model.
	 * 
	 */
	public void checkConsistency() {
	}
	
	public String getState() {
		
		StringBuffer buf = new StringBuffer();

		
		return buf.toString();
	}



}
