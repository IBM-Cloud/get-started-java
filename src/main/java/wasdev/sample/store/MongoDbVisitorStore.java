/******************************************************************************
 * Copyright (c) 2018 IBM Corp.                                               *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 *    http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/
package wasdev.sample.store;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.String;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bson.Document;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import wasdev.sample.Visitor;

public class MongoDbVisitorStore implements VisitorStore {

    private MongoDatabase db = null;
    private MongoCollection<Document> collection;
    private static final String databaseName   = "mydb",
                                collectionName = "mycollection";

    public MongoDbVisitorStore() {
        MongoClient mongo = createClient();
        if (mongo != null) {
            db = mongo.getDatabase(databaseName);
            if (db == null) {
                System.out.println("Could not find database with name \"" +
                                   databaseName +
                                   "\". A new database will be created.");
            }
        }
        collection = db.getCollection(collectionName);
    }

    public MongoDatabase getDB() {
        return db;
    }

    /**
     * Retrieve the file name for the KeyStore, or default to a file name of
     * "mongoKeyStore" in the current user's directory.
     * @return A String that either contains the file name specified in
     *         javax.net.ssl.trustStore, or a "mongoKeyStore" prefixed by the
     *         current user's directory.
     */
    private static String getKeyStoreName() {
        String keyStoreName = System.getProperty("java.net.ssl.trustStore");
        return keyStoreName == null ? System.getProperty("user.dir") +
                                      File.separator + "mongoKeyStore" :
                                      keyStoreName;
    }

    /**
     * Retrieve the password for the KeyStore, or default to "keypass".
     *
     * @return A String that either contains the password specified in
     *         javax.net.ssl.trustStorePassword, or "keypass".
     */
    private static String getKeyStorePass() {
        String keyStorePass = System.getProperty("javax.net.ssl.trustStorePassword");
        return keyStorePass == null ? "keypass" : keyStorePass;
    }

    /**
     * Given a file name and password, either locate and load an existing
     * KeyStore or create a new one.
     *
     * @param keyStoreName String specifying the name of the KeyStore file.
     * @param keyStorePass String specifying the password for the KeyStore.
     * @return A KeyStore object located at the specified location, or null
     *         if an error occurs.
     */
    private static KeyStore initializeKeyStore(String keyStoreName,
                                               String keyStorePass) {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            System.out.println("Could not get KeyStore instance.");
            //e.printStackTrace();
            return null;
        }
        
        File file = new File(keyStoreName);

        FileInputStream inputStream = null;
        char [] keyStorePassArray = "null".toCharArray();

        if (file.exists()) {
            System.out.println("Found existing TrustStore: \"" + keyStoreName +
                               "\"");
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("Could not locate TrustStore file.");
                //e.printStackTrace();
                return null;
            }
            keyStorePassArray = keyStorePass.toCharArray();
        } else {
            System.out.println("Creating new TrustStore: \"" + keyStoreName +
                               "\"");
        }

        try {
            keyStore.load(inputStream, keyStorePassArray);
        } catch (IOException | NoSuchAlgorithmException |
                 CertificateException e) {
            System.out.println("Could not load TrustStore.");
            //e.printStackTrace();
            return null;
        }

        return keyStore;
    }

    /**
     * Create a Certificate object given a Base64-encoded String. This method
     * expects "-----BEGIN CERTIFICATE-----" and "-----END CERTIFICATE-----"
     * endings to be removed from the string by the caller.
     *
     * @param certString Base64-encoded String describing a PEM-formatted SSL
     *                   certificate.
     * @return A Certificate object generated from the provided String, or
     *         null if an error occurs.
     */
    private static Certificate generateCertificate(String certString) {
        byte [] decoded = Base64.getDecoder().decode(certString);
        ByteArrayInputStream bytes = new ByteArrayInputStream(decoded);
        CertificateFactory certFactory = null;

        try {
            certFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            System.out.println("Could not prepare certificate factory.");
            //e.printStackTrace();
            return null;
        }

        Certificate cert = null;
        try {
            cert = certFactory.generateCertificate(bytes);
        } catch (CertificateException e) {
            System.out.println("Unable to generate certificate.");
            //e.printStackTrace();
            return null;
        }
        return cert;
    }

    /**
     * Add a Base64-encoded, PEM-formatted SSL certificate to a specified
     * KeyStore.
     *
     * @param keyStore The KeyStore that will store the new certificate.
     * @param certString A Base64-encoded, PEM-formatted String describing an
     *                   SSL certificate. The "-----BEGIN CERTIFICATE-----" and
     *                   "-----END CERTIFICATE-----" endings should already be
     *                   removed by the caller.
     * @param keyStoreName A String specifying the file name of the KeyStore.
     * @param keyStorePass A String specifying the password for the KeyStore.
     */
    private static void addCertToKeyStore(KeyStore keyStore,
                                          String certString) {
        Certificate cert = generateCertificate(certString);
        try {
            keyStore.setCertificateEntry("mykey", cert);
        } catch (KeyStoreException e) {
            System.out.println("Unable to add certificate to the TrustStore.");
            //e.printStackTrace();
        }

        String keyStoreName = System.getProperty("javax.net.ssl.trustStore"),
               keyStorePass = System.getProperty("javax.net.ssl.trustStorePassword");
        try {
            keyStore.store(new FileOutputStream(keyStoreName), keyStorePass.toCharArray());
            System.out.println("Successfully added certificate to TrustStore.");
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                 CertificateException e) {
            System.out.println("Unable to store updated TrustStore.");
            //e.printStackTrace();
        }
    }

    private static MongoClient createClient() {
        
        //Update the following lines if using Self Signed Certs and uncomment lines 223, 224, 241
        String keyStoreName = null,
               keyStorePass = null;
        //System.out.println("Using TrustStore name \"" + keyStoreName +
        //                   "\" and password \"" + keyStorePass + "\"");
        KeyStore keyStore = initializeKeyStore(keyStoreName, keyStorePass);
        String url        = null,
               certString = null;
        //End updates for self signed certs
        
        if (System.getenv("VCAP_SERVICES") != null) {
            // When running in Bluemix, the VCAP_SERVICES env var will have the
            // credentials for all bound/connected services
            // Parse the VCAP JSON structure looking for mongodb.
            JsonObject mongoCredentials = VCAPHelper.getCloudCredentials("mongodb");

            if (mongoCredentials == null) {
                System.out.println("No MongoDB database service bound to this application");
                return null;
            }
            url = mongoCredentials.get("uri").getAsString();
            //certString = mongoCredentials.get("ca_certificate_base64").getAsString();
        } else {
            System.out.println("Running locally. Looking for credentials in mongo.properties");
            url = VCAPHelper.getLocalProperties("mongo.properties").getProperty("mongo_url");
            if (url == null || url.length() == 0) {
                System.out.println("To use a database, set the MongoDB url in src/main/resources/mongo.properties");
                return null;
            }
            
            certString = VCAPHelper.getLocalProperties("mongo.properties").getProperty("mongo_ssl");
        }

        if (keyStore != null) {
            System.setProperty("javax.net.ssl.trustStore", keyStoreName);
            System.setProperty("javax.net.ssl.trustStorePassword", keyStorePass);

            addCertToKeyStore(keyStore, certString);
        } else {
            System.out.println("A TrustStore could not be found or created.");
        }

        try {
            System.out.println("Connecting to MongoDb");
            MongoClient client = new MongoClient(new MongoClientURI(url));
            return client;
        } catch (Exception e) {
            System.out.println("Unable to connect to database");
            //e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection<Visitor> getAll() {
        Collection<Visitor> docs = new HashSet<Visitor>();
        try {
            MongoIterable<Document> foundDocs = collection.find();
            for (Document doc : foundDocs) {
                Visitor visitor = new Visitor();
                visitor.set_id(doc.getObjectId("_id").toString());
                visitor.setName(doc.getString("name"));
                docs.add(visitor);
            }
        } catch (Exception e) {
            return null;
        }
        return docs;
    }

    @Override
    public Visitor get(String name) {
        Document doc = collection.find(Filters.eq("_id", name)).first();
        Visitor visitor = new Visitor();
        visitor.set_id(doc.getObjectId("_id").toString());
        visitor.setName(doc.getString("name"));
        return visitor;
    }

    @Override
    public Visitor persist(Visitor td) {
        Document doc = new Document("name", td.getName()).append("count", 1);
        collection.insertOne(doc);
        doc = collection.find(Filters.eq("name", td.getName())).first();
        td.set_id(doc.getObjectId("_id").toString());
        return td;
    }

    @Override
    public Visitor update(String id, Visitor newVisitor) {
        collection.updateOne(Filters.eq("name", newVisitor.getName()), new Document("$set", new Document("name", newVisitor.getName()).append("count", 2)));

        Document myDoc = collection.find(Filters.eq("name", newVisitor.getName())).first();
        newVisitor.set_id(myDoc.getObjectId("_id").toString());
        return newVisitor;
    }

    @Override
    public void delete(String name) {
        collection.deleteOne(Filters.eq("name", name));
    }

    @Override
    public int count() throws Exception {
        return (int) collection.count();
    }
}
