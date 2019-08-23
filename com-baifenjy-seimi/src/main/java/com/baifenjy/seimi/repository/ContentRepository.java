package com.baifenjy.seimi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.baifenjy.seimi.entity.BlogContent;

@Repository
public interface ContentRepository extends JpaRepository<BlogContent,Long>, JpaSpecificationExecutor<BlogContent>
{

}
