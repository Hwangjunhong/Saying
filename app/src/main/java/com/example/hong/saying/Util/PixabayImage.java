
package com.example.hong.saying.Util;

import com.example.hong.saying.DataModel.Hit;

import java.util.List;

public class PixabayImage {

    private Integer totalHits;
    private List<Hit> hits = null;
    private Integer total;

    public Integer getTotalHits() {
        return totalHits;
    }


    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
