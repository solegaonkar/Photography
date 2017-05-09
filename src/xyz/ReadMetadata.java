package xyz;

import java.io.File;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata.Item;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;

public class ReadMetadata {

	public static void main(String[] args) throws Exception {
		IImageMetadata imd = Sanselan.getMetadata(new File("C:\\Users\\vs0016025\\Desktop\\Images\\Abstract001.JPG"));

		if (imd instanceof JpegImageMetadata) {
			for (Object o : ((JpegImageMetadata) imd).getItems()) {
				Item item = (Item) o;
				System.out.println(item.getKeyword() + ":" + item.getText());
			}
		}
		//System.out.println(imd.toString());
		
		//System.out.println(Sanselan.getImageInfo(new File("C:\\Users\\vs0016025\\Desktop\\Images\\Abstract001.JPG")).toString());
		
		//System.out.println(Sanselan.getXmpXml(new File("C:\\Users\\vs0016025\\Desktop\\Images\\Abstract001.JPG")));
		
		
	}
}
