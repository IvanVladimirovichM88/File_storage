import java.io.Serializable;

public class FileNameWithId implements Serializable {
    private int id;
    private String fileName;

    public FileNameWithId(){}

    public FileNameWithId(int id, String fileName){
        this.id = id;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return fileName;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }
}
