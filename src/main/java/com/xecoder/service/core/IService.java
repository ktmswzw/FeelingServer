package com.xecoder.service.core;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/12-15:05
 * Feeling.com.xecoder.service
 */
public interface IService<T> extends Pagination {

    public long count();

    public T findById(String id);

    public Iterable<T> findAll(int page, int size, String sortColumn);

    public T findByPk(Object... keys);

    public Iterable<T> findByNameLike(String name, String sortColumn);

    public Iterable<T> search(String keyword, int page, int size, String sortColumn);

    public T save(T model);

    public Iterable<T> save(Iterable<T> model);

    public void delete(String id);

    public void delete(Iterable<T> model);
}
