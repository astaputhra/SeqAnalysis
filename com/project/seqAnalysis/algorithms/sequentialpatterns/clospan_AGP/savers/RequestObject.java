package com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.savers;

/**
 * Created by astaputhra on 1/4/15.
 */
public class RequestObject {
    private int minSub;
    private String time;
    private String type;
    private String remarks;

    public int getMinSub() {
        return minSub;
    }

    public void setMinSub(int minSub) {
        this.minSub = minSub;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
