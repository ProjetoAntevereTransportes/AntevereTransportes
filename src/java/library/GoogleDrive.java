package library;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;
import contratos.ModuloEnum;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class GoogleDrive {

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME
            = "Drive API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    private static java.io.File DATA_STORE_DIR = null;

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     */
    private static final List<String> SCOPES
            = Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            contratos.Arquivo a = new database.FileUpload()
                    .getFile("StoredCredential");
            java.io.File f = a.getFile();

            String directory = f.getAbsolutePath().
                    substring(0, f.getAbsolutePath().lastIndexOf(java.io.File.separator));

            DATA_STORE_DIR = new java.io.File(directory);

            f.renameTo(new java.io.File(directory + "/" + a.getNome()));

            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        String clientSecret = library.Settings.getClientSecret();
        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new StringReader(clientSecret));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void teste() throws IOException {
        // Build a new authorized API client service.
        Drive service = getDriveService();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setMaxResults(10)
                .execute();
        List<File> files = result.getItems();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getTitle(), file.getId());
            }
        }
    }

    public static String getFolderID(Drive service, String folderName) throws IOException {
        FileList result = service.files().list()
                .setMaxResults(9999)
                .execute();
        List<File> files = result.getItems();
        String folderID = null;
        for (File file : files) {
            if (file.getTitle().equals(folderName)) {
                folderID = file.getId();
                break;
            }
        }
        return folderID;
    }

    public static Boolean insertFile(Drive service,
            String name,
            String description,
            java.io.File f,
            String folderName,
            String mimetype) {
        try {
            // File's metadata.
            File body = new File();
            body.setTitle(name);
            body.setDescription(description);

            String mimeType = mimetype;
            if (mimeType == null || mimeType.equals("")) {
                mimeType = URLConnection.guessContentTypeFromName(name);
            }

            body.setMimeType(mimeType);

            // Set the parent folder.
            if (folderName != null && folderName.length() > 0) {
                body.setParents(
                        Arrays.asList(new ParentReference().setId(getFolderID(service, folderName))));
            }

            // File's content.            
            FileContent mediaContent = new FileContent(mimeType, f);

            File file = service.files().insert(body, mediaContent).execute();

            // Uncomment the following line to print the File ID.
            // System.out.println("File ID: %s" + file.getId());
            return true;
        } catch (IOException e) {
            Log.writeError("Erro ao realizar upload de um arquivo.", e.toString(), ModuloEnum.INTERNO);
            return false;
        }
    }
}
