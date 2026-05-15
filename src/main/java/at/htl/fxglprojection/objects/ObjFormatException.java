package at.htl.fxglprojection.objects;

public class ObjFormatException extends Exception {
    private final String message;
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
            return "Line " + line + ": " + message;

        return message;
    }
}
