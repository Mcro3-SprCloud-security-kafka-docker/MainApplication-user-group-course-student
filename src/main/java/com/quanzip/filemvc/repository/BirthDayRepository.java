package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.BirthDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BirthDayRepository extends JpaRepository<BirthDay, Long> {

//    @Query(value = "select b from BirthDay where month")
//    List<BirthDay> findAllByBirthDateLessThanEqualAndBirthDateGreaterThanEqual(LocalDate endDate, LocalDate startDate);
}
