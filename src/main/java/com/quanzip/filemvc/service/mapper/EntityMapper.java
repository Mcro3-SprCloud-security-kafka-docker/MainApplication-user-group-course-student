package com.quanzip.filemvc.service.mapper;

import java.util.List;

public interface EntityMapper<D, E>{
    D toDto(E e);
    E toEntity(D d);

    List<D> toDtos(List<E> es);
    List<E> toEntites(List<D> ds);
}
