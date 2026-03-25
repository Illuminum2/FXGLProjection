package at.htl.fxglprojection.objects;

public class ObjFormatException extends Exception {
    private String message;
    private Integer line;

    public ObjFormatException(String message) {
        super(message);
        this.message = message;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String getMessage() {
        if (line != null)
            return line.toString() + ": " + message;

        return message;
    }
}
