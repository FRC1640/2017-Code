package vision;

import utilities.Subsystem;

public class Vision extends Subsystem{
	private VisionData visionData;

	@Override
	public void update() {
		visionData.update();
	}

	@Override
	public void init() {
		visionData = VisionData.getInstance();
	}

}
