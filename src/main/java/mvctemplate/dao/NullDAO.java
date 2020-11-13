package mvctemplate.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

import mvctemplate.model.Model;

public class NullDAO implements DAO {

	private Model model;
	
	
	public NullDAO() {
		model = new Model();
	}

	@Override
	public void persist() throws FileNotFoundException, IOException {
		
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public void setModel(Model model) {
		// TODO Auto-generated method stub
		this.model = model;

	}

}
