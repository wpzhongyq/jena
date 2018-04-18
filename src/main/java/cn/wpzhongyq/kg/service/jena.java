package cn.wpzhongyq.kg.service;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Created by wpzhongyq on 23/03/2018.
 */
@Service
public class jena
{
    public void define()
    {
        Model model = readXml("/Users/wpzhongyq/IdeaProjects/jena/src/main/java/cn/wpzhongyq/kg/service/container.xml");

        Bag smiths = model.createBag("smiths");
        StmtIterator iter = model.listStatements(
                new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
                    public boolean selects(Statement s) {
                        return s.getString().endsWith("Smith");
                    }
                });
        while (iter.hasNext()) {
            smiths.add(iter.nextStatement().getSubject());
        }

        //Model model = model2.intersection(model1);

        smiths = model.getBag("smiths");

        NodeIterator iter2 = smiths.iterator();
        if (iter2.hasNext()) {
            System.out.println("The bag contains:");
            while (iter2.hasNext()) {
                System.out.println("  " +
                        ((Resource) iter2.next())
                                .getProperty(VCARD.FN)
                                .getString());
            }
        } else {
            System.out.println("The bag is empty");
        }

        //model.write(System.out);
        //model.write(System.out, "RDF/XML-ABBREV");
        model.write(System.out, "N-TRIPLES");

    }

    private Model createRDF()
    {
        // some definitions
        String personURI    = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource johnSmith = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,
                        model.createResource()
                                .addProperty(VCARD.Given, givenName)
                                .addProperty(VCARD.Family, familyName));

        return model;
    }

    private void printTriple(Model model)
    {
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();  // get next statement
            Resource  subject   = stmt.getSubject();     // get the subject
            Property  predicate = stmt.getPredicate();   // get the predicate
            RDFNode   object    = stmt.getObject();      // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
        }
    }

    private Model readXml(String inputFileName)
    {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open( inputFileName );
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read(in, null);

        return model;
    }

    private  String NaviModel(Model model)
    {
        String ret = "";
        Resource vcard = model.getResource("http://somewhere/JohnSmith");
        vcard.addProperty(VCARD.NICKNAME, "Smithy")
                .addProperty(VCARD.NICKNAME, "Adman");

        StmtIterator iter = vcard.listProperties(VCARD.NICKNAME);
        while (iter.hasNext()) {
            ret += "    " + iter.nextStatement()
                    .getObject()
                    .toString();
        }
        return ret;
    }
}
