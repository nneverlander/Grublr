package com.grublr.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 * Created by adi on 9/1/15.
 */
public class SlimClient {

    public static void main(String[] args) {
        findFood();
        //shareFood();
    }

    private static void shareFood() {
        String TARGET_URL = "https://grublr-0831.appspot.com/r/food/share";
        String share = fileToString("/home/adi/Projects/Grublr/share.json");

        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();
        WebTarget webTarget = client.target(TARGET_URL);
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
                new File("/home/adi/Downloads/Aisha/Cover.png"),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        FormDataBodyPart formDataBodyPart = new FormDataBodyPart("metadata", share, MediaType.TEXT_PLAIN_TYPE);
        multiPart.bodyPart(fileDataBodyPart);
        multiPart.bodyPart(formDataBodyPart);

        Response response = webTarget.request(MediaType.TEXT_HTML_TYPE).post(Entity.entity(multiPart, multiPart.getMediaType()));

        System.out.println(response.getStatus() + " "
                + response.getStatusInfo() + " " + response);
    }

    private static void findFood() {
        //String TARGET_URL = "https://grublr-0831.appspot.com/r/food/find";
        String TARGET_URL = "http://localhost:8080/r/food/find";
        String find = fileToString("/home/adi/Projects/Grublr/find.json");

        Client client = ClientBuilder.newBuilder().build();
        WebTarget webTarget = client.target(TARGET_URL);
        Response response = webTarget.request(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(Entity.json(find));

        System.out.println(response.getStatus() + " "
                + response.getStatusInfo() + " " + response);
    }

    public static String fileToString(String fileName) {

        // This will reference one line at a time
        String line = null;
        StringBuilder sb = new StringBuilder();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);


            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return sb.toString();
    }
}
