package dataStorage;

public class Image {
    int     id;
    String  fileName;
    int     width;
    int     height;

    public Image(int id,String fileName,int width,int height){
        this.id         = id;
        this.fileName   = fileName;
        this.width      = width;
        this.height     = height;
    }

    public boolean nameEquals(String _file_name){
        return this.fileName.equals(_file_name);
    }
    public boolean idEquals(int id){ return this.id == id;}

    public void print(){
        System.out.println("ImageID: " + id);
        System.out.println("Filename: " + fileName);
        System.out.println("Width: " + width);
        System.out.println("Height: "+ height);
    }
}
