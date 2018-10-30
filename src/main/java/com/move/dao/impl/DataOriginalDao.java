package com.move.dao.impl;

import com.move.model.DataOriginal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataOriginalDao extends JpaRepository<DataOriginal,Integer> {
    public List<DataOriginal> findAllByFileId(Integer fileId);
    public List<DataOriginal> findAllByMonth(String month);
    public List<DataOriginal> findAllByMonthAndFileId(String month,Integer fileId);

}
