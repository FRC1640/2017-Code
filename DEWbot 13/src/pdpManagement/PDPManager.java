package pdpManagement;

import utilities.Subsystem;

public class PDPManager extends Subsystem {
	PDPManagerIO pdpManagerIO;

	@Override
	public void update() {
		pdpManagerIO.update();
	}

	@Override
	public void init() {
		pdpManagerIO = PDPManagerIO.getInstance();
	}

}
