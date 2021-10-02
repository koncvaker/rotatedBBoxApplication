package dataStorage;

import javafx.scene.shape.Rectangle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class JSONData {
    Vector<Image> images;
    Vector<AnnotationCore> annotations;
    Vector<Category> categories;
    String path;

    //Constructor stuff
    public JSONData(){
        images      = new Vector<Image>();
        annotations = new Vector<AnnotationCore>();
        categories  = new Vector<Category>();
    }

    //Set stuff
    public void setPath(String path){this.path = path;}

    //Add stuff
    public void addImage(Image image){
        images.add(image);
    }
    public void addAnnotation(AnnotationCore annotation){
        annotations.add(annotation);
    }
    public void addCategory(Category category){
        categories.add(category);
    }

    //Get stuff
    public Image getImageByID(int imageID){
        return images.get(0);
    }
    public Image getImageByFilename(String filename){
        if(images.size() != 0){
            for (Image i : images) {
                if(i.fileName.equals(filename)){
                    return i;
                }
            }
        }
        return null;
    }
    public int  getImageIDByFilename(String filename){
        if(images.size() != 0){
            for (Image i : images) {
                if(i.fileName.equals(filename)){
                    return i.id;
                }
            }
        }
        return -1;
    }
    public AnnotationCore getAnnotationByID(int annotationID){
        return annotations.get(0);
    }
    public Vector<AnnotationCore> getAnnotationsByImageID(int imageID){
        Vector<AnnotationCore> annotationCores = new Vector<>();
        for (AnnotationCore i : annotations) {
            if(i.imgID == imageID){
                annotationCores.add(i);
            }
        }
        return annotationCores;
    }
    public Category getCategoryByID(int categoryID){
        return categories.get(0);
    }
    public int getCategoryIDByName(String name){
        if(categories.size() != 0){
            for(Category i : categories){
                if(i.name.equals(name)){
                    return i.id;
                }
            }
        }
        return -1;
    }
    public JSONObject getJSONObject(){
        JSONObject retval = new JSONObject();

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
        retval.put("images",imagesArray);

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

            //JSONArray segmentArrayContainer = new JSONArray();
            JSONArray segmentArray = new JSONArray();

            for(Vector<Double> j : i.segment.segmentation){
                JSONArray coordinate = new JSONArray();
                for(double k : j){
                    coordinate.add(k);
                }
                segmentArray.add(coordinate);
            }

            //segmentArrayContainer.add(segmentArray);
            annotation.put("segmentation",segmentArray);
            annotation.put("area",i.area);
            annotation.put("iscrowd",i.isCrowd);
            annotArray.add(annotation);
        }
        retval.put("annotations",annotArray);

        JSONArray categoriesArray = new JSONArray();
        for(Category i : categories){
            JSONObject category = new JSONObject();
            category.put("id",i.id);
            category.put("name",i.name);
            categoriesArray.add(category);
        }
        retval.put("categories",categoriesArray);

        return retval;
    }

    public Vector<Rectangle> getBBoxRectangles(String filename, Double imgRatio){
        Vector<Rectangle> rectangleVector = new Vector<Rectangle>();
        Vector<AnnotationCore> BBoxVector = getAnnotationsByImageID(getImageIDByFilename(filename));
        for(AnnotationCore i : BBoxVector){
            rectangleVector.add(i.box.toRectangle(imgRatio));
        }
        return rectangleVector;
    }

    //Print stuff
    public void printImages(){
        for(Image i : images){
            i.print();
        }
    }
    public void printAnnotations(){
        for(AnnotationCore i : annotations){
            i.print();
        }
    }
    public void printCategories(){
        for(Category i : categories){
            i.print();
        }
    }
    public void printAll(){
        printImages();
        printCategories();
        printAnnotations();
    }



}
