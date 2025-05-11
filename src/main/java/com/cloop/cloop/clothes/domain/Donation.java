package com.cloop.cloop.clothes.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cloth_id", nullable = false)
    private Cloth cloth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status; // ENUM: PENDING / COMPLETE / REJECTED

    @Column(nullable = false)
    private LocalDate completedDate;

    public enum Status {
        PENDING,
        COMPLETE,
        REJECTED
    }
}