package tutorials.VideoUploadClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class VideoUploadClient {
	private static final String SOURCE_URL = "http://techslides.com/demos/sample-videos/small.mp4";
	private static final String SERVICE_END_POINT = "http://SERVICE_HOST/PATH/video/upload";

	public static void main(String[] args) throws MalformedURLException,
			URISyntaxException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		
		WebResource resource = client.resource(SERVICE_END_POINT);
		WebResource.Builder builder = resource
				.type(MediaType.MULTIPART_FORM_DATA_TYPE);

		File sourceVideoFile = getSourceVideoFile();
		
		FormDataMultiPart multiPart = new FormDataMultiPart();
		FileDataBodyPart fdbp = new FileDataBodyPart("video", sourceVideoFile);
		multiPart.bodyPart(fdbp);

		String response = builder.post(String.class, multiPart);
		System.out.println(response);
	}

	private static File getSourceVideoFile() {
		File sourceVideoFile = new File("sample.mp4");
		try {
			FileOutputStream fos = new FileOutputStream(sourceVideoFile);
			fos.write(readFromUrl(SOURCE_URL).toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sourceVideoFile;
	}

	private static ByteArrayOutputStream readFromUrl(String sourceUrl) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			URL url = new URL(sourceUrl);
			InputStream is = null;
			try {
				is = url.openStream();
				byte[] byteChunk = new byte[4096];
				int n;
				while ((n = is.read(byteChunk)) > 0) {
					baos.write(byteChunk, 0, n);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		return baos;
	}
}
