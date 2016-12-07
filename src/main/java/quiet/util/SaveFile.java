package quiet.util;

import java.io.*;

public class SaveFile {
	//static Logger logger = LoggerFactory.getLogger(SaveFile.class);
	public static File saveAs(InputStream input, String target, int buffer){
		//logger.info("in #saveAs# method.");
		File savedFile = null;		
		BufferedInputStream bis;
		FileOutputStream fos;
		BufferedOutputStream bos;
		File targ = new File(target);
		File parent = targ.getParentFile();
		byte[] data;
		int count;
		try {
			bis = new BufferedInputStream(input);
			if(parent != null && !parent.exists()){
				parent.mkdirs();
			}
			fos = new FileOutputStream(targ);
			bos = new BufferedOutputStream(fos, buffer);
			data = new byte[buffer];
			while((count = bis.read(data, 0, buffer)) != -1){
				bos.write(data, 0, count);
			}
			bos.flush();
			bos.close();
			bis.close();
			savedFile = new File(target);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//logger.info("leave #saveAs# method.");
		return savedFile;	
	}
}
