package mvctemplate.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import mvctemplate.model.Model;
import util.serial.ObjectStreamUtil;


public class ObjectStreamDAO implements DAO {

	private String filename, directory, fileprefix, filesuffix;
	private Model model = null;
	
	
	public ObjectStreamDAO() throws ClassNotFoundException, IOException {
		
		directory = "./data/";
		fileprefix = "game";
		filesuffix = ".dat";
		
		
		if (Files.notExists(Paths.get(directory))) {
			Files.createDirectory(Paths.get(directory));
		}
		
		
		filename = directory + fileprefix + filesuffix;
		
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
		ObjectStreamUtil.writeObject(directory + fileprefix + (new Date()).getTime() + filesuffix, model);
				
	}

	@Override
	public void setModel(Model model) {
		this.model = model;
		
	}

}
