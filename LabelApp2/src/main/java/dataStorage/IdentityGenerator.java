package dataStorage;

public class IdentityGenerator {
    private int img_id = 0;
    private int ann_id = 0;
    private int cat_id = 0;

    public int getImg_id(){
        int temp_id = img_id;
        img_id++;
        return temp_id;
    }
    public int getAnn_id(){
        int temp_id = ann_id;
        ann_id++;
        return temp_id;
    }
    public int getCat_id(){
        int temp_id = cat_id;
        cat_id++;
        return temp_id;
    }
}
