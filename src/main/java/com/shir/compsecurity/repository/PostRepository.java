package com.shir.compsecurity.repository;

import com.shir.compsecurity.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {}
