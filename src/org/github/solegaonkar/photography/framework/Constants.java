package org.github.solegaonkar.photography.framework;

import java.io.File;

public class Constants {
	public static final String	BASE_DIRECTORY	= System.getProperty("user.dir");
	public static final String	OriginalsPath	= BASE_DIRECTORY + File.separator + "originals";
	public static final String	ImagesPath		= BASE_DIRECTORY + File.separator + "originals";
	public static final String	DataFile		= BASE_DIRECTORY + File.separator + "db.object";
	public static final float	SaveQuality		= 0.9f;
	public static final String	Signature		= "Vikas K Solegaonkar";
}
