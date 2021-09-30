package dataStorage;

public class Category {
    int id;
    String name;

    public Category(int catId,String catName){
        id = catId;
        name = catName;
    }

    public void print(){
        System.out.println("Category: " + name);
    }
}
