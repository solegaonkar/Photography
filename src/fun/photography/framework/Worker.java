package fun.photography.framework;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * A worker can be instantiated with a list of processors. They work on the image and are invoked one after the other in
 * a thread.
 * 
 * @author vs0016025
 *
 */
public class Worker extends Thread {
	private Processor[]	processors	= null;
	private Photograph	image		= null;
	private File		output		= null;

	public Worker(Photograph input, File output, Processor... processors) {
		super();
		this.image = input.clone();
		this.output = output;
		this.processors = processors;
		this.setDaemon(true);
	}

	@Override
	public void run() {
		for (int i = 0; i < processors.length; i++) {
			if (processors[i] == null)
				throw new NullPointerException("The Processor cannot be null.");
			processors[i].process(image);
		}
		try {
			image.save(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
