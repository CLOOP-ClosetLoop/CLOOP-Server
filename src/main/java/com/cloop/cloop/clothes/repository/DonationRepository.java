package com.cloop.cloop.clothes.repository;

import com.cloop.cloop.clothes.domain.Cloth;
import com.cloop.cloop.clothes.domain.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    Optional<Donation> findByCloth(Cloth cloth);
}
