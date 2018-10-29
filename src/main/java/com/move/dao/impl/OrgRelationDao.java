package com.move.dao.impl;

import com.move.model.DataOriginal;
import com.move.model.OrgRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgRelationDao extends JpaRepository<OrgRelation,Integer> {
}
