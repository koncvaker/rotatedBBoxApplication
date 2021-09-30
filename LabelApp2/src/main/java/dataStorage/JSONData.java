package dataStorage;

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
        return images.get(0);
    }
    public AnnotationCore getAnnotationByID(int annotationID){
        return annotations.get(0);
    }
    public Vector<AnnotationCore> getAnnotationsByImageID(int imageID){
        return new Vector<AnnotationCore>();
    }
    public Category getCategoryByID(int categoryID){
        return categories.get(0);
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
