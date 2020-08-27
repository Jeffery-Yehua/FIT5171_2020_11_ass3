package rockets.util;

public class outcome implements Comparable<outcome> {


    private int failed;
    private int successful;
    private double percentage;

    public outcome() {
        this.failed = 0;
        this.successful = 0;
        this.percentage = 0;
    }


    public void setFailed(int failed) {
        this.failed = failed;
        percentage = this.successful / (this.successful + this.failed);
    }

    public void setSuccessful(int successful) {
        this.successful = successful;
        percentage = this.successful / (this.successful + this.failed);
    }


    public int getFailed() {
        return failed;
    }

    public int getSuccessful() {
        return successful;
    }

    public double getPercentage() {
        return percentage;
    }

    public void incrementsuccessful(){
        this.successful++;
        percentage = this.successful / (this.successful + this.failed);
    }


    public void incrementfailed(){
        this.failed++;
        percentage = this.successful / (this.successful + this.failed);
    }

    @Override
    public int compareTo(outcome o) {
        if(this.percentage-o.percentage>0)
            return  1;
        else if (this.percentage-o.percentage==0)
            return 0;
        else return -1;
    }
}
