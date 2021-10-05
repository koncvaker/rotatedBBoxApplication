package dataStorage;

public class AnnotationCore {
    int annotID;
    int imgID;
    int catID;
    double area;
    int isCrowd = 0;
    AnnotationBBox box;
    AnnotationSegmentationNew segment;
    double score;

    public AnnotationCore(int annotID, int imgID, int catID, double area, int isCrowd, AnnotationBBox box, AnnotationSegmentationNew seg){
        this.annotID    = annotID;
        this.imgID      = imgID;
        this.catID      = catID;
        this.area       = area;
        this.isCrowd    = isCrowd;
        this.box        = box;
        this.segment    = seg;
        this.score      = 0;
    }

    public AnnotationCore(int imgID, int catID,  AnnotationBBox box, AnnotationSegmentationNew seg,double score){
        //this.annotID    = annotID;
        this.imgID      = imgID;
        this.catID      = catID;
        //   this.area       = area;
        //   this.isCrowd    = isCrowd;
        this.box        = box;
        this.segment    = seg;
        this.score      = score;
    }


    public void print(){
        System.out.println("AnnotID: " + annotID);
        System.out.println("ImgID: "+ imgID);
        System.out.println("CatID: " + catID);
        System.out.println("Area: " + area);
        System.out.println("Iscrowd: " + isCrowd);
        box.print();
        segment.print();
    }
}
