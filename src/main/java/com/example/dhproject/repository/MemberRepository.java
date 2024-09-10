package com.example.dhproject.repository;

import com.example.dhproject.domain.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<MemberEntity> findByUsername(String username);

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findById(Long memberId);

    @Override
    Page<MemberEntity> findAll(Pageable pageable);
}
