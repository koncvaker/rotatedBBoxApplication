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
    public  final Vector<Image>          images;
    private final Vector<AnnotationCore>    annotations;
    private final Vector<Category>      categories;
    private IdentityGenerator     idGenerator  = new IdentityGenerator();
    private String                writePath;
    private String                fileName;
    private String                readPath;

    //Constructor stuff
    public DataWorker(String path, String name){
        images      = new Vector<Image>();
        annotations = new Vector<AnnotationCore>();
        categories  = new Vector<Category>();
        writePath   = path;
        fileName    = name;
    }
    public DataWorker(){
        storage = new JSONData();
        images      = new Vector<Image>();
        annotations = new Vector<AnnotationCore>();
        categories  = new Vector<Category>();
        writePath   = "";
        fileName    = "";
    }

    //Add stuff
    public int addImage(String file_name,int width,int height){
        boolean inImages = false;
        if(images.size() != 0){
            for (Image test:images) {
                if(test.nameEquals(file_name)){
                    inImages = true;
                    break;
                }
            }
        }
        if(!inImages){
            images.add(new Image(idGenerator.getImg_id(),file_name,width,height));
        }
        return 0;
    }
    public void addAnnotation(int img_id, int cat_id, double area, int iscrowd, AnnotationBBox box, AnnotationSegmentation seg){
        // TODO: 2021. 09. 28. Repair segmentation
        //annotations.add(new AnnotationCore(idGenerator.getAnn_id(),img_id,cat_id,area,iscrowd,box,seg));
    }
    public void addCategory(String category){
        boolean inCategories = false;
        if(categories.size() != 0){
            for (Category test:categories) {
                if(test.name.equals(category)){
                    inCategories = true;
                    break;
                }
            }
        }
        if(!inCategories){
            categories.add(new Category(idGenerator.getCat_id(),category));
        }
    }
    public void addImage(Image img){storage.addImage(img);}

    //Print stuff
    public void printImages(){
        for (Image i: images) {
            i.print();
        }
    }
    public void printAnnotations(){
        for (AnnotationCore i: annotations) {
            i.print();
        }
    }
    public void printCategories(){
        for( Category i : categories){
            i.print();
        }
    }

    //Set stuff
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public void setWritePath(String writePath){
        this.writePath = writePath;
    }
    public void setReadPath(String readPath) { this.readPath = readPath;    }

    //Get stuff
    public int getImageID(String fileName){
        for (Image i: images) {
            if(i.nameEquals(fileName)){
                return i.id;
            }
        }
        return -1;
    }
    public int getCategoryID(String category){
        for (Category i: categories) {
            if(i.name.equals(category)){
                return i.id;
            }
        }
        return -1;
    }

    //Read stuff from files
    public void readJSON(String path){
        resetStorage();
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
    public void readAllXML(String path){

        String[] pathnames;
        File f = new File(path);
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            if (pathname.endsWith(".xml")) {
                //System.out.println(pathname);
                readXML(pathname);
            }

        }

    }
    void readXML(String fname) {

        try {
            // TODO: 2021. 09. 27. Set pathname to a real one when you call the function
            File inputFile = new File(readPath + fname);
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
            AnnotationSegmentation seg;
            imageID = getImageID(fileName);

            NodeList nList = doc.getElementsByTagName("object");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    category = eElement.getElementsByTagName("name").item(0).getTextContent();
                    addCategory(category);
                    catID = getCategoryID(category);
                    bboxX = Double.parseDouble(eElement.getElementsByTagName("cx").item(0).getTextContent());
                    bboxY = Double.parseDouble(eElement.getElementsByTagName("cy").item(0).getTextContent());
                    bboxWidth = Double.parseDouble(eElement.getElementsByTagName("w").item(0).getTextContent());
                    bboxHeight = Double.parseDouble(eElement.getElementsByTagName("h").item(0).getTextContent());

                    //Convert center to bottom left for training
                    bboxX = bboxX - bboxWidth / 2;
                    bboxY = bboxY - bboxHeight / 2;
                    bboxAngle = Double.parseDouble(eElement.getElementsByTagName("angle").item(0).getTextContent());
                    //Normalize angle between -pi/4 and pi/4 (-45° | +45°)
                    // TODO: 2021. 09. 30. When displayed it rotation has to be inverted, because we need to measure anti clockwise for training, and clockwise for displaying
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
                    bboxAngle = Math.toRadians(tempBBoxAngle);


                    bbox = new AnnotationBBox(bboxX,bboxY,bboxWidth,bboxHeight,bboxAngle);
                    seg = new AnnotationSegmentation(bboxX,bboxY,bboxWidth,bboxHeight,bboxAngle);
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

        JSONObject obj = new JSONObject();

        JSONArray imagesArray = new JSONArray();
        for (Image i: images) {
            JSONObject image = new JSONObject();
            image.put("id",i.id);

            Path path = Paths.get(i.fileName);
            Path fileName = path.getFileName();
            image.put("file_name",fileName.toString());
            image.put("width",i.width);
            image.put("height",i.height);
            imagesArray.add(image);
        }
        obj.put("images",imagesArray);

        JSONArray annotArray = new JSONArray();
        for (AnnotationCore i : annotations) {
            JSONObject annotation = new JSONObject();
            annotation.put("id",i.annotID);
            annotation.put("image_id",i.imgID);
            annotation.put("category_id",i.catID);
            JSONArray bboxArray = new JSONArray();
            bboxArray.add(i.box.x);
            bboxArray.add(i.box.y);
            bboxArray.add(i.box.width);
            bboxArray.add(i.box.height);
            bboxArray.add(i.box.theta);
            annotation.put("bbox",bboxArray);

            JSONArray segmentArrayContainer = new JSONArray();
            JSONArray segmentArray = new JSONArray();
            // TODO: 2021. 09. 28. Repair annotations
            /*
            segmentArray.add(i.segment.x1);
            segmentArray.add(i.segment.y1);
            segmentArray.add(i.segment.x2);
            segmentArray.add(i.segment.y2);
            segmentArray.add(i.segment.x3);
            segmentArray.add(i.segment.y3);
            segmentArray.add(i.segment.x4);
            segmentArray.add(i.segment.y4);*/

            segmentArrayContainer.add(segmentArray);
            annotation.put("segmentation",segmentArrayContainer);
            annotation.put("area",i.area);
            annotation.put("iscrowd",i.isCrowd);
            annotArray.add(annotation);
        }
        obj.put("annotations",annotArray);

        JSONArray categoriesArray = new JSONArray();
        for(Category i : categories){
            JSONObject category = new JSONObject();
            category.put("id",i.id);
            category.put("name",i.name);
            categoriesArray.add(category);
        }
        obj.put("categories",categoriesArray);
        FileWriter file;
        try {

            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter(writePath+fileName);
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
