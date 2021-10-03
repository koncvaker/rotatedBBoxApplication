package dataStorage;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import static java.lang.Integer.parseInt;

public class DataWorker {

    //Variable declaration stuff
    public JSONData storage;
    private IdentityGenerator     idGenerator  = new IdentityGenerator();
    private String  XMLReadPath;
    private String  JSONFileName;
    private String  JSONReadPath;
    private String  JSONWritePath;


    //Constructor stuff
    public DataWorker(){
        storage = new JSONData();
    }

    //Add stuff
    public void addImage(String file_name,int width,int height){
        if(storage.getImageIDByFilename(file_name) == -1){
            storage.addImage(new Image(idGenerator.getImg_id(),file_name,width,height));
        }
    }
    public void addAnnotation(int img_id, int cat_id, double area, int iscrowd, AnnotationBBox box, AnnotationSegmentationNew seg){
        storage.addAnnotation(new AnnotationCore(idGenerator.getAnn_id(),img_id,cat_id,area,iscrowd,box,seg));

    }
    public void addCategory(String category){
        if(storage.getCategoryIDByName(category) == -1){
            storage.addCategory(new Category(idGenerator.getCat_id(),category));
        }
    }


    //Print stuff
    public void printImages(){
        storage.printImages();
    }
    public void printAnnotations(){
        storage.printAnnotations();
    }
    public void printCategories(){
        storage.printCategories();
    }
    public void printAll(){
        storage.printAll();
    }

    //Set stuff
    public void setJSONFileName(String fileName)    {this.JSONFileName  = fileName;}
    public void setJSONWritePath(String writePath)  {this.JSONWritePath = writePath;}
    public void setXMLReadPath(String XMLReadPath)  { this.XMLReadPath  = XMLReadPath;}
    public void setJSONReadPath(String jsonReadPath){ this.JSONReadPath = jsonReadPath;}



