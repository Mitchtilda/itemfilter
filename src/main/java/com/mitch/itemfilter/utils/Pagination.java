package com.Mitch.itemfilter.utils;

import java.util.ArrayList;
import java.util.List;

public final class Pagination<T> extends ArrayList<T> {

    private int pageSize;

    public Pagination(int pageSize, List<T> objects) {
        this.pageSize = pageSize;
        addAll(objects);
    }

    public int pageSize() {
        return pageSize;
    }

    public int totalPages() {
        return (int) Math.ceil((double) size() / pageSize);
    }

    public boolean exists(int page) {
        return page >= 0 && page < totalPages();
    }

    public List<T> getPage(int page) {
        if (page < 0 || page >= totalPages()) {
            if (page > 0) { // If can go to previous page
                return getPage(page - 1);
            }
            throw new IndexOutOfBoundsException("Page: " + page + ", Total: " + totalPages());
        }

        List<T> objects = new ArrayList<>();

        int min = page * pageSize;
        int max = ((page * pageSize) + pageSize);

        if (max > size()) max = size();

        for (int i = min; max > i; i++) objects.add(get(i));

        return objects;
    }
}
