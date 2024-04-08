package com.sspl.demo.repository;

import com.sspl.demo.domain.Dashboard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {}
