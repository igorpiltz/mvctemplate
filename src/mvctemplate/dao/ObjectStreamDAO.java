package mvctemplate.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import parallellroutineexecutor.model.Model;
import util.serial.ObjectStreamUtil;


public class ObjectStreamDAO implements DAO {

	private String filename, fileprefix, filesuffix;
	private Model model = null;
	
	
	public ObjectStreamDAO() throws ClassNotFoundException, IOException {
		
		fileprefix = "./data/game";
		filesuffix = ".dat";
		
		filename = fileprefix + filesuffix;
		
		try {
			model = (Model)ObjectStreamUtil.readObject(filename);
		} catch (FileNotFoundException ex) {
			model = new Model(); 
		}
		
					
	}
	
	@Override
	public Model getModel() {

		return model;
		
	}

	@Override
	public void persist() throws FileNotFoundException, IOException {
		ObjectStreamUtil.writeObject(filename, model);
		ObjectStreamUtil.writeObject(fileprefix + (new Date()).getTime() + filesuffix, model);
				
	}

	@Override
	public void setModel(Model model) {
		this.model = model;
		
	}

}
