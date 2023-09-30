public class ParameterFunction {
    protected String type;
    protected int parameterCount;

    ParameterFunction() {
        this.type = "None";
        this.parameterCount = 0;
    }

    public String getType() {
        return type;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public void setType(String type) {
        this.type = type;
    }
}