    //Read stuff from files
    public void readJSON(){
        String path = JSONReadPath;
        resetStorage();
        resetIdentityGenerator();
        JSONParser jsonParser = new JSONParser();
        try
        {
            FileReader reader = new FileReader(path);
            Object obj = jsonParser.parse(reader);
            JSONObject root = (JSONObject) obj;
            JSONArray imageJSONRead = (JSONArray) root.get("images");
            JSONArray annotationsJSONRead = (JSONArray) root.get("annotations");
            JSONArray categoriesJSONRead = (JSONArray) root.get("categories");

            for(Object i : imageJSONRead){
                parseImage((JSONObject)i);
            }
            for(Object i : annotationsJSONRead){
                parseAnnotation((JSONObject)i);
            }
            for(Object i : categoriesJSONRead){
                parseCategory((JSONObject)i);
            }

            storage.printAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readAllXML(){
        resetStorage();
        resetIdentityGenerator();
        String path = XMLReadPath;
        String[] pathNames;
        File f = new File(path);
        pathNames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathNames) {
            // Print the names of files and directories
            System.out.println("Reading: " + pathname);
            if (pathname.endsWith(".xml")) {
                //System.out.println(pathname);
                readXML(pathname);
            }

        }

    }
    void readXML(String fname) {
        try {
            File inputFile = new File(XMLReadPath + File.separator + fname);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            String fileName;
            int width;
            int height;
            String category;
            double bboxX;
            double bboxY;
            double bboxHeight;
            double bboxWidth;
            double bboxAngle;

            fileName = doc.getDocumentElement().getElementsByTagName("path").item(0).getTextContent();
            width    = parseInt(doc.getDocumentElement().getElementsByTagName("width").item(0).getTextContent());
            height   = parseInt(doc.getDocumentElement().getElementsByTagName("height").item(0).getTextContent());

            addImage(fileName,width,height);
            int imageID;
            int catID;
            AnnotationBBox bbox;
            AnnotationSegmentationNew seg;
            imageID = storage.getImageIDByFilename(fileName);

            NodeList nList = doc.getElementsByTagName("object");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    category = eElement.getElementsByTagName("name").item(0).getTextContent();
                    addCategory(category);
                    catID = storage.getCategoryIDByName(category);
                    bboxX = Double.parseDouble(eElement.getElementsByTagName("cx").item(0).getTextContent());
                    bboxY = Double.parseDouble(eElement.getElementsByTagName("cy").item(0).getTextContent());
                    bboxWidth = Double.parseDouble(eElement.getElementsByTagName("w").item(0).getTextContent());
                    bboxHeight = Double.parseDouble(eElement.getElementsByTagName("h").item(0).getTextContent());


                    bboxAngle = Double.parseDouble(eElement.getElementsByTagName("angle").item(0).getTextContent());
                    //Normalize angle between -pi/4 and pi/4 (-45° | +45°)

                    double tempBBoxAngle = Math.toDegrees(bboxAngle);
                    while (tempBBoxAngle < -45){
                        tempBBoxAngle += 90;
                        double tempwidth;
                        tempwidth = bboxWidth;
                        bboxWidth = bboxHeight;
                        bboxHeight = tempwidth;
                    }
                    while (tempBBoxAngle > 45){
                        tempBBoxAngle -= 90;
                        double tempWidth;
                        tempWidth = bboxWidth;
                        bboxWidth = bboxHeight;
                        bboxHeight = tempWidth;
                    }
                    //Convert center to TOP left for training
                    bboxX = bboxX - bboxWidth / 2;
                    bboxY = bboxY - bboxHeight / 2;
                    //We have to measure anti-clockwise for training!
                    bboxAngle = -1 * Math.toRadians(tempBBoxAngle);

                    bbox = new AnnotationBBox(bboxX,bboxY,bboxWidth,bboxHeight,bboxAngle);
                    seg = new AnnotationSegmentationNew(bboxX,bboxY,bboxWidth,bboxHeight,bboxAngle);
                    addAnnotation(imageID,catID,bboxWidth*bboxHeight,0,bbox,seg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Parse stuff
    public void parseImage(JSONObject image){
        String   filename    = (String) image.get("file_name");
        Long     width      = (Long) image.get("width");
        Long     height     =  (Long) image.get("height");
        Long     id         =  (Long) image.get("id");
        storage.addImage(new Image(id.intValue(),filename,width.intValue(),height.intValue()));
    }
    public void parseAnnotation(JSONObject annotation){
        Double      area          = (Double)      annotation.get("area");
        Long        category_id   = (Long)        annotation.get("category_id");
        JSONArray   bbox          = (JSONArray)   annotation.get("bbox");
        Long        iscrowd       = (Long)        annotation.get("iscrowd");
        JSONArray   segmentation  = (JSONArray)   annotation.get("segmentation");
        Long        id            = (Long)        annotation.get("id");
        Long        image_id      = (Long)        annotation.get("image_id");

        Vector<Double> bboxVector = new Vector<Double>();
        for(Object i : bbox){
            bboxVector.add((Double) i);
        }
        Vector<Vector<Double>> segmentationVector = new Vector<Vector<Double>>();

        for(Object i : segmentation){
            JSONArray innerSegmentation = (JSONArray) i;
            Vector<Double> innerSegmentationVector = new Vector<Double>();
            for(Object j : innerSegmentation){
                innerSegmentationVector.add((Double) j);
            }
            segmentationVector.add(innerSegmentationVector);
        }

        storage.addAnnotation(new AnnotationCore(id.intValue(),image_id.intValue(),category_id.intValue(),area,iscrowd.intValue(),new AnnotationBBox(bboxVector),new AnnotationSegmentationNew(segmentationVector)));

    }
    public void parseCategory(JSONObject category){
        Long    id      = (Long)    category.get("id");
        String  name    = (String)  category.get("name");
        storage.addCategory(new Category(id.intValue(),name));
    }

    //Write stuff to files
    public void writeDataToJSON(){
        JSONObject obj = storage.getJSONObject();
        FileWriter file;
        try {
            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter(JSONWritePath + File.separator + JSONFileName);
            file.write(obj.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //Reset stuff
    public void resetStorage(){
        storage = new JSONData();
    }
    public void resetIdentityGenerator(){
        idGenerator = new IdentityGenerator();
    }




}
