package org.example.fileshibernate.dto;

public class PrefsDto {

    private String direction;
    private String sortingOrder;

    public PrefsDto() {
    }

    public PrefsDto(String direction, String sortingOrder) {
        this.direction = direction;
        this.sortingOrder = sortingOrder;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSortingOrder() {
        return sortingOrder;
    }

    public void setSortingOrder(String sortingOrder) {
        this.sortingOrder = sortingOrder;
    }
}
