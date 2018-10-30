package com.move.dao.impl;

import com.move.model.DataResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataResultDao extends JpaRepository<DataResult,Integer> {
    public List<DataResult> findAllByFileId(Integer fileId);
    public List<DataResult> findAllByMonth(String month);
    public List<DataResult> findAllByMonthAndFileId(String month,Integer fileId);
}
